package com.celluloid;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class Cell extends Thread {
    protected final FoodPool foodPool;
    protected final Watcher watcher;
    protected final Config config;

    protected int mealsEaten = 0;
    protected boolean alive = true;

    @Autowired
    public Cell(FoodPool foodPool, Watcher watcher, Config config) {
        this.foodPool = foodPool;
        this.watcher = watcher;
        this.config = config;
    }

    public abstract void reproduce();

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
    }

    private boolean attemptToEat() {
        long startWait = System.currentTimeMillis();
        boolean ateFood = false;

        while (System.currentTimeMillis() - startWait < config.T_STARVE && !ateFood) {
            int food = foodPool.consumeFood(1);
            if (food > 0) {
                mealsEaten++;
                ateFood = true;
                System.out.println(this.getName() + " ate food. Total meals: " + mealsEaten);
                if (mealsEaten >= config.REPRODUCTION_THRESHOLD) {
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
        int fullTime = config.T_FULL + (int) (Math.random() * config.T_FULL_VARIANCE);
        try {
            Thread.sleep(fullTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected void die() {
        alive = false;
        System.out.println(this.getName() + " has died.");
    }
}
