package com.celluloid;

public abstract class Cell implements Runnable {
    protected final FoodPool foodPool;
    protected final Watcher watcher;
    protected final String name;

    protected int mealsEaten = 0;
    protected boolean alive = true;

    public Cell(String name, FoodPool foodPool, Watcher watcher) {
        this.foodPool = foodPool;
        this.watcher = watcher;
        this.name = name;
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

    public String getName() {
        return name;
    }
}
