package com.celluloid.controller;
import com.celluloid.Config;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Configuration", description = "Endpoints for accessing application configuration")
public class ConfigController {
    private final Config config;

    public ConfigController(Config config) {
        this.config = config;
    }

    @GetMapping("/config")
    @Operation(
            summary = "Get configuration",
            description = "Returns the current application configuration settings."
    )
    public Config getConfig() {
        return config;
    }
}
