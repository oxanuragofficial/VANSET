package vanset_backend.config;

import java.security.interfaces.RSAPublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtDecoderConfig {

    @Bean
    public JwtDecoder jwtDecoder(java.security.KeyPair keyPair) {

        RSAPublicKey publicKey =
                (RSAPublicKey) keyPair.getPublic();

        return NimbusJwtDecoder
                .withPublicKey(publicKey)
                .build();
    }
}