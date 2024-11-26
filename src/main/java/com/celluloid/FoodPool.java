package com.celluloid;

import org.springframework.stereotype.Service;

import java.util.concurrent.Semaphore;

@Service
public class FoodPool {
    private int totalFood = 3; // Initial amount, configurable later
    private final Semaphore semaphore = new Semaphore(1);

    public int consumeFood(int amount) {
        int foodConsumed = 0;
        try {
            semaphore.acquire();
            if (totalFood >= amount) {
                foodConsumed = amount;
                totalFood -= amount;
            } else {
                foodConsumed = totalFood;
                totalFood = 0;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
        return foodConsumed;
    }

    public void addFood(int amount) {
        try {
            semaphore.acquire();
            totalFood += amount;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }

    public int getTotalFood() {
        int food = 0;
        try {
            semaphore.acquire();
            food = totalFood;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
        return food;
    }
}