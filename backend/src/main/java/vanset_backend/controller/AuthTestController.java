package vanset_backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthTestController {

    @GetMapping("/api/auth/test")
    public Map<String, Object> testAuthentication(
            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        response.put("authenticated",
                authentication.isAuthenticated());

        response.put("userId",
                authentication.getName());

        response.put("authorities",
                authentication.getAuthorities());

        return response;
    }
}