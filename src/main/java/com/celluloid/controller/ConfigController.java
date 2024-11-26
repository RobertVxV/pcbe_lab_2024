package com.celluloid.controller;

import com.celluloid.Config;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class ConfigController {
    private final Config config;

    public ConfigController(Config config){
        this.config=config;
    }
    @GetMapping
    public ResponseEntity<Config> getConfig() {
        System.out.println("returnare config serializat");
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(config, headers, HttpStatus.OK);
    }
}