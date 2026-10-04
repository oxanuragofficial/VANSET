package vanset_backend.config;

import java.util.Collection;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import vanset_backend.entity.UserRole;

public class JwtAuthenticationConverter
        implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {

        String role = jwt.getClaimAsString("role");

        if (role == null || role.isBlank()) {
            return List.of();
        }

        UserRole userRole;

        try {
            userRole = UserRole.valueOf(role);
        } catch (IllegalArgumentException exception) {
            return List.of();
        }

        return List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" + userRole.name()
                )
        );
    }
}