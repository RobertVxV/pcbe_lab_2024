package com.celluloid.controller;
import com.celluloid.Config;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {
    private final Config config;

    public ConfigController(Config config) {
        this.config = config;
    }

    @GetMapping("/config")
    public Config getConfig() {
        return config;
    }
}
