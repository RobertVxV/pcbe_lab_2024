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
