import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

class FoodPool {
    private int totalFood;
    private final Semaphore semaphore;

    public FoodPool(int initialFood) {
        this.totalFood = initialFood;
        this.semaphore = new Semaphore(1);
    }

    public int consumeFood(int amount) {
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

    public void addFood(int amount) {
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

abstract class Cell extends Thread {
    protected static final int T_FULL = 4000; // Time to consume food
    protected static final int T_STARVE = 2000; // Time to starve
    protected static final int T_STARVE_VARIANCE = 1000; // Variance in starving time
    protected static final int REPRODUCTION_THRESHOLD = 10; // Meals before reproduction

    protected FoodPool foodPool;
    protected int mealsEaten = 0;
    protected boolean alive = true;
    protected boolean hungry = true;
    protected static final Random rand = new Random();

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
                    int starvationTime = T_STARVE + rand.nextInt(T_STARVE_VARIANCE);
                    try {
                        Thread.sleep(starvationTime);
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
            mealsEaten = 0;
        }

        hungry = true;
    }

    protected void die() {
        alive = false;
        System.out.println(this.getName() + " has died.");
        int foodToAdd = rand.nextInt(5) + 1;
        foodPool.addFood(foodToAdd);
        System.out.println(this.getName() + " died and added " + foodToAdd + " food to the pool.");
    }
}

class AsexualCell extends Cell {
    public AsexualCell(FoodPool foodPool) {
        super(foodPool);
    }

    @Override
    public void reproduce() {
        System.out.println(this.getName() + " is reproducing asexually.");
        AsexualCell child1 = new AsexualCell(foodPool);
        AsexualCell child2 = new AsexualCell(foodPool);
        child1.start();
        child2.start();
    }
}

class SexualCell extends Cell {
    private boolean hasPartner = false;
    private static final List<SexualCell> waitingCells = new ArrayList<>();

    public SexualCell(FoodPool foodPool) {
        super(foodPool);
    }

    @Override
    public synchronized void reproduce() {
        // Add this cell to the waiting list
        waitingCells.add(this);

        // Check for partners
        if (waitingCells.size() > 1) {
            // Attempt to find a partner
            for (SexualCell waitingCell : waitingCells) {
                if (waitingCell != this && !waitingCell.hasPartner) {
                    // Found a partner
                    this.hasPartner = true;
                    waitingCell.hasPartner = true;

                    // Remove both cells from waiting list
                    waitingCells.remove(waitingCell);
                    waitingCells.remove(this);

                    System.out.println(this.getName() + " is reproducing with " + waitingCell.getName());

                    // Create new cells from both parents
                    SexualCell child1 = new SexualCell(foodPool);
                    SexualCell child2 = new SexualCell(foodPool);

                    child1.start();
                    child2.start();

                    break; // Exit after finding a partner
                }
            }
        } else {
            // If no partner is found, stay in the waiting list
            System.out.println(this.getName() + " is waiting for a partner to reproduce.");
        }
    }
}

public class GameOfLife {
    private static final int maxCycles = 100;
    private static int cycleCount = 0;

    public static void main(String[] args) {
        FoodPool foodPool = new FoodPool(10);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int foodAmount = (int) (Math.random() * 5) + 1;
                foodPool.addFood(foodAmount);
                System.out.println("Added " + foodAmount + " food to the pool.");
            }
        }, 0, 5000);

        SexualCell cell1 = new SexualCell(foodPool);
        SexualCell cell2 = new SexualCell(foodPool);
        AsexualCell cell3 = new AsexualCell(foodPool);

        cell1.start();
        cell2.start();
        cell3.start();

        // Run the simulation for a specified number of cycles
        while (cycleCount < maxCycles) {
            try {
                Thread.sleep(1000);
                cycleCount++;
                System.out.println("Cycle count: " + cycleCount);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }


        System.out.println("Simulation ended after " + maxCycles + " cycles.");


        try {
            cell1.join();
            cell2.join();
            cell3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        timer.cancel();
        System.out.println("Timer cancelled. Exiting program.");
    }
}
