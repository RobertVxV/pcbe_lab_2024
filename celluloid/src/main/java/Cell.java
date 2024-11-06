import java.util.Random;

abstract class Cell extends Thread {
    protected static final Config config = ConfigLoader.getConfig();

    protected static final int T_FULL = config.T_FULL;
    protected static final int T_STARVE = config.T_STARVE;
    protected static final int T_FULL_VARIANCE = config.T_FULL_VARIANCE;
    protected static final int REPRODUCTION_THRESHOLD = config.REPRODUCTION_THRESHOLD;

    protected FoodPool foodPool;
    protected int mealsEaten = 0;
    protected boolean alive = true;
    protected final Watcher watcher;

    public Cell(FoodPool foodPool, Watcher watcher) { // a cell its added to the food pool and it has its watcher
        this.foodPool = foodPool;
        this.watcher = watcher;
    }

    public abstract void reproduce();

    @Override
    public void run() {
        while (alive) {
            if (attemptToEat()) {
                remainFull(); // if a cell eats, it stays full
            } else {
                die(); // if not the cell dies
            }
        }
    }

    private boolean attemptToEat() {
        long startWait = System.currentTimeMillis();
        boolean ateFood = false;

        while (System.currentTimeMillis() - startWait < T_STARVE && !ateFood) { // loop until cell either starves or eats
            // eating logic
            int food = foodPool.consumeFood(1);
            if (food > 0) {
                mealsEaten++;
                ateFood = true;
                System.out.println(this.getName() + " ate food. Total meals: " + mealsEaten);
                if (mealsEaten >= REPRODUCTION_THRESHOLD) {
                    reproduce();
                }
            } else {
                // wait on watcher to notify
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
        Random rand = new Random();
        int fullTime = T_FULL + rand.nextInt(T_FULL_VARIANCE);
        try {
            Thread.sleep(fullTime); // cell remain full and does nothing
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected void die() {
        alive = false;
        System.out.println(this.getName() + " has died.");
        Random rand = new Random();
        int foodToAdd = rand.nextInt(5) + 1;
        foodPool.addFood(foodToAdd);
        System.out.println(this.getName() + " died and added " + foodToAdd + " food to the pool.");
    }

    protected void kill() {
        alive = false;
    }
}
