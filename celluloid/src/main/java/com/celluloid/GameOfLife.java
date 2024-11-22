package com.celluloid;

import com.celluloid.cell.AsexualCell;
import com.celluloid.cell.SexualCell;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GameOfLife {
    private final FoodPool foodPool = new FoodPool();
    private final Watcher watcher = new Watcher(foodPool);

    private final ArrayList<SexualCell> sexualCells = new ArrayList<>();
    private final ArrayList<AsexualCell> asexualCells = new ArrayList<>();

    public GameOfLife() {
        for (int i = 0; i < Config.SEXUAL_CELLS_COUNT; i++) {
            sexualCells.add(new SexualCell(foodPool, watcher));
        }

        for (int i = 0; i < Config.ASEXUAL_CELLS_COUNT; i++) {
            asexualCells.add(new AsexualCell(foodPool, watcher));
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startSimulation() {
        watcher.start();

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
