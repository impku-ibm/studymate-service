package com.portal.studymate.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Prevents Render free-tier from spinning down the service after inactivity.
 * Pings the health endpoint every 10 minutes.
 */
@Component
@Slf4j
public class KeepAliveScheduler {

    @Value("${server.port:8080}")
    private int port;

    @Value("${RENDER_EXTERNAL_URL:}")
    private String renderExternalUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 600_000) // every 10 minutes
    public void keepAlive() {
        String url = renderExternalUrl.isBlank()
            ? "http://localhost:" + port + "/actuator/health"
            : renderExternalUrl + "/actuator/health";
        try {
            restTemplate.getForObject(url, String.class);
            log.debug("Keep-alive ping OK: {}", url);
        } catch (Exception e) {
            log.warn("Keep-alive ping failed: {}", e.getMessage());
        }
    }
}
