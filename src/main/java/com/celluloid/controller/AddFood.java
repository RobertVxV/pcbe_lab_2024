package com.celluloid.controller;

import com.celluloid.GameOfLife;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/food")
public class AddFood {

    private final GameOfLife gameOfLife;

    @Autowired
    public AddFood(GameOfLife gameOfLife) {
        this.gameOfLife = gameOfLife;
    }

    @PostMapping
    public ResponseEntity<String> addFood(@RequestBody Map<String, Object> body) {
        int numberOfFoodUnits = (int) body.get("units");

        if (numberOfFoodUnits > 0) {
            gameOfLife.addFoodUnits(numberOfFoodUnits);
        } else {
            return ResponseEntity.badRequest().body("Invalid food unit amount: " + numberOfFoodUnits);
        }

        return ResponseEntity.ok("You have added " + numberOfFoodUnits + " units successfully");
    }
}
