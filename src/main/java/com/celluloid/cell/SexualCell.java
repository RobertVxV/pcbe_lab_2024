package com.celluloid.cell;

import com.celluloid.*;

public class SexualCell extends Cell {
    private boolean isSeekingPartner = false;
    private Cupid cupid;
    private Config config;
    private final GlobalGameStats globalState = GlobalGameStats.getInstance();
    private boolean created_by_user;

    public SexualCell(FoodPool foodPool, Watcher watcher, Cupid cupid, Config config, boolean created_by_user) {
        super(foodPool, watcher, config);
        this.cupid = cupid;
        this.config = config;
        this.created_by_user = created_by_user;
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
        return (created_by_user) ? "SexualCell_CREATED_BY_USER_" + cellIndex : "SexualCell_" + cellIndex;
    }

    public synchronized void makeChild(SexualCell partner) {
        if (partner != null) {
            System.out.println(this.getName() + " found a partner: " + partner.getName());
            SexualCell child = new SexualCell(foodPool, watcher, cupid, config, false);

            Thread thread = new Thread(child);

            thread.start();

            globalState.incrementSexualCellsAlive(1);
        }
    }
}
