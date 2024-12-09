package com.celluloid.cell;

import com.celluloid.Config;
import com.celluloid.FoodPool;
import com.celluloid.event.EventQueue;

public class AsexualCell extends Cell {
    Config config;
    public AsexualCell(FoodPool foodPool, EventQueue eventQueue, Config config) {
        super(foodPool, eventQueue, config);
        this.config = config;
    }

    @Override
    public void reproduce() {
        System.out.println(this.getName() + " is reproducing asexually.");
        AsexualCell child1 = new AsexualCell(foodPool, eventQueue, config);
        AsexualCell child2 = new AsexualCell(foodPool, eventQueue, config);

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
