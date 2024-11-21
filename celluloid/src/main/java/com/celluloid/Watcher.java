package com.celluloid;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Random;

@Service
public class Watcher extends Thread {
    private final FoodPool foodPool;
    private final LinkedList<SexualCell> waitingCells = new LinkedList<>();

    public Watcher(FoodPool foodPool) {
        this.foodPool = foodPool;
    }

    public void registerCell(SexualCell cell) {
        synchronized (waitingCells) {
            waitingCells.add(cell);
            waitingCells.notifyAll(); // Notify any waiting cells
        }
    }
    public void notifyCellDeath(Cell cell) {
        System.out.println(cell.getName() + " has died and food has been added to the pool.");
        Random rand = new Random();
        int foodToAdd = rand.nextInt(5) + 1;
        foodPool.addFood(foodToAdd);
        System.out.println(cell.getName() + " added " + foodToAdd + " food to the pool.");
    }


    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(50); // Check food status at intervals
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (this) {
                if (foodPool.getTotalFood() > 0) {
                    notifyAll(); // Notify cells if food is available
                }
            }
            synchronized (waitingCells) {
                while (waitingCells.size() < 2) {
                    try {
                        waitingCells.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                // Pair cells for reproduction
                if (waitingCells.size() >= 2) {
                    SexualCell cell1 = waitingCells.removeFirst();
                    SexualCell cell2 = waitingCells.removeFirst();
                    cell1.makeChild(cell2);
                }
            }
        }
    }


}
