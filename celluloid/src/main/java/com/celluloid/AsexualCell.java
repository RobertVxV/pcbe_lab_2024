package com.celluloid;

public class AsexualCell extends Cell {
    public AsexualCell(String name, FoodPool foodPool, Watcher watcher) {
        super(name, foodPool, watcher);
    }

    @Override
    public void reproduce() {
        System.out.println(this.getName() + " is reproducing asexually.");
        AsexualCell child1 = new AsexualCell(name, foodPool, watcher);
        AsexualCell child2 = new AsexualCell(name, foodPool, watcher);

        // start child threads
        Thread thread1 = new Thread(child1);
        Thread thread2 = new Thread(child2);
        thread1.start();
        thread2.start();

        this.die();
    }
}
