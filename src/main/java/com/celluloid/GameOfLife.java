package com.celluloid;

import com.celluloid.cell.AsexualCell;
import com.celluloid.cell.SexualCell;
import com.celluloid.event.EventQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GameOfLife {
    private final GlobalGameStats globalState = GlobalGameStats.getInstance();
    private final FoodPool foodPool;
    private final EventQueue eventQueue = new EventQueue();
    private final Config config;

    private final ArrayList<SexualCell> sexualCells = new ArrayList<>();
    private final ArrayList<AsexualCell> asexualCells = new ArrayList<>();

    @Autowired
    public GameOfLife(Config config) {
        this.config = config;
        this.foodPool = new FoodPool(config.getStartFood());

        for (int i = 0; i < config.getSexualCellsCount(); i++) {
            sexualCells.add(new SexualCell(foodPool, eventQueue, config, false));
        }

        for (int i = 0; i < config.getAsexualCellsCount(); i++) {
            asexualCells.add(new AsexualCell(foodPool, eventQueue, config, false));
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

    public void stopSimulation() {
        for (var cell : sexualCells) {
            cell.stopCell();
        }

        for (var cell : asexualCells) {
            cell.stopCell();
        }
    }

    public void addSexualCell(int count) {
        for (int i = 0; i < count; i++) {
            SexualCell cell = new SexualCell(foodPool, eventQueue, config, true);
            sexualCells.add(cell);

            Thread thread = new Thread(cell);
            thread.start();
        }
    }

    public void addAsexualCell(int count) {
        for (int i = 0; i < count; i++) {
            AsexualCell cell = new AsexualCell(foodPool, eventQueue, config, true);
            asexualCells.add(cell);

            Thread thread = new Thread(cell);
            thread.start();
        }
    }

    public void addFoodUnits(int amount) {
        foodPool.addFood(amount);
        globalState.addFood(amount);
    }
}
