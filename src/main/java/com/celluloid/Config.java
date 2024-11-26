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

    public int getTFull() {
        return tFull;
    }

    public void setTFull(int tFull) {
        this.tFull = tFull;
    }

    public int getTStarve() {
        return tStarve;
    }

    public void setTStarve(int tStarve) {
        this.tStarve = tStarve;
    }

    public int getTFullVariance() {
        return tFullVariance;
    }

    public void setTFullVariance(int tFullVariance) {
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
