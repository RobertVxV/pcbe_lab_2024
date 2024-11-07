class AsexualCell extends Cell {
    public AsexualCell(FoodPool foodPool, Watcher watcher) {
        super(foodPool, watcher);
    }

    @Override
    public void reproduce() {
        System.out.println(this.getName() + " is reproducing asexually.");
        AsexualCell child1 = new AsexualCell(foodPool, watcher);
        AsexualCell child2 = new AsexualCell(foodPool, watcher);
        child1.start();
        child2.start();
        this.die();
    }
}
