package com.celluloid;

import com.celluloid.cell.Cell;
import com.celluloid.utils.CellTime;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.PriorityQueue;
import java.util.Random;

@Service
public class Watcher extends Thread {
    private final FoodPool foodPool;
    private final PriorityQueue<CellTime> notifyQueue = new PriorityQueue<>();

    public Watcher(FoodPool foodPool) {
        this.foodPool = foodPool;
    }

    public void notifyCellDeath(Cell cell) {
        System.out.println(cell.getName() + " has died and food has been added to the pool.");
        Random rand = new Random();
        int foodToAdd = rand.nextInt(5) + 1;
        foodPool.addFood(foodToAdd);
        System.out.println(cell.getName() + " added " + foodToAdd + " food to the pool.");
    }

    public void addCellToQueue(Cell cell, Instant time) {
        notifyQueue.add(new CellTime(cell, time));
    }

    @Override
    public void run() {

    }


}
