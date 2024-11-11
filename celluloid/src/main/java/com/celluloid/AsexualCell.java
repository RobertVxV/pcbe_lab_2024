package com.celluloid;

import org.springframework.stereotype.Component;

@Component
public class AsexualCell extends Cell {
    public AsexualCell(FoodPool foodPool, Watcher watcher, Config config) {
        super(foodPool, watcher, config);  // Pass the config to the superclass constructor
    }

    @Override
    public void reproduce() {
        System.out.println(this.getName() + " is reproducing asexually.");
        AsexualCell child1 = new AsexualCell(foodPool, watcher, config);  // Pass config to child cells
        AsexualCell child2 = new AsexualCell(foodPool, watcher, config);  // Pass config to child cells
        child1.start();
        child2.start();
        this.die();
    }
}
