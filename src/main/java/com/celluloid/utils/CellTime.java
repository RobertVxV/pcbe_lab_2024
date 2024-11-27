package com.celluloid.utils;

import com.celluloid.cell.Cell;

import java.time.Instant;

public record CellTime(Cell cell, Instant timestamp) implements Comparable<CellTime> {

    // The compareTo method required by the Comparable interface
    @Override
    public int compareTo(CellTime other) {
        return this.timestamp.compareTo(other.timestamp);
    }
}
