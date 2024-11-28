package com.celluloid.cell;

import com.celluloid.*;

public class SexualCell extends Cell {
    private boolean isSeekingPartner = false;
    private Cupid cupid;
    private Config config;
    private final GlobalState globalState = GlobalState.getInstance();

    public SexualCell(FoodPool foodPool, Watcher watcher, Cupid cupid, Config config) {
        super(foodPool, watcher, config);
        this.cupid = cupid;
        this.config = config;
        //globalState.incrementSexualCellsAlive(4);
    }

    @Override
    public synchronized void reproduce() {
        if (!isSeekingPartner) {
            System.out.println(this.getName() + " is seeking a partner to reproduce.");
            isSeekingPartner = true;
            cupid.registerCell(this);
        }
    }

    @Override
    public String getName() {
        return "SexualCell_" + cellIndex;
    }

    public synchronized void makeChild(SexualCell partner) {
        if (partner != null) {
            System.out.println(this.getName() + " found a partner: " + partner.getName());
            SexualCell child = new SexualCell(foodPool, watcher, cupid, config);

            Thread thread = new Thread(child);

            thread.start();

            globalState.incrementSexualCellsAlive(1);
        }
    }
}
