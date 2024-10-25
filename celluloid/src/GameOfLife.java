import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class FoodPool {
    private int totalFood;

    public FoodPool(int initialFood) {
        this.totalFood = initialFood;
    }

    public synchronized int consumeFood(int amount) { // use semaphores
        if (totalFood >= amount) {
            totalFood -= amount;
            return amount;
        } else {
            int availableFood = totalFood;
            totalFood = 0;
            return availableFood;
        }
    }

    public synchronized void addFood(int amount) {
        totalFood += amount;
    } //use semaphores

    public synchronized int getTotalFood() {
        return totalFood;
    }
}

abstract class Cell extends Thread {
    /*
    these info should be gathered from a config file
    */
    protected static final int T_FULL = 5000;
    protected static final int T_STARVE = 3000;
    protected static final int REPRODUCTION_THRESHOLD = 10;

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
                    try {
                        Thread.sleep(T_STARVE);
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
            Thread.sleep(T_FULL);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (mealsEaten >= REPRODUCTION_THRESHOLD) {
            reproduce();
        }

        hungry = true;
    }

    protected void die() {
        alive = false; //add an event that ends the tread
        System.out.println(this.getName() + " has died.");
        Random rand = new Random();
        int foodToAdd = rand.nextInt(5) + 1;
        foodPool.addFood(foodToAdd);
        System.out.println(this.getName() + " died and added " + foodToAdd + " food to the pool.");
    }

    protected void kill()
    {
        alive = false; //add an event that ends  the thread
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

public class GameOfLife {
    public static void main(String[] args) {
        int initialFood = 100;
        FoodPool foodPool = new FoodPool(initialFood);

        AsexualCell asexualCell1 = new AsexualCell(foodPool);
        SexualCell sexualCell1 = new SexualCell(foodPool);
        SexualCell sexualCell2 = new SexualCell(foodPool);

        asexualCell1.start();
        sexualCell1.start();
        sexualCell2.start();

        sexualCell1.findPartner(sexualCell2);
    }
}