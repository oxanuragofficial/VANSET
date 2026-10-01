package vanset_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/api/health",
                        "/api/test",
                        "/api/products",
                        "/api/products/**",
                        "/api/categories",
                        "/api/categories/**",
                        "/api/product-variants",
                        "/api/product-variants/**",
                        "/api/collections",
                        "/api/collections/**",
                        "/error",
                        "/api/orders",
                        "/api/orders/**"
                ).permitAll()
                .anyRequest().authenticated()
                );

        return http.build();
    }
}
