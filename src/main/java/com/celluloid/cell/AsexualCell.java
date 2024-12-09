package com.celluloid.cell;

import com.celluloid.CellRegister;
import com.celluloid.Config;
import com.celluloid.FoodPool;
import com.celluloid.event.EventQueue;

public class AsexualCell extends Cell {

    public AsexualCell(
            FoodPool foodPool,
            EventQueue eventQueue,
            Config config,
            CellRegister cellRegister,
            boolean createdByUser) {
        super(foodPool, eventQueue, config, cellRegister, createdByUser);
    }

    @Override
    public void reproduce() {
        System.out.println(getName() + " is reproducing asexually.");
        for (int i = 0; i < 2; i++) {
            cellRegister.addAsexualCell();
        }
        die();
    }

    @Override
    public String getName() {
        return "AsexualCell_" + cellIndex;
    }
}
