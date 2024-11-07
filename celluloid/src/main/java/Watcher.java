import java.util.Random;

class Watcher extends Thread {
    private final FoodPool foodPool;

    public Watcher(FoodPool foodPool) {
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

    public void notifyCellDeath(Cell cell) {
        System.out.println(cell.getName() + " has died and food has been added to the pool.");
        Random rand = new Random();
        int foodToAdd = rand.nextInt(5) + 1;
        foodPool.addFood(foodToAdd);
        System.out.println(cell.getName() + " added " + foodToAdd + " food to the pool.");
    }
}