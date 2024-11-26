package com.celluloid.utils;

import com.celluloid.cell.Cell;

import java.time.Instant;

public record CellTime(Cell cell, Instant timestamp) {
    public static int compare(CellTime c1, CellTime c2) {
        return c1.timestamp.compareTo(c2.timestamp);
    }
}
