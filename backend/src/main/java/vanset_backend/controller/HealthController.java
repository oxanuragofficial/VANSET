package vanset_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vanset_backend.service.HealthService;

@RestController
public class HealthController {

    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping("/api/health")
    public String health() {
        return healthService.getHealthStatus();
    }
}