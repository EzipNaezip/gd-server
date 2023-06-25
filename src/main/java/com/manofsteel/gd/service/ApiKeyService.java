package com.manofsteel.gd.service;

import com.manofsteel.gd.config.ApiKeyConfig;
import com.manofsteel.gd.type.entity.ApiKey;
import com.manofsteel.gd.type.entity.User;
import com.manofsteel.gd.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyConfig apiKeyConfig;

    public void createApiKey(String apiKey, User user) {
        ApiKey newApiKey = new ApiKey();
        newApiKey.setApiKey(apiKey);
        newApiKey.setUser(user);
        apiKeyRepository.save(newApiKey);
    }

    public void updateApiKey(User user, String newApiKey) {
        ApiKey apiKey = apiKeyRepository.findByUser(user);
        apiKey.setApiKey(newApiKey);
        apiKeyRepository.save(apiKey);
    }

    public void deleteApiKey(User user) {
        apiKeyRepository.deleteByUser(user);
    }

    public ApiKey findByUser(User user) {
        return apiKeyRepository.findByUser(user);
    }

    public String getDefaultApiKey() {
        return apiKeyConfig.getApiKey();
    }

}
