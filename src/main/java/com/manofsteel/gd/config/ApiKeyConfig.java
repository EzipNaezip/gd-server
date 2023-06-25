package com.manofsteel.gd.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyConfig {

    @Value("${openai.api.key}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}