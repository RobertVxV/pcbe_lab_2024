package com.celluloid;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gameoflife")
public class Config {
    @JsonProperty("startFood")
    private int startFood;

    @JsonProperty("reproductionThreshold")
    private int reproductionThreshold;

    @JsonProperty("timeFull")
    private int timeFull;

    @JsonProperty("timeStarve")
    private int timeStarve;

    @JsonProperty("timeFullVariance")
    private int timeFullVariance;

    @JsonProperty("sexualCellsCount")
    private int sexualCellsCount;

    @JsonProperty("asexualCellsCount")
    private int asexualCellsCount;

    @JsonProperty("foodAmountAfterDeath")
    private int foodAmountAfterDeath;

    public int getStartFood() {
        return startFood;
    }

    public void setStartFood(int startFood) {
        this.startFood = startFood;
    }

    public int getReproductionThreshold() {
        return reproductionThreshold;
    }

    public void setReproductionThreshold(int reproductionThreshold) {
        this.reproductionThreshold = reproductionThreshold;
    }

    public int getTimeFull() {
        return timeFull;
    }

    public void setTimeFull(int timeFull) {
        this.timeFull = timeFull;
    }

    public int getTimeStarve() {
        return timeStarve;
    }

    public void setTimeStarve(int timeStarve) {
        this.timeStarve = timeStarve;
    }

    public int getTimeFullVariance() {
        return timeFullVariance;
    }

    public void setTimeFullVariance(int timeFullVariance) {
        this.timeFullVariance = timeFullVariance;
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

    public int getFoodAmountAfterDeath() {
        return foodAmountAfterDeath;
    }

    public void setFoodAmountAfterDeath(int foodAmountAfterDeath) {
        this.foodAmountAfterDeath = foodAmountAfterDeath;
    }
}
