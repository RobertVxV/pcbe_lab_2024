class GameOfLife {
    public static void main(String[] args) {
        int initialFood = 3;
        FoodPool foodPool = new FoodPool(initialFood);
        Watcher watcher = new Watcher(foodPool);
        watcher.start();
        Cupid cupid = new Cupid();
        cupid.start();

        // Create some initial cells
        AsexualCell asexualCell1 = new AsexualCell(foodPool, watcher);
        SexualCell sexualCell1 = new SexualCell(foodPool, watcher, cupid);
        SexualCell sexualCell2 = new SexualCell(foodPool, watcher, cupid);
        SexualCell sexualCell3 = new SexualCell(foodPool, watcher, cupid);

        // Start the simulation
        asexualCell1.start();
        sexualCell1.start();
        sexualCell2.start();
        sexualCell3.start();
    }
}
