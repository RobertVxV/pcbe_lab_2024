package com.celluloid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SexualCell extends Cell {
    private boolean isSeekingPartner = false;

    @Autowired
    private Cupid cupid;

    // Modify the constructor to accept Config as the third argument
    public SexualCell(FoodPool foodPool, Watcher watcher, Config config, Cupid cupid) {
        super(foodPool, watcher, config);  // Pass the config to the superclass constructor
        this.cupid = cupid;
    }

    @Override
    public synchronized void reproduce() {
        if (!isSeekingPartner) {
            System.out.println(this.getName() + " is seeking a partner to reproduce.");
            isSeekingPartner = true;
            cupid.registerCell(this);
        }
    }

    public synchronized void makeChild(SexualCell partner) {
        if (partner != null) {
            System.out.println(this.getName() + " found a partner: " + partner.getName());
            SexualCell child = new SexualCell(foodPool, watcher, config, cupid);  // Pass config to the child cell
            child.start();
        }
    }
}
