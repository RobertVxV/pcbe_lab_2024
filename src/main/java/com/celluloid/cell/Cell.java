package com.celluloid.cell;

import com.celluloid.Config;
import com.celluloid.FoodPool;
import com.celluloid.GlobalGameStats;
import com.celluloid.event.Event;
import com.celluloid.event.EventQueue;
import com.celluloid.event.EventType;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Cell implements Runnable {
    private static final AtomicInteger cellCounter = new AtomicInteger(0);

    protected final Config config;
    protected final EventQueue eventQueue;
    protected final FoodPool foodPool;

    protected final int cellIndex;
    protected final boolean createdByUser;
    private final GlobalGameStats globalState = GlobalGameStats.getInstance();

    protected int mealsEaten = 0;
    protected boolean alive = true;
    protected Instant lastMealTime = Instant.now();

    public Cell(FoodPool foodPool, EventQueue eventQueue, Config config, boolean createdByUser) {
        this.config = config;
        this.foodPool = foodPool;
        this.eventQueue = eventQueue;
        this.cellIndex = cellCounter.getAndIncrement();
        this.createdByUser = createdByUser;

        eventQueue.add(new Event(this, EventType.CELL_EATING, Instant.now()));
    }

    public abstract void reproduce();

    public abstract String getName();

    @Override
    public void run() {
        while (alive) {
            Event event;
            var actionPending = false;

            synchronized (eventQueue) {
                event = eventQueue.front();
                if (event != null && event.targetCell() == this &&
                        event.timestamp().isBefore(Instant.now())
                ) {
                    actionPending = true;
                    eventQueue.pop();
                }
            }

            if (actionPending) {
                performAction(event);
            }

            synchronized (eventQueue) {
                try {
                    eventQueue.wait();
                } catch (InterruptedException ignored) {}
            }
        }
    }

    private void performAction(Event event) {
        if (event.type() == EventType.CELL_EATING) {
            if (canEat()) {
                eat();
                globalState.consumeFood(1);

                eventQueue.add(new Event(
                        this,
                        EventType.CELL_EATING,
                        Instant.now().plusMillis(config.getTimeFull())
                ));

                if (canReproduce()) {
                    eventQueue.add(new Event(
                            this,
                            EventType.CELL_REPRODUCING,
                            Instant.now()
                    ));
                }
            }
            eventQueue.add(new Event(
                    this,
                    EventType.CELL_STARVING,
                    Instant.now().plusMillis(config.getTimeStarve())
            ));
        }

        else if (event.type() == EventType.CELL_STARVING) {
            if (isStarving()) {
                die();
                eventQueue.addDeadCell(this);
            }
        }

        else if (event.type() == EventType.CELL_REPRODUCING) {
            if (canReproduce()) {
                reproduce();
            }
        }
    }

    private boolean isStarving() {
        return Duration.between(lastMealTime, Instant.now()).toMillis() >= config.getTimeStarve();
    }

    private boolean canEat() {
        return (mealsEaten == 0 ||
                Duration.between(lastMealTime, Instant.now()).toMillis() > config.getTimeFull()
        ) && foodPool.getTotalFood() > 0;
    }

    private void eat() {
        foodPool.consumeFood(1);
        mealsEaten++;
        lastMealTime = Instant.now();
        System.out.println(getName() + " ate food. Total meals: " + mealsEaten);
    }

    private boolean canReproduce() {
        return mealsEaten >= config.getReproductionThreshold();
    }

    protected void die() {
        alive = false;
        eventQueue.addDeadCell(this);
        addFoodToPoolAfterDeath();
        System.out.println(getName() + " has died.");
    }

    private void addFoodToPoolAfterDeath() {
        Random rand = new Random();
        int foodToAdd = rand.nextInt(config.getFoodAmountAfterDeath()) + 1;
        foodPool.addFood(foodToAdd);
        System.out.printf("%d food has been added to the pool.%n", foodToAdd);
    }

    public void stopCell() {
        alive = false;
        System.out.println(getName() + " is stopping.");
    }
}
