package com.celluloid;

import com.celluloid.cell.AsexualCell;
import com.celluloid.cell.SexualCell;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GameOfLife {
    private final FoodPool foodPool = new FoodPool();
    private final Watcher watcher;
    private final Cupid cupid;

    private final ArrayList<SexualCell> sexualCells = new ArrayList<>();
    private final ArrayList<AsexualCell> asexualCells = new ArrayList<>();

    private final Config config;
    private final RabbitMQConfig rabbitMQConfig;
    private final RabbitTemplate rabbitTemplate;


    public GameOfLife(Config config, RabbitMQConfig rabbitMQConfig, RabbitTemplate rabbitTemplate) {
        this.config = config;
        this.rabbitMQConfig = rabbitMQConfig;
        this.rabbitTemplate = rabbitTemplate;
        this.watcher = new Watcher(foodPool, config);
        this.cupid = new Cupid(rabbitTemplate, rabbitMQConfig);

        for (int i = 0; i <  config.getSexualCellsCount(); i++) {
            sexualCells.add(new SexualCell(foodPool, watcher, cupid, config));
        }

        for (int i = 0; i < config.getAsexualCellsCount(); i++) {
            asexualCells.add(new AsexualCell(foodPool, watcher, config));
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startSimulation() {
        watcher.start();
        cupid.start();

        for (var cell : sexualCells) {
            Thread thread = new Thread(cell);
            thread.start();
        }

        for (var cell : asexualCells) {
            Thread thread = new Thread(cell);
            thread.start();
        }
    }
}
