package vanset_backend.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import vanset_backend.entity.User;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(User user) {

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("vanset")
                .issuedAt(now)
                .expiresAt(
                        now.plus(24, ChronoUnit.HOURS)
                )
                .subject(
                        user.getId().toString()
                )
                .claim(
                        "role",
                        user.getRole().name()
                )
                .claim(
                        "email",
                        user.getEmail()
                )
                .claim(
                        "phone",
                        user.getPhone()
                )
                .build();

        return jwtEncoder
                .encode(
                        JwtEncoderParameters.from(claims)
                )
                .getTokenValue();
    }
}