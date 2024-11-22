package com.celluloid;

import org.springframework.stereotype.Component;

@Component
public class Config {
//    @Value("${cell.full.time:1000}")
    public static int T_FULL = 1000;

//    @Value("${cell.starve.time:2000}")
    public static int T_STARVE = 2000;

//    @Value("${cell.full.variance:500}")
    public static int T_FULL_VARIANCE = 500;

//    @Value("${cell.reproduction.threshold:3}")
    public static int REPRODUCTION_THRESHOLD = 3;

    public static int SEXUAL_CELLS_COUNT = 4;

    public static int ASEXUAL_CELLS_COUNT = 3;
}
