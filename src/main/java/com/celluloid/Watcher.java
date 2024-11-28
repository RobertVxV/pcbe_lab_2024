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
        synchronized (notifyQueue) {
            notifyQueue.add(new CellTime(cell, time));
//            System.out.println("Added " + cell.getName() + " to queue with time: " + time);
            notifyQueue.notify(); // Notify all waiting threads
        }
    }


    @Override
    public void run() {
        try {
            while (true) {
                CellTime entry;
                synchronized (notifyQueue) {
                    while (notifyQueue.isEmpty() || notifyQueue.peek().timestamp().isAfter(Instant.now())) {
                        if (!notifyQueue.isEmpty()) {
                            Instant nextTime = notifyQueue.peek().timestamp();
                            long waitTime = nextTime.toEpochMilli() - Instant.now().toEpochMilli();
//                            System.out.println("Next cell timestamp: " + nextTime + ", Current time: " + Instant.now());
//                            System.out.println("Waiting for: " + waitTime + "ms. Queue size: " + notifyQueue.size());

                            if (waitTime <= 0) {
                                break; // Force processing
                            }
                            notifyQueue.wait(Math.max(waitTime, 1000)); // Wake up every second to recheck
                        } else {
//                            System.out.println("Queue is empty. Waiting indefinitely.");
                            notifyQueue.wait(1000); // Periodically wake up even if empty
                        }
                    }
//                    System.out.println("Processing queue. Size before poll: " + notifyQueue.size());
                    entry = notifyQueue.poll();
                    //loop... needs fixing??
                    System.out.println("Polled entry: " + (entry != null ? entry.cell().getName() : "null"));
                }

                if (entry != null) {
                    var cell = entry.cell();
                    synchronized (cell) {
//                        System.out.println("Notifying cell: " + cell.getName());
                        cell.notify();
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Watcher thread interrupted. Stopping thread.");
        }
    }

}
