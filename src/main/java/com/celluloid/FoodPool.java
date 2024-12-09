package com.celluloid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodPool {
    private int totalFood;

    @Autowired
    public FoodPool(Config config) {
        this.totalFood = config.getStartFood();
    }

    public void consumeFood(int amount) {
        synchronized (this) {
            if (totalFood >= amount) {
                totalFood -= amount;
            } else {
                totalFood = 0;
            }
        }
    }

    public void addFood(int amount) {
        synchronized (this) {
            totalFood += amount;
        }
    }

    public int getTotalFood() {
        synchronized (this) {
            return totalFood;
        }
    }
}