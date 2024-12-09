package com.celluloid;

import com.celluloid.cell.Cell;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DeadCellRegister {
    private final Set<Cell> deadCells = new HashSet<>();

    public DeadCellRegister() {}

    public void registerDeadCell(Cell deadCell) {
        synchronized (deadCells) {
            deadCells.add(deadCell);
        }
    }

    public boolean contains(Cell cell) {
        synchronized (deadCells) {
            return deadCells.contains(cell);
        }
    }

    public int getDeadCellCount() {
        synchronized (deadCells) {
            return deadCells.size();
        }
    }
}
