package com.celluloid.cell;

import com.celluloid.CellRegister;
import com.celluloid.Config;
import com.celluloid.FoodPool;
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
    protected final CellRegister cellRegister;

    protected boolean alive = true;
    protected final int cellIndex;
    protected int mealsEaten = 0;
    private boolean waitingToEat = false;
    protected Instant lastMealTime = Instant.now();

    public Cell(
            FoodPool foodPool,
            EventQueue eventQueue,
            Config config,
            CellRegister cellRegister
    ) {
        this.config = config;
        this.foodPool = foodPool;
        this.eventQueue = eventQueue;
        this.cellRegister = cellRegister;
        this.cellIndex = cellCounter.getAndIncrement();

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
        if (event.type() == EventType.CELL_EATING || waitingToEat) {
            synchronized (foodPool) {
                if (canEat()) {
                    eat();
                    eventQueue.add(new Event(
                            this,
                            EventType.CELL_EATING,
                            Instant.now().plusMillis(
                                    config.getTimeFull() +
                                            (int) (Math.random() * config.getTimeFullVariance()))
                    ));

                    if (canReproduce()) {
                        eventQueue.add(new Event(
                                this,
                                EventType.CELL_REPRODUCING,
                                Instant.now()
                        ));
                    }
                }
                else if (!isFoodAvailable() && isTimeToEat()) {
                    waitingToEat = true;
                }

            }
            eventQueue.add(new Event(
                    this,
                    EventType.CELL_STARVING,
                    Instant.now().plusMillis(config.getTimeStarve())
            ));
        }

        if (event.type() == EventType.CELL_STARVING) {
            if (isStarving()) {
                die();
                cellRegister.registerDeadCell(this);
            }
        }

        if (event.type() == EventType.CELL_REPRODUCING) {
            if (canReproduce()) {
                reproduce();
            }
        }
    }

    private boolean isStarving() {
        return Duration.between(lastMealTime, Instant.now()).toMillis() >= config.getTimeStarve();
    }

    private boolean isTimeToEat() {
        return mealsEaten == 0 ||
                Duration.between(lastMealTime, Instant.now()).toMillis() > config.getTimeFull();
    }

    private boolean isFoodAvailable() {
        return foodPool.getTotalFood() > 0;
    }

    private boolean canEat() {
        return isTimeToEat() && isFoodAvailable();
    }

    private void eat() {
        foodPool.consumeFood(1);

        mealsEaten++;
        waitingToEat = false;
        lastMealTime = Instant.now();

        System.out.println(getName() + " ate food. Total meals: " + mealsEaten);
    }

    private boolean canReproduce() {
        return mealsEaten >= config.getReproductionThreshold();
    }

    protected void die() {
        alive = false;
        cellRegister.registerDeadCell(this);
        addFoodToPoolAfterDeath();

        synchronized (eventQueue) {
            eventQueue.notifyAll();
        }

        System.out.println(getName() + " has died.");
    }

    private void addFoodToPoolAfterDeath() {
        Random rand = new Random();
        int foodToAdd = rand.nextInt(config.getFoodAmountAfterDeath()) + 1;
        foodPool.addFood(foodToAdd);
        System.out.printf("%d food has been added to the pool.%n", foodToAdd);
    }

    public void endThread() {
        alive = false;
        System.out.println(getName() + " is stopping.");
    }
}
