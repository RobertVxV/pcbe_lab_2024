package com.celluloid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class SexualCell extends Cell {
    private boolean isSeekingPartner = false;

    private Watcher watcher;

    // Modify the constructor to accept Config as the third argument
    public SexualCell(String name, FoodPool foodPool, Watcher watcher) {
        super(name, foodPool, watcher);  // Pass the config to the superclass constructor
        this.watcher = watcher;
    }

    @Override
    public synchronized void reproduce() {
        if (!isSeekingPartner) {
            System.out.println(this.getName() + " is seeking a partner to reproduce.");
            isSeekingPartner = true;
            watcher.registerCell(this);
        }
    }

    public synchronized void makeChild(SexualCell partner) {
        if (partner != null) {
            System.out.println(this.getName() + " found a partner: " + partner.getName());
            SexualCell child = new SexualCell(this.name + "-" + partner.name, foodPool, watcher);  // Pass config to the child cell
            Thread thread = new Thread(child);
            thread.start();
        }
    }
}
