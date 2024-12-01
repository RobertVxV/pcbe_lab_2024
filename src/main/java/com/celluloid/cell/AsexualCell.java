package com.celluloid.cell;

import com.celluloid.Config;
import com.celluloid.FoodPool;
import com.celluloid.GlobalGameStats;
import com.celluloid.Watcher;

public class AsexualCell extends Cell {
    private Config config;
    private boolean created_by_user;
    private final GlobalGameStats globalState = GlobalGameStats.getInstance();
    public AsexualCell(FoodPool foodPool, Watcher watcher, Config config, boolean created_by_user) {
        super(foodPool, watcher, config);
        this.config = config;
        this.created_by_user = created_by_user;
       // globalState.incrementAsexualCellsAlive(3);
    }

    @Override
    public void reproduce() {
        System.out.println(this.getName() + " is reproducing asexually.");
        AsexualCell child1 = new AsexualCell(foodPool, watcher, config, false);
        AsexualCell child2 = new AsexualCell(foodPool, watcher, config, false);


        // start child threads
        Thread thread1 = new Thread(child1);
        Thread thread2 = new Thread(child2);
        thread1.start();
        thread2.start();

        globalState.incrementAsexualCellsAlive(1);

        this.die();
    }

    @Override
    public String getName() {
        return (this.created_by_user) ? "AsexualCell_CREATED_BY_USER_" + cellIndex : "AsexualCell_" + cellIndex;
    }
}
