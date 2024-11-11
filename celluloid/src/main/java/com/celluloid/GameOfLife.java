package com.celluloid;

import com.celluloid.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameOfLife {

    private final FoodPool foodPool;
    private final Watcher watcher;
    private final Cupid cupid;
    private final AsexualCell asexualCell1;
    private final SexualCell sexualCell1;
    private final SexualCell sexualCell2;
    private final SexualCell sexualCell3;

    @Autowired
    public GameOfLife(FoodPool foodPool, Watcher watcher, Cupid cupid,
                      AsexualCell asexualCell1, SexualCell sexualCell1,
                      SexualCell sexualCell2, SexualCell sexualCell3) {
        this.foodPool = foodPool;
        this.watcher = watcher;
        this.cupid = cupid;
        this.asexualCell1 = asexualCell1;
        this.sexualCell1 = sexualCell1;
        this.sexualCell2 = sexualCell2;
        this.sexualCell3 = sexualCell3;
    }

    @PostConstruct
    public void startSimulation() {
        // Start the Watcher and Cupid threads
        watcher.start();
        cupid.start();

        // Start the simulation by running the cells
        asexualCell1.start();
        sexualCell1.start();
        sexualCell2.start();
        sexualCell3.start();
    }
}
