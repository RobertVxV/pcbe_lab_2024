package com.celluloid.cell;

import com.celluloid.FoodPool;
import com.celluloid.Watcher;

public class SexualCell extends Cell {
    private boolean isSeekingPartner = false;

    public SexualCell(FoodPool foodPool, Watcher watcher) {
        super(foodPool, watcher);
    }

    @Override
    public synchronized void reproduce() {
        if (!isSeekingPartner) {
            System.out.println(this.getName() + " is seeking a partner to reproduce.");
            isSeekingPartner = true;
            watcher.registerCell(this);
        }
    }

    @Override
    public String getName() {
        return "SexualCell_" + cellIndex;
    }

    public synchronized void makeChild(SexualCell partner) {
        if (partner != null) {
            System.out.println(this.getName() + " found a partner: " + partner.getName());
            SexualCell child = new SexualCell(foodPool, watcher);

            Thread thread = new Thread(child);
            thread.start();
        }
    }
}
