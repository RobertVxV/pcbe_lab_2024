package com.celluloid.event;

import com.celluloid.cell.Cell;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class EventQueue implements Runnable {
    private final PriorityQueue<Event> queue = new PriorityQueue<>();
    private boolean running = true;
    private final Set<String> deadCells = new HashSet<>();

    public EventQueue() {
    }

    public void add(Event event) {
        if (event == null) {
            return;
        }

        synchronized (queue) {
            queue.add(event);
        }
    }

    @Override
    public void run() {
        while (running) {
            synchronized (this) {
                removeDeadCellEvents();

                long deltaMillis = Integer.MAX_VALUE;
                if (queue.peek() != null) {
                    var firstTimestamp = queue.peek().timestamp();
                    deltaMillis = Duration.between(Instant.now(), firstTimestamp).toMillis();
                }

                System.out.printf("queue size: %d%n", queue.size());

//                try {
//                    queue.wait(deltaMillis);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//
//                queue.notifyAll();
                if (deltaMillis <= 0) {
                    this.notifyAll();
                    try {
                        wait(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public Event front() {
        synchronized (queue) {
            return queue.peek();
        }
    }

    public void pop() {
        synchronized (queue) {
            queue.poll();
        }
    }

    public void addDeadCell(Cell cell) {
        synchronized (deadCells) {
            deadCells.add(cell.getName());
        }
    }

    private void removeDeadCellEvents() {
        synchronized (deadCells) {
            while (!queue.isEmpty() && deadCells.contains(queue.peek().cell().getName())) {
                System.out.println("removed event for dead cell: " + queue.peek().cell().getName());
                queue.poll();
            }
        }
    }
}
