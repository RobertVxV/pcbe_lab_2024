package com.celluloid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
    @Value("${cell.full.time:1000}")
    public int T_FULL;

    @Value("${cell.starve.time:2000}")
    public int T_STARVE;

    @Value("${cell.full.variance:500}")
    public int T_FULL_VARIANCE;

    @Value("${cell.reproduction.threshold:3}")
    public int REPRODUCTION_THRESHOLD;
}
