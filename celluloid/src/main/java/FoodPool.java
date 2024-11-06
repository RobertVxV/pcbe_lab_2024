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
