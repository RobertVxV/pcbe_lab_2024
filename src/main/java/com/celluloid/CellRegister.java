package com.celluloid;

import com.celluloid.cell.AsexualCell;
import com.celluloid.cell.Cell;
import com.celluloid.cell.SexualCell;
import com.celluloid.event.EventQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CellRegister {
    private final Set<SexualCell> sexualCells = new HashSet<>();
    private final Set<AsexualCell> asexualCells = new HashSet<>();

    private final Config config;
    private final EventQueue eventQueue;
    private final FoodPool foodPool;
    private final DeadCellRegister deadCells;

    @Autowired
    public CellRegister(
            Config config,
            EventQueue eventQueue,
            FoodPool foodPool,
            DeadCellRegister deadCells
    ) {
        this.config = config;
        this.eventQueue = eventQueue;
        this.foodPool = foodPool;
        this.deadCells = deadCells;

        initialize();
    }

    public void initialize() {
        Cell.cellCounter.set(0);

        synchronized (sexualCells) {
            sexualCells.clear();
            for (int i = 0; i < config.getSexualCellsCount(); i++) {
                addSexualCell();
            }
        }

        synchronized (asexualCells) {
            asexualCells.clear();
            for (int i = 0; i < config.getAsexualCellsCount(); i++) {
                addAsexualCell();
            }
        }

        deadCells.clear();
    }

//    public void startCellThreads() {
//        for (var cell : sexualCells) {
//            Thread thread = new Thread(cell);
//            thread.start();
//        }
//
//        for (var cell : asexualCells) {
//            Thread thread = new Thread(cell);
//            thread.start();
//        }
//    }

    public void endCellThreads() {
        for (var cell : sexualCells) {
            cell.endThread();
        }

        for (var cell : asexualCells) {
            cell.endThread();
        }
    }

    public void addSexualCell() {
        SexualCell cell = new SexualCell(foodPool, eventQueue, config, this);
        synchronized (sexualCells) {
            sexualCells.add(cell);
        }

        Thread thread = new Thread(cell);
        thread.start();
    }

    public void addAsexualCell() {
        AsexualCell cell = new AsexualCell(foodPool, eventQueue, config, this);
        synchronized (asexualCells) {
            asexualCells.add(cell);
        }

        Thread thread = new Thread(cell);
        thread.start();
    }

    public int getSexualCellCount() {
        return sexualCells.size();
    }

    public int getAsexualCellCount() {
        return asexualCells.size();
    }

    public int getDeadCellCount() {
        return deadCells.getDeadCellCount();
    }

    public void registerDeadCell(Cell cell) {
        synchronized (deadCells) {
            deadCells.registerDeadCell(cell);
        }

        if (cell instanceof AsexualCell) {
            synchronized (asexualCells) {
                asexualCells.remove(cell);
            }
        }
        else if (cell instanceof SexualCell) {
            synchronized (sexualCells) {
                sexualCells.remove(cell);
            }
        }
    }
}
