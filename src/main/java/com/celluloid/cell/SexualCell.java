package com.celluloid.cell;

import com.celluloid.Config;
import com.celluloid.FoodPool;
import com.celluloid.GlobalState;
import com.celluloid.event.EventQueue;
import jakarta.annotation.Nonnull;

import java.util.HashSet;
import java.util.Set;

public class SexualCell extends Cell {
    private static final Set<SexualCell> cellsSeekingPartner = new HashSet<>();
    private final GlobalState globalState = GlobalState.getInstance();

    public SexualCell(FoodPool foodPool, EventQueue eventQueue, Config config) {
        super(foodPool, eventQueue, config);
    }

    private static SexualCell findPartnerFor(Cell cell) {
        synchronized (cellsSeekingPartner) {
            for (SexualCell partner : cellsSeekingPartner) {
                if (partner != cell) {
                    return partner;
                }
            }
            return null;
        }
    }

    @Override
    public void reproduce() {
        SexualCell partner = findPartnerFor(this);

        synchronized (cellsSeekingPartner) {
            if (partner != null) {
                System.out.println(this.getName() + " made a child with " + partner.getName());
                makeChildWith(partner);
                cellsSeekingPartner.remove(partner);
            }
            else {
                System.out.println(this.getName() + " is seeking a partner to reproduce.");
                cellsSeekingPartner.add(this);
            }
        }
    }

    @Override
    public String getName() {
        return "SexualCell_" + cellIndex;
    }

    private synchronized void makeChildWith(@Nonnull SexualCell partner) {
        System.out.println(this.getName() + " found a partner: " + partner.getName());
        SexualCell child = new SexualCell(foodPool, eventQueue, config);
        Thread thread = new Thread(child);
        thread.start();

        globalState.incrementSexualCellsAlive(1);
    }
}
