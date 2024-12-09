package com.celluloid.cell;

import com.celluloid.Config;
import com.celluloid.FoodPool;
import com.celluloid.event.Event;
import com.celluloid.event.EventQueue;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Cell implements Runnable {
    private static final AtomicInteger cellCounter = new AtomicInteger(0);

    private final Config config;
    protected final EventQueue eventQueue;
    protected final FoodPool foodPool;

    protected final int cellIndex;
    protected int mealsEaten = 0;
    protected boolean alive = true;
    protected Instant lastMealTime = Instant.now();

    public Cell(FoodPool foodPool, EventQueue eventQueue, Config config) {
        this.config = config;
        this.foodPool = foodPool;
        this.eventQueue = eventQueue;
        this.cellIndex = cellCounter.getAndIncrement();

        eventQueue.add(new Event(this, Instant.now()));
    }

    public abstract void reproduce();

    public abstract String getName();

    @Override
    public void run() {
        while (alive) {
            boolean actionPending = false;
            synchronized (eventQueue) {
                var event = eventQueue.front();
                if (event != null) {
                    if (event.cell() == this && event.timestamp().isBefore(Instant.now())) {
                        eventQueue.pop();
                        actionPending = true;
                    }
                }
            }
            if (actionPending) {
                performAction();
            }

            synchronized (eventQueue) {
                try {
                    eventQueue.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void performAction() {
        if (isStarving()) {
            die();
        }
        synchronized (foodPool) {
            if (canEat()) {
                eat();
                eventQueue.add(new Event(this, Instant.now().plusMillis(config.getTFull())));
            }
            eventQueue.add(new Event(this, Instant.now().plusMillis(config.getTStarve())));
        }
        if (canReproduce()) {
            reproduce();
        }
    }

    private boolean isStarving() {
        return Duration.between(Instant.now(), lastMealTime).toMillis() >= config.getTStarve();
    }

    private boolean canEat() {
        return Duration.between(lastMealTime, Instant.now()).toMillis() >= config.getTFull() &&
                foodPool.getTotalFood() > 0;
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
        int foodToAdd = rand.nextInt(5) + 1;
        foodPool.addFood(foodToAdd);
        System.out.printf("%s has died and %d food has been added to the pool.%n",
                getName(), foodToAdd);
    }
}
