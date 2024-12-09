package com.celluloid;

import com.celluloid.cell.AsexualCell;
import com.celluloid.cell.SexualCell;
import com.celluloid.event.EventQueue;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GameOfLife {
    private final FoodPool foodPool = new FoodPool();
    private final EventQueue eventQueue = new EventQueue();

    private final ArrayList<SexualCell> sexualCells = new ArrayList<>();
    private final ArrayList<AsexualCell> asexualCells = new ArrayList<>();

    private final Config config;

    public GameOfLife(Config config) {
        this.config = config;

        for (int i = 0; i <  config.getSexualCellsCount(); i++) {
            sexualCells.add(new SexualCell(foodPool, eventQueue, config));
        }

        for (int i = 0; i < config.getAsexualCellsCount(); i++) {
            asexualCells.add(new AsexualCell(foodPool, eventQueue, config));
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startSimulation() {
        Thread eventQueueThread = new Thread(eventQueue);
        eventQueueThread.start();

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
