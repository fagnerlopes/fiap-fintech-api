package br.com.fintech.fintechapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller para health check da aplicação
 */
@RestController
public class HealthCheckController {

    /**
     * Endpoint de health check
     * GET /health
     * 
     * @return JSON com status, data/hora, environment e nome da aplicação
     */
    @GetMapping("/health")
    public Map<String, Object> getHealthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("timestamp", LocalDateTime.now());
        response.put("environment", "local");
        response.put("application", "Fintech API");
        response.put("version", "0.0.1-SNAPSHOT");
        response.put("message", "Sistema de autenticação operacional");
        
        return response;
    }
    
    /**
     * Endpoint home da aplicação
     * GET /
     * 
     * @return JSON com informações básicas
     */
    @GetMapping("/")
    public Map<String, String> home() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Bem-vindo à Fintech API");
        response.put("version", "0.0.1-SNAPSHOT");
        response.put("documentation", "Em desenvolvimento");
        response.put("healthCheck", "/health");
        
        return response;
    }
}
