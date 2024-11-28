package com.celluloid.controller;

import com.celluloid.Config;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@RestController
@Tag(name = "Configuration", description = "Endpoints for accessing application configuration")
@RequestMapping(value = "/config")
public class ConfigController {
    private final Config config;

    public ConfigController(Config config){
        this.config=config;
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
                        "reproductionThreshold", config.getReproductionThreshold(),
                        "tFull", config.getTFull(),
                        "tStarve", config.getTStarve(),
                        "tFullVariance", config.getTFullVariance(),
                        "sexualCellsCount", config.getSexualCellsCount(),
                        "asexualCellsCount", config.getAsexualCellsCount()
                )
        );
    }


    @PostMapping
    public ResponseEntity<String> saveConfig(@RequestBody Config newConfig) {

        config.setAsexualCellsCount(newConfig.getAsexualCellsCount());
        config.setSexualCellsCount(newConfig.getSexualCellsCount());
        config.setReproductionThreshold(newConfig.getReproductionThreshold());
        config.setTFullVariance(newConfig.getTFullVariance());
        config.setTFull(newConfig.getTFull());
        config.setTStarve(newConfig.getTStarve());

        Yaml yaml = new Yaml(createDumperOptions());
        try(FileWriter writer = new FileWriter("src/main/resources/application.yml", false)) {
            yaml.dump(createConfigMap(newConfig), writer);
            return new ResponseEntity<>("Configuration updated!", HttpStatus.OK);
        } catch (IOException e)
        {
            return new ResponseEntity<>("Failed to save configuration!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<String> updateConfig(@RequestBody Config newConfig) {
        return saveConfig(newConfig);
    }
}