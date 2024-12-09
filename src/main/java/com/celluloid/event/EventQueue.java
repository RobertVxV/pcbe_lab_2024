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

        synchronized (this) {
            queue.add(event);
        }
    }

    @Override
    public void run() {
        while (running) {
            synchronized (this) {
                removeDeadCellEvents();

                long millisUntilNextEvent = Integer.MAX_VALUE;
                if (queue.peek() != null) {
                    var firstTimestamp = queue.peek().timestamp();
                    millisUntilNextEvent = Duration.between(Instant.now(), firstTimestamp).toMillis();
                }

//                @TODO fix or remove
//                try {
//                    queue.wait(millisUntilNextEvent);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//
//                queue.notifyAll();

                if (millisUntilNextEvent <= 0) {
                    notifyAll();
                }

                try {
                    wait(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public Event front() {
        synchronized (this) {
            return queue.peek();
        }
    }

    public void pop() {
        synchronized (this) {
            queue.poll();
        }
    }

    public void addDeadCell(Cell cell) {
        synchronized (deadCells) {
            deadCells.add(cell.getName());
        }
    }

    private void removeDeadCellEvents() {
        synchronized (this) {
            synchronized (deadCells) {
                while (!queue.isEmpty() && deadCells.contains(queue.peek().targetCell().getName())) {
                    queue.poll();
                }
            }
        }
    }
}
