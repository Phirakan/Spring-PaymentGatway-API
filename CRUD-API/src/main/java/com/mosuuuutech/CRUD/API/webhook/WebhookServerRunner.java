package com.mosuuuutech.CRUD.API.webhook;

import com.mosuuuutech.CRUD.API.services.MoneyspaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;

@Component
public class WebhookServerRunner {

    private static final Logger logger = LoggerFactory.getLogger(WebhookServerRunner.class);

    @Autowired
    private MoneyspaceService moneyspaceService;

    @Autowired
    private MoneyspaceWebhookHandler webhookHandler;

    @Value("${moneyspace.webhook.hostname:localhost}")
    private String webhookHostname;

    @Value("${moneyspace.webhook.port:8088}")
    private int webhookPort;

    @Value("${moneyspace.webhook.enabled:true}")
    private boolean webhookEnabled;

    @EventListener(ApplicationReadyEvent.class)
    public void startWebhookServer() {
        if (webhookEnabled) {
            try {
                logger.info("Starting Moneyspace webhook server on {}:{}", webhookHostname, webhookPort);
                moneyspaceService.startWebhookServer(webhookHostname, webhookPort, webhookHandler);
                logger.info("Moneyspace webhook server started successfully");
            } catch (Exception e) {
                logger.error("Failed to start Moneyspace webhook server", e);
            }
        } else {
            logger.info("Moneyspace webhook server is disabled");
        }
    }

    @PreDestroy
    public void stopWebhookServer() {
        if (webhookEnabled) {
            logger.info("Stopping Moneyspace webhook server");
            moneyspaceService.stopWebhookServer();
            logger.info("Moneyspace webhook server stopped");
        }
    }
}