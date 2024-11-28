package com.celluloid;

import com.celluloid.cell.AsexualCell;
import com.celluloid.cell.SexualCell;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GameOfLife {
    private FoodPool foodPool;
    private final Watcher watcher = new Watcher(foodPool);
    private final Cupid cupid = new Cupid();

    private final ArrayList<SexualCell> sexualCells = new ArrayList<>();
    private final ArrayList<AsexualCell> asexualCells = new ArrayList<>();

    private final Config config;

    public GameOfLife(Config config) {
        this.config = config;
        foodPool = new FoodPool(config.getStartFood());

        for (int i = 0; i <  config.getSexualCellsCount(); i++) {
            sexualCells.add(new SexualCell(foodPool, watcher, cupid, config));
        }

        for (int i = 0; i < config.getAsexualCellsCount(); i++) {
            asexualCells.add(new AsexualCell(foodPool, watcher, config));
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
}
