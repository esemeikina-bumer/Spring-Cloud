package ru.netology.config_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {

    @Value("${my.custom.property:default-value}")
    private String customProperty;

    @GetMapping("/config")
    public String getConfig() {
        return "Value from Config Server: " + customProperty;
    }
}