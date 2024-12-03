package com.celluloid;

import com.celluloid.cell.AsexualCell;
import com.celluloid.cell.SexualCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GameOfLife {
    private final GlobalGameStats globalState = GlobalGameStats.getInstance();
    private final Watcher watcher;
    private FoodPool foodPool;
    private final Cupid cupid = new Cupid();

    private final ArrayList<SexualCell> sexualCells = new ArrayList<>();
    private final ArrayList<AsexualCell> asexualCells = new ArrayList<>();

    private final Config config;

    @Autowired
    public GameOfLife(Config config) {
        this.config = config;
        FoodPool foodPool = new FoodPool(config.getStartFood());
        watcher = new Watcher(foodPool);

        for (int i = 0; i <  config.getSexualCellsCount(); i++) {
            sexualCells.add(new SexualCell(foodPool, watcher, cupid, config, false));
        }

        for (int i = 0; i < config.getAsexualCellsCount(); i++) {
            asexualCells.add(new AsexualCell(foodPool, watcher, config, false));
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startSimulation() {
        watcher.start();
        cupid.start();

        for (var cell : sexualCells) {
            Thread thread = new Thread(cell);
            thread.start();
        }

        for (var cell : asexualCells) {
            Thread thread = new Thread(cell);
            thread.start();
        }
    }

    public void stopSimulation() {
        watcher.interrupt();
        cupid.interrupt();

        for (var cell : sexualCells) {
            cell.stopCell();
        }

        for (var cell : asexualCells) {
            cell.stopCell();
        }
    }

    public void addSexualCell(int count) {
        for (int i = 0; i < count; i++) {
            SexualCell cell = new SexualCell(foodPool, watcher, cupid, config, true);
            sexualCells.add(cell);

            Thread thread = new Thread(cell);
            thread.start();
        }
    }

    public void addAsexualCell(int count) {
        for (int i = 0; i < count; i++) {
            AsexualCell cell = new AsexualCell(foodPool, watcher, config, true);
            asexualCells.add(cell);

            Thread thread = new Thread(cell);
            thread.start();
        }
    }

    public void addFoodUnits(int amount) {
        foodPool.addFood(amount);
        synchronized (watcher) {
            watcher.notifyFoodAvailable();
        }
        globalState.addFood(amount);
    }
}
