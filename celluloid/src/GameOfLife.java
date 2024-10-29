import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

class FoodPool {
    private int totalFood;
    private final Semaphore semaphore;

    public FoodPool(int initialFood) {
        this.totalFood = initialFood;
        this.semaphore = new Semaphore(1);
    }

    public int consumeFood(int amount) { // semafoare
        int foodConsumed = 0;
        try
        {
            semaphore.acquire();
            if (totalFood >= amount) {
                foodConsumed = amount;
                totalFood -= amount;
            } else {
                foodConsumed = totalFood;
                totalFood = 0;
            }
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
        finally
        {
            semaphore.release();
        }

        return foodConsumed;
    }

    public void addFood(int amount) {
        try
        {
            semaphore.acquire();
            totalFood += amount;
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
        finally
        {
            semaphore.release();
        }
    }

    public synchronized int getTotalFood() {
        int food;
        try
        {
            semaphore.acquire();
            food = totalFood;
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            food = 0;
        }
        finally
        {
            semaphore.release();
        }
        return food;
    }
}

abstract class Cell extends Thread {
    protected static final int T_FULL = 4000; // Time a cell stays full
    protected static final int T_STARVE = 2000; // Time a cell can starve
    protected static final int T_STARVE_VARIANCE = 1000;
    protected static final int REPRODUCTION_THRESHOLD = 10; // store in json

    protected FoodPool foodPool;
    protected int mealsEaten = 0;
    protected boolean alive = true;
    protected boolean hungry = true;

    public Cell(FoodPool foodPool) {
        this.foodPool = foodPool;
    }

    public abstract void reproduce();

    @Override
    public void run() {
        while (alive) {
            if (hungry) {
                int food = foodPool.consumeFood(1);
                if (food > 0) {
                    eat(food);
                } else {
                    Random rand = new Random();
                    int starvationTime = T_STARVE + rand.nextInt(T_STARVE_VARIANCE);
                    try {
                        Thread.sleep(starvationTime); // Starvation time
                        if (hungry) {
                            die();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    private void eat(int food) {
        mealsEaten++;
        hungry = false;
        System.out.println(this.getName() + " ate food. Total meals: " + mealsEaten);

        try {
            Thread.sleep(T_FULL); // Stay full for some time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (mealsEaten >= REPRODUCTION_THRESHOLD) {
            reproduce();
        }

        hungry = true; // Become hungry again
    }

    protected void die() {
        alive = false;
        System.out.println(this.getName() + " has died.");
        Random rand = new Random();
        int foodToAdd = rand.nextInt(5) + 1;
        foodPool.addFood(foodToAdd);
        System.out.println(this.getName() + " died and added " + foodToAdd + " food to the pool.");
    }

    protected void kill() // add an event that ends the thread
    {
        alive = false;
    }
}

class AsexualCell extends Cell {
    public AsexualCell(FoodPool foodPool) {
        super(foodPool);
    }

    @Override
    public void reproduce() {
        System.out.println(this.getName() + " is reproducing asexually.");
        this.kill();
        AsexualCell child1 = new AsexualCell(foodPool);
        AsexualCell child2 = new AsexualCell(foodPool);
        child1.start();
        child2.start();
    }
}

class SexualCell extends Cell {
    private List<SexualCell> partners = new ArrayList<>();

    public SexualCell(FoodPool foodPool) {
        super(foodPool);
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
            SexualCell child = new SexualCell(foodPool);
            child.start();
        }
    }
}

public class Celluloid {
    public static void main(String[] args) {
        int initialFood = 3;
        FoodPool foodPool = new FoodPool(initialFood);

        // Create some initial cells
        AsexualCell asexualCell1 = new AsexualCell(foodPool);
        SexualCell sexualCell1 = new SexualCell(foodPool);
        SexualCell sexualCell2 = new SexualCell(foodPool);

        // Start the simulation
        asexualCell1.start();
        sexualCell1.start();
        sexualCell2.start();

        // Allow sexual cells to find partners
        sexualCell1.findPartner(sexualCell2);
    }
}
