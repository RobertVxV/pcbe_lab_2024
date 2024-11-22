package com.celluloid.cell;

import com.celluloid.FoodPool;
import com.celluloid.Watcher;

public class AsexualCell extends Cell {
    public AsexualCell(FoodPool foodPool, Watcher watcher) {
        super(foodPool, watcher);
    }

    @Override
    public void reproduce() {
        System.out.println(this.getName() + " is reproducing asexually.");
        AsexualCell child1 = new AsexualCell(foodPool, watcher);
        AsexualCell child2 = new AsexualCell(foodPool, watcher);

        // start child threads
        Thread thread1 = new Thread(child1);
        Thread thread2 = new Thread(child2);
        thread1.start();
        thread2.start();

        this.die();
    }

    @Override
    public String getName() {
        return "AsexualCell_" + cellIndex;
    }
}
