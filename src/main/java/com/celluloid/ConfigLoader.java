package com.celluloid;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

class ConfigLoader {
    private static Config config;

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.json")) {
            if (input == null) {
                throw new IllegalStateException("config.json not found in resources");
            }
            ObjectMapper mapper = new ObjectMapper();
            config = mapper.readValue(input, Config.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Config getConfig() {
        return config;
    }
}
