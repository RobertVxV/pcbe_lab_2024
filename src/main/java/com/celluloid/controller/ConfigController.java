package com.celluloid.controller;

import com.celluloid.Config;
import com.celluloid.GameOfLife;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@Tag(name = "Configuration", description = "Endpoints for accessing application configuration")
@RequestMapping(value = "/config")
public class ConfigController {

    private final Config config;
    private final GameOfLife gameOfLife;

    @Autowired
    public ConfigController(Config config, GameOfLife gameOfLife) {
        this.gameOfLife = gameOfLife;
        this.config = config;
    }

    @GetMapping()
    @Operation(
            summary = "Get configuration",
            description = "Returns the current application configuration settings."
    )
    public ResponseEntity<Config> getConfig() {
//        System.out.println("returnare config serializat");
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(config, headers, HttpStatus.OK);
    }

    private DumperOptions createDumperOptions() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return options;
    }

    @PostMapping
    public ResponseEntity<String> saveConfig(@RequestBody Config newConfig) {
        Yaml yaml = new Yaml(createDumperOptions());
        try (FileReader reader = new FileReader("src/main/resources/application.yml")) {
            // Load existing YAML
            Map<String, Object> yamlData = yaml.load(reader);
            if (yamlData == null) yamlData = new LinkedHashMap<>();

            // Update or create the gameoflife section
            Map<String, Object> gameOfLifeConfig = (Map<String, Object>) yamlData.get("gameoflife");
            if (gameOfLifeConfig == null) gameOfLifeConfig = new LinkedHashMap<>();

            gameOfLifeConfig.put("startFood", newConfig.getStartFood());
            gameOfLifeConfig.put("reproductionThreshold", newConfig.getReproductionThreshold());
            gameOfLifeConfig.put("timeFull", newConfig.getTimeFull());
            gameOfLifeConfig.put("timeStarve", newConfig.getTimeStarve());
            gameOfLifeConfig.put("timeFullVariance", newConfig.getTimeFullVariance());
            gameOfLifeConfig.put("sexualCellsCount", newConfig.getSexualCellsCount());
            gameOfLifeConfig.put("asexualCellsCount", newConfig.getAsexualCellsCount());
            gameOfLifeConfig.put("foodAmountAfterDeath", newConfig.getFoodAmountAfterDeath());

            yamlData.put("gameoflife", gameOfLifeConfig);

            // Write the updated YAML back to the file
            try (FileWriter writer = new FileWriter("src/main/resources/application.yml", false)) {
                yaml.dump(yamlData, writer);
                return new ResponseEntity<>("Configuration updated!", HttpStatus.OK);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to save configuration!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<String> updateConfig(@RequestBody Config newConfig) {
        return saveConfig(newConfig);
    }

    @PostMapping("/reset")
    @Operation(
            summary = "Reset configuration or update to new values",
            description = "If a JSON body is provided, updates the application configuration in memory. If no body is provided, resets the configuration to values read from application.yml and writes to application.yml."
    )
    public Config resetConfig(@RequestBody(required = false) Config newConfig) {
        if (newConfig != null) {
            config.setStartFood(newConfig.getStartFood());
            config.setAsexualCellsCount(newConfig.getAsexualCellsCount());
            config.setSexualCellsCount(newConfig.getSexualCellsCount());
            config.setReproductionThreshold(newConfig.getReproductionThreshold());
            config.setTimeFull(newConfig.getTimeFull());
            config.setTimeStarve(newConfig.getTimeStarve());
            config.setTimeFullVariance(newConfig.getTimeFullVariance());
            config.setFoodAmountAfterDeath(newConfig.getFoodAmountAfterDeath());

            gameOfLife.endSimulation();
            gameOfLife.startSimulation();

            return newConfig;
        } else {
            return resetToDefaults();
        }
    }

    private Config resetToDefaults() {
        Yaml yaml = new Yaml();
        try (FileReader reader = new FileReader("src/main/resources/application.yml")) {
            Map<String, Object> yamlData = yaml.load(reader);
            Map<String, Object> gameOfLifeConfig = (Map<String, Object>) yamlData.get("gameoflife");

            config.setAsexualCellsCount((Integer) gameOfLifeConfig.get("asexualCellsCount"));
            config.setSexualCellsCount((Integer) gameOfLifeConfig.get("sexualCellsCount"));
            config.setReproductionThreshold((Integer) gameOfLifeConfig.get("reproductionThreshold"));
            config.setStartFood((Integer) gameOfLifeConfig.get("startFood"));
            config.setTimeFull((Integer) gameOfLifeConfig.get("timeFull"));
            config.setTimeStarve((Integer) gameOfLifeConfig.get("timeStarve"));
            config.setTimeFullVariance((Integer) gameOfLifeConfig.get("timeFullVariance"));
            config.setFoodAmountAfterDeath((Integer) gameOfLifeConfig.get("foodAmountAfterDeath"));

            gameOfLife.endSimulation();
            gameOfLife.startSimulation();

            return config;
        } catch (IOException e) {
            throw new RuntimeException("Failed to reset configuration!", e);
        }
    }
}