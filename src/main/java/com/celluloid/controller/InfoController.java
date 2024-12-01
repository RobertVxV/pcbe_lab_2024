package com.celluloid.controller;

import com.celluloid.GlobalGameStats;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Information", description = "Endpoints for accessing application runtime information.")
@RequestMapping("/info")
public class InfoController {

    private final GlobalGameStats globalState = GlobalGameStats.getInstance(); // Access the GlobalState singleton

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return Map.of(
                "sexualCellsAlive", globalState.getSexualCellsAlive(),
                "asexualCellsAlive", globalState.getAsexualCellsAlive(),
                "cellsDied", globalState.getCellsDied(),
                "foodUnitsAvailable", globalState.getTotalFood() // Assuming you have a method in GlobalState or another service for this
        );
    }
}
