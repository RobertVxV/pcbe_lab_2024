package com.celluloid.utils;

import com.celluloid.cell.Cell;
import jakarta.annotation.Nonnull;

import java.time.Instant;

public record CellTime(Cell cell, Instant timestamp) implements Comparable<CellTime> {
    @Override
    public int compareTo(@Nonnull CellTime cellTime) {
        return this.timestamp.compareTo(cellTime.timestamp);
    }
}
