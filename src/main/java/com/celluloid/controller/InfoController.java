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

    private final FoodPool foodPool;
    private final CellRegister cellRegister;

    @Autowired
    public InfoController(
            FoodPool foodPool,
            CellRegister cellRegister
    ) {
        this.foodPool = foodPool;
        this.cellRegister = cellRegister;
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return Map.of(
                "sexualCellsAlive", cellRegister.getSexualCellCount(),
                "asexualCellsAlive", cellRegister.getAsexualCellCount(),
                "cellsDied", cellRegister.getDeadCellCount(),
                "foodUnitsAvailable", foodPool.getTotalFood()
        );
    }
}
