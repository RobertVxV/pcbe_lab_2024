package com.celluloid;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GameOfLife {
    private final FoodPool foodPool = new FoodPool();
    private final Watcher watcher = new Watcher(foodPool);
    private final Cupid cupid = new Cupid();

    private final ArrayList<SexualCell> sexualCells = new ArrayList<>();
    private final ArrayList<AsexualCell> asexualCells = new ArrayList<>();

    public GameOfLife() {
        for (int i = 0; i < Config.SEXUAL_CELLS_COUNT; i++) {
            sexualCells.add(new SexualCell("sexual_cell_" + i, foodPool, watcher, cupid));
        }

        for (int i = 0; i < Config.ASEXUAL_CELLS_COUNT; i++) {
            asexualCells.add(new AsexualCell("asexual_cell_" + i, foodPool, watcher));
        }
    }

    @PostConstruct
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
