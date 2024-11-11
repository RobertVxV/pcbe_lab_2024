package com.celluloid;

import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class Cupid extends Thread {
    private final LinkedList<SexualCell> waitingCells = new LinkedList<>();

    public void registerCell(SexualCell cell) {
        synchronized (waitingCells) {
            waitingCells.add(cell);
            waitingCells.notifyAll(); // Notify any waiting cells
        }
    }

    @Override
    public void run() {
        while (true) {
            synchronized (waitingCells) {
                while (waitingCells.size() < 2) {
                    try {
                        waitingCells.wait(); // Wait for cells to register
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                // Pair cells for reproduction
                SexualCell cell1 = waitingCells.removeFirst();
                SexualCell cell2 = waitingCells.removeFirst();
                cell1.makeChild(cell2);
            }
        }
    }
}
