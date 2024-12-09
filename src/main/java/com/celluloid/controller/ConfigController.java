package com.celluloid.controller;

import com.celluloid.Config;
import com.celluloid.GameOfLife;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Configuration", description = "Endpoints for accessing application configuration")
@RequestMapping(value = "/config")
public class ConfigController {
    private final Config config;
    private final GameOfLife gameOfLife;

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
        System.out.println("returnare config serializat");
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(config, headers, HttpStatus.OK);
    }

    private DumperOptions createDumperOptions() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return options;
    }

    private Map<String, Object> createConfigMap(Config config) {
        return Map.of(
                "gameoflife", Map.of(
                        "startFood", config.getStartFood(),
                        "reproductionThreshold", config.getReproductionThreshold(),
                        "tFull", config.getTimeFull(),
                        "tStarve", config.getTimeStarve(),
                        "tFullVariance", config.getTimeFullVariance(),
                        "sexualCellsCount", config.getSexualCellsCount(),
                        "asexualCellsCount", config.getAsexualCellsCount()
                )
        );
    }


    @PostMapping
    public ResponseEntity<String> saveConfig(@RequestBody Config newConfig) {

        /*config.setAsexualCellsCount(newConfig.getAsexualCellsCount());
        config.setSexualCellsCount(newConfig.getSexualCellsCount());
        config.setReproductionThreshold(newConfig.getReproductionThreshold());
        config.setTFullVariance(newConfig.getTFullVariance());
        config.setTFull(newConfig.getTFull());
        config.setTStarve(newConfig.getTStarve());*/

        Yaml yaml = new Yaml(createDumperOptions());
        try (FileWriter writer = new FileWriter("src/main/resources/application.yml", false)) {
            yaml.dump(createConfigMap(newConfig), writer);
            return new ResponseEntity<>("Configuration updated!", HttpStatus.OK);
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

            gameOfLife.stopSimulation();
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
            config.setTimeFull((Integer) gameOfLifeConfig.get("tFull"));
            config.setTimeStarve((Integer) gameOfLifeConfig.get("tStarve"));
            config.setTimeFullVariance((Integer) gameOfLifeConfig.get("tFullVariance"));

            gameOfLife.stopSimulation();
            gameOfLife.startSimulation();

            return config;
        } catch (IOException e) {
            throw new RuntimeException("Failed to reset configuration!", e);
        }
    }
}