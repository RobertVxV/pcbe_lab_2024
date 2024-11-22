package com.celluloid.cell;

import com.celluloid.Config;
import com.celluloid.FoodPool;
import com.celluloid.Watcher;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Cell implements Runnable {
    protected final FoodPool foodPool;
    protected final Watcher watcher;
    protected final int cellIndex;

    protected int mealsEaten = 0;
    protected boolean alive = true;

    private static final AtomicInteger cellCounter = new AtomicInteger(0);

    public Cell(FoodPool foodPool, Watcher watcher) {
        this.foodPool = foodPool;
        this.watcher = watcher;
        this.cellIndex = cellCounter.getAndIncrement();
    }

    public abstract void reproduce();
    abstract public String getName();

    @Override
    public void run() {
        while (alive) {
            if (attemptToEat()) {
                remainFull();
            } else {
                this.die();
                watcher.notifyCellDeath(this);
            }
        }

        System.out.println(this.getName() + " has closed.");
    }

    private boolean attemptToEat() {
        long startWait = System.currentTimeMillis();
        boolean ateFood = false;
        while (System.currentTimeMillis() - startWait < Config.T_STARVE && !ateFood) {
            int food = foodPool.consumeFood(1);
            if (food > 0) {
                mealsEaten++;
                ateFood = true;
                System.out.println(getName() + " ate food. Total meals: " + mealsEaten);
                if (mealsEaten >= Config.REPRODUCTION_THRESHOLD) {
                    reproduce();
                }
            } else {
                try {
                    synchronized (watcher) {
                        watcher.wait(100);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return ateFood;
    }

    private void remainFull() {
        int fullTime = Config.T_FULL + (int) (Math.random() * Config.T_FULL_VARIANCE);
        try {
            Thread.sleep(fullTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected void die() {
        alive = false;
        System.out.println(getName() + " has died.");
    }
}
