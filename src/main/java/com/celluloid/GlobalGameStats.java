package com.celluloid;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class GlobalGameStats {
    private static GlobalGameStats instance;

    private AtomicInteger sexualCellsAlive = new AtomicInteger(4);
    private AtomicInteger asexualCellsAlive = new AtomicInteger(3);
    private AtomicInteger cellsDied = new AtomicInteger(0);
    private AtomicInteger totalFood = new AtomicInteger(100);

    private static class SingletonHelper {
        private static final GlobalGameStats INSTANCE = new GlobalGameStats();
    }

    private GlobalGameStats() {}

    public static GlobalGameStats getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public int getSexualCellsAlive() {
        return sexualCellsAlive.get();
    }

    public void incrementSexualCellsAlive(int amount) {
        int newCount = sexualCellsAlive.addAndGet(amount);
    }

    public void decrementSexualCellsAlive() {
        sexualCellsAlive.decrementAndGet();
    }

    public int getAsexualCellsAlive() {
        return asexualCellsAlive.get();
    }

    public void incrementAsexualCellsAlive(int amount) {
        asexualCellsAlive.addAndGet(amount);
    }

    public void decrementAsexualCellsAlive() {
        asexualCellsAlive.decrementAndGet();
    }

    public int getCellsDied() {
        return cellsDied.get();
    }

    public void incrementCellsDied() {
        cellsDied.incrementAndGet();
    }

    public int getTotalFood() {
        return totalFood.get();
    }

    public void addFood(int amount) {
        totalFood.addAndGet(amount);
    }

    public void consumeFood(int amount) {
        totalFood.addAndGet(-amount);
    }
}

