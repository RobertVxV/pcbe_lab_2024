package com.celluloid.event;

import com.celluloid.DeadCellRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.PriorityQueue;

@Service
public class EventQueue {
    private final PriorityQueue<Event> queue = new PriorityQueue<>();
    private boolean running = true;
    private final DeadCellRegister deadCells;

    @Autowired
    public EventQueue(DeadCellRegister deadCells) {
        this.deadCells = deadCells;
    }

    public void startThread() {
        System.out.println("Starting event queue!");
        running = true;
        Thread thread = new Thread(this::run);
        thread.start();
    }

    public void endThread() {
        System.out.println("We ended the event queue");
        running = false;
    }

    public void add(Event event) {
        if (event == null) {
            return;
        }

        synchronized (this) {
            queue.add(event);
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

    private void run() {
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

    private void removeDeadCellEvents() {
        synchronized (this) {
            while (!queue.isEmpty() && deadCells.contains(queue.peek().targetCell())) {
                queue.poll();
            }
        }
    }
}
