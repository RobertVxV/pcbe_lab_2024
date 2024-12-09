package com.celluloid.controller;

import com.celluloid.CellRegister;
import com.celluloid.FoodPool;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Information", description = "Endpoints for accessing application runtime information.")
@RequestMapping("/info")
public class InfoController {

    @Autowired
    @GetMapping("/stats")
    public Map<String, Object> getStats(
            FoodPool foodPool,
            CellRegister cellRegister
    ) {
        return Map.of(
                "sexualCellsAlive", cellRegister.getSexualCellCount(),
                "asexualCellsAlive", cellRegister.getAsexualCellCount(),
                "cellsDied", cellRegister.getDeadCellCount(),
                "foodUnitsAvailable", foodPool.getTotalFood()
        );
    }
}
