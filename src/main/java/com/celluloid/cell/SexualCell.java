package com.celluloid.cell;

import com.celluloid.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class SexualCell extends Cell {
    private boolean isSeekingPartner = false;
    private Cupid cupid;
    private Config config;

    public SexualCell(FoodPool foodPool, Watcher watcher, Cupid cupid, Config config) {
        super(foodPool, watcher, config);
        this.cupid = cupid;
        this.config = config;
    }

    @Override
    public synchronized void reproduce() {
        if (!isSeekingPartner) {
            System.out.println(this.getName() + " is seeking a partner to reproduce.");
            isSeekingPartner = true;
            cupid.registerCell(this);
        }
    }

    @Override
    public String getName() {
        return "SexualCell_" + cellIndex;
    }

    public synchronized void makeChild(SexualCell partner) {
        if (partner != null) {
            System.out.println(this.getName() + " found a partner: " + partner.getName());
            SexualCell child = new SexualCell(foodPool, watcher, cupid, config);

            Thread thread = new Thread(child);
            thread.start();
        }
    }

    @RabbitListener(queues = RabbitMQConfig.cupidQueue)
    public void consume(Object [] cells)
    {
        SexualCell cell1 = (SexualCell) cells[0];
        SexualCell cell2 = (SexualCell) cells[1];

        cell1.makeChild(cell2);
    }
}
