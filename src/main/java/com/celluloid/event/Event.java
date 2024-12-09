package com.celluloid.event;

import com.celluloid.cell.Cell;
import jakarta.annotation.Nonnull;

import java.time.Instant;

public record Event(
        @Nonnull Cell targetCell,
        @Nonnull EventType type,
        @Nonnull Instant timestamp)
    implements Comparable<Event> {

    @Override
    public int compareTo(@Nonnull Event event) {
        if (this.timestamp.isBefore(event.timestamp)) {
            return -1;
        }
        if (this.timestamp.isAfter(event.timestamp)) {
            return 1;
        }
        return 0;
    }
}
