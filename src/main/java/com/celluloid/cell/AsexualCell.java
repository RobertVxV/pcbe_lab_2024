package com.celluloid.cell;

import com.celluloid.Config;
import com.celluloid.FoodPool;
import com.celluloid.GlobalState;
import com.celluloid.event.EventQueue;

public class AsexualCell extends Cell {
    private final GlobalState globalState = GlobalState.getInstance();

    public AsexualCell(FoodPool foodPool, EventQueue eventQueue, Config config) {
        super(foodPool, eventQueue, config);
    }

    @Override
    public void reproduce() {
        System.out.println(getName() + " is reproducing asexually.");

        AsexualCell child1 = new AsexualCell(foodPool, eventQueue, config);
        AsexualCell child2 = new AsexualCell(foodPool, eventQueue, config);

        Thread thread1 = new Thread(child1);
        Thread thread2 = new Thread(child2);
        thread1.start();
        thread2.start();

        globalState.incrementAsexualCellsAlive(1);

        die();
    }

    @Override
    public String getName() {
        return "AsexualCell_" + cellIndex;
    }
}
