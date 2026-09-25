package vanset_backend.service;

import org.springframework.stereotype.Service;

@Service
public class HealthService {

    public String getHealthStatus() {
        return """
                {
                  "status": "UP",
                  "service": "vanset-backend"
                }
                """;
    }
}