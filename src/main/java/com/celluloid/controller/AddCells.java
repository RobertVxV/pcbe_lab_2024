package com.celluloid.controller;

import com.celluloid.GameOfLife;
import com.celluloid.GlobalGameStats;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/cells")
public class AddCells {

    private final GlobalGameStats globalState = GlobalGameStats.getInstance();

    private final GameOfLife gameOfLife;

    public AddCells(GameOfLife gameOfLife) {
        this.gameOfLife = gameOfLife;
    }

    @PostMapping
    public ResponseEntity<String> addCells(@RequestBody Map<String, Object> body) {
        String type = (String) body.get("type");
        int count = (int) body.get("count");


        if("sexual".equalsIgnoreCase(type))
        {
            gameOfLife.addSexualCell(count);
            globalState.incrementSexualCellsAlive(count);
        }
        else if ("asexual".equalsIgnoreCase(type))
        {
            gameOfLife.addAsexualCell(count);
            globalState.incrementAsexualCellsAlive(count);
        }
        else
        {
            return ResponseEntity.badRequest().body("Invalid cell type: " + type);
        }


        return ResponseEntity.ok("You successfully added " + count + " cells of type " + type);
    }
}