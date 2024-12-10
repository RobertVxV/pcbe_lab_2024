package com.celluloid;

import com.celluloid.event.EventQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class GameOfLife {
    private final FoodPool foodPool;
    private final EventQueue eventQueue;
    private final CellRegister cellRegister;

    @Autowired
    public GameOfLife(
            FoodPool foodPool,
            EventQueue eventQueue,
            CellRegister cellRegister
    ) {
        this.foodPool = foodPool;
        this.eventQueue = eventQueue;
        this.cellRegister = cellRegister;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startSimulation() {
        System.out.println("We are starting the simulation!");
        eventQueue.startThread();
        cellRegister.startCellThreads();
    }

    public void endSimulation() {
        cellRegister.endCellThreads();
        eventQueue.endThread();
    }

    public void restartSimulation() {
        endSimulation();
        startSimulation();
    }

    public void addSexualCells(int count) {
        for (int i = 0; i < count; i++) {
            cellRegister.addSexualCell();
        }
    }

    public void addAsexualCells(int count) {
        for (int i = 0; i < count; i++) {
            cellRegister.addAsexualCell();
        }
    }

    public void addFoodUnits(int amount) {
        foodPool.addFood(amount);
    }
}
