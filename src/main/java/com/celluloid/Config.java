package com.celluloid;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gameoflife")
public class Config {
    private int reproductionThreshold;
    private int tFull;
    private int tStarve;
    private int tFullVariance;
    private int sexualCellsCount;
    private int asexualCellsCount;

    // Getters and setters
    public int getReproductionThreshold() {
        return reproductionThreshold;
    }

    public void setReproductionThreshold(int reproductionThreshold) {
        this.reproductionThreshold = reproductionThreshold;
    }

    public int gettFull() {
        return tFull;
    }

    public void settFull(int tFull) {
        this.tFull = tFull;
    }

    public int gettStarve() {
        return tStarve;
    }

    public void settStarve(int tStarve) {
        this.tStarve = tStarve;
    }

    public int gettFullVariance() {
        return tFullVariance;
    }

    public void settFullVariance(int tFullVariance) {
        this.tFullVariance = tFullVariance;
    }

    public int getSexualCellsCount() {
        return sexualCellsCount;
    }

    public void setSexualCellsCount(int sexualCellsCount) {
        this.sexualCellsCount = sexualCellsCount;
    }

    public int getAsexualCellsCount() {
        return asexualCellsCount;
    }

    public void setAsexualCellsCount(int asexualCellsCount) {
        this.asexualCellsCount = asexualCellsCount;
    }
}
