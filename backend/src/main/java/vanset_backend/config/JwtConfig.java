package vanset_backend.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class JwtConfig {

    @Bean
    public KeyPair keyPair() {

        try {
            KeyPairGenerator keyPairGenerator =
                    KeyPairGenerator.getInstance("RSA");

            keyPairGenerator.initialize(2048);

            return keyPairGenerator.generateKeyPair();

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Failed to generate RSA key pair", e);
        }
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(
            KeyPair keyPair) {

        RSAPublicKey publicKey =
                (RSAPublicKey) keyPair.getPublic();

        RSAPrivateKey privateKey =
                (RSAPrivateKey) keyPair.getPrivate();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID("vanset-key")
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);

        return (jwkSelector, securityContext) ->
                jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtEncoder jwtEncoder(
            JWKSource<SecurityContext> jwkSource) {

        return new NimbusJwtEncoder(jwkSource);
    }
}