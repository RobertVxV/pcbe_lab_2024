package com.celluloid.cell;

import com.celluloid.Config;
import com.celluloid.FoodPool;
import com.celluloid.Watcher;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Cell implements Runnable {
    private static final AtomicInteger cellCounter = new AtomicInteger(0);

    protected final FoodPool foodPool;
    protected final Watcher watcher;
    private final Config config;

    protected final int cellIndex;
    protected int mealsEaten = 0;
    protected boolean alive = true;
    protected Instant lastMealTime = Instant.now();

    public Cell(FoodPool foodPool, Watcher watcher, Config config) {
        this.config = config;
        this.foodPool = foodPool;
        this.watcher = watcher;
        this.cellIndex = cellCounter.getAndIncrement();

        watcher.addCellToQueue(this, Instant.now());
    }

    public abstract void reproduce();
    public abstract String getName();

    @Override
    public void run() {
        while (alive) {
            if (isStarving()) {
                die();
            }
            synchronized (foodPool) {
                if (canEat()) {
                    eat();
                }
            }
            if (canReproduce()) {
                reproduce();
            }

            synchronized (this) {
                watcher.addCellToQueue(this, Instant.now().plusMillis(config.getTFull()));
                try {
                    wait();
                } catch (InterruptedException ignored) {}
            }
        }
        System.out.println(this.getName() + " has closed.");
    }

    private boolean isStarving() {
        return Duration.between(Instant.now(), lastMealTime).toMillis() > config.getTStarve();
    }

    private boolean canEat() {
        return foodPool.getTotalFood() > 0;
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
        watcher.notifyCellDeath(this);
        System.out.println(getName() + " has died.");
    }
}
