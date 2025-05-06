package com.mosuuuutech.CRUD.API.config;

import com.mosuuuutech.CRUD.API.services.MoneyspaceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MoneyspaceConfig {

    @Value("${moneyspace.secret-id}")
    private String secretId;

    @Value("${moneyspace.secret-key}")
    private String secretKey;

    @Value("${moneyspace.success-url}")
    private String successUrl;

    @Value("${moneyspace.fail-url}")
    private String failUrl;

    @Value("${moneyspace.cancel-url}")
    private String cancelUrl;

    @Bean
    public MoneyspaceService moneyspaceService() throws Exception {
        return new MoneyspaceService(secretId, secretKey, successUrl, failUrl, cancelUrl);
    }
}