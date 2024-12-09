package com.celluloid.cell;

import com.celluloid.Config;
import com.celluloid.FoodPool;
import com.celluloid.GlobalGameStats;
import com.celluloid.event.EventQueue;

public class AsexualCell extends Cell {
    private final GlobalGameStats globalState = GlobalGameStats.getInstance();

    public AsexualCell(FoodPool foodPool, EventQueue eventQueue, Config config, boolean createdByUser) {
        super(foodPool, eventQueue, config,createdByUser);
    }

    @Override
    public void reproduce() {
        System.out.println(getName() + " is reproducing asexually.");

        AsexualCell child1 = new AsexualCell(foodPool, eventQueue, config, false);
        AsexualCell child2 = new AsexualCell(foodPool, eventQueue, config, false);

        Thread thread1 = new Thread(child1);
        Thread thread2 = new Thread(child2);
        thread1.start();
        thread2.start();

        globalState.incrementAsexualCellsAlive(1);

        die();
    }

    @Override
    public String getName() {
        return (this.createdByUser) ? "AsexualCell_CREATED_BY_USER_" + cellIndex : "AsexualCell_" + cellIndex;
    }
}
