import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

class FoodPool { // the place where cells live, and the place where the food is
    private int totalFood;
    private final Semaphore semaphore;

    public FoodPool(int initialFood) {
        this.totalFood = initialFood;
        this.semaphore = new Semaphore(1); // using semaphores
    }

    public int consumeFood(int amount) { // one cell at a time consumes food from the pool
        int foodConsumed = 0;
        try {
            semaphore.acquire();
            if (totalFood >= amount) {
                foodConsumed = amount;
                totalFood -= amount;
            } else {
                foodConsumed = totalFood;
                totalFood = 0;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }

        return foodConsumed;
    }

    public void addFood(int amount) { // one cell at the time adds food to the pool
        try {
            semaphore.acquire();
            totalFood += amount;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }

    public int getTotalFood() {
        int food;
        try {
            semaphore.acquire();
            food = totalFood;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            food = 0;
        } finally {
            semaphore.release();
        }
        return food;
    }
}

class Watcher extends Thread {
    private final FoodPool foodPool;

    public Watcher(FoodPool foodPool) { // a watcher, "God" is added to the pool
        this.foodPool = foodPool;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(50); // short interval to check food status
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (this) {
                if (foodPool.getTotalFood() > 0) {
                    notifyAll(); // notify cells if food is available
                }
            }
        }
    }
}

abstract class Cell extends Thread {
    protected static final int T_FULL = 3000;
    protected static final int T_STARVE = 2000;
    protected static final int T_FULL_VARIANCE = 1000;
    protected static final int REPRODUCTION_THRESHOLD = 10;

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


class AsexualCell extends Cell {
    public AsexualCell(FoodPool foodPool, Watcher watcher) {
        super(foodPool, watcher);
    }

    @Override
    public void reproduce() {
        System.out.println(this.getName() + " is reproducing asexually.");
        this.kill();
        AsexualCell child1 = new AsexualCell(foodPool, watcher);
        AsexualCell child2 = new AsexualCell(foodPool, watcher);
        child1.start();
        child2.start();
    }
}

class SexualCell extends Cell {
    private List<SexualCell> partners = new ArrayList<>();

    public SexualCell(FoodPool foodPool, Watcher watcher) {
        super(foodPool, watcher);
    }

    public void findPartner(SexualCell partner) {
        synchronized (this) {
            partners.add(partner);
            notifyAll();
        }
    }

    @Override
    public synchronized void reproduce() {
        if (partners.isEmpty()) {
            System.out.println(this.getName() + " is waiting for a partner to reproduce.");
            try {
                wait(); // Wait for a partner
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            SexualCell partner = partners.remove(0);
            System.out.println(this.getName() + " is reproducing with " + partner.getName());
            SexualCell child = new SexualCell(foodPool, watcher);
            child.start();
        }
    }
}

public class GameOfLife {
    public static void main(String[] args) {
        int initialFood = 100;
        FoodPool foodPool = new FoodPool(initialFood);
        Watcher watcher = new Watcher(foodPool);
        watcher.start();

        // Create some initial cells
        AsexualCell asexualCell1 = new AsexualCell(foodPool, watcher);
        SexualCell sexualCell1 = new SexualCell(foodPool, watcher);
        SexualCell sexualCell2 = new SexualCell(foodPool, watcher);

        // Start the simulation
        asexualCell1.start();
        sexualCell1.start();
        sexualCell2.start();

        // Allow sexual cells to find partners
        sexualCell1.findPartner(sexualCell2);
    }
}
