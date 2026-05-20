package sk.posam.fsa.skill_market.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class KeycloakJwtRolesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.addAll(extractUserRole(jwt));
        authorities.addAll(extractClientRoles(jwt));
        return authorities;
    }

    private Collection<GrantedAuthority> extractUserRole(Jwt jwt) {
        String userRole = jwt.getClaimAsString("user_role");
        if (userRole == null || userRole.isBlank()) {
            return List.of();
        }

        // Map "Client" -> "CLIENT" and "Freelancer" -> "FREELANCER"
        String normalizedRole = userRole.toUpperCase();
        return List.of(new SimpleGrantedAuthority(ROLE_PREFIX + normalizedRole));
    }

    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractClientRoles(Jwt jwt) {
        Object resourceAccess = jwt.getClaim("resource_access");
        if (!(resourceAccess instanceof Map<?, ?> resourceAccessMap)) {
            return List.of();
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Object clientAccessValue : resourceAccessMap.values()) {
            if (!(clientAccessValue instanceof Map<?, ?> clientAccess)) {
                continue;
            }

            Object roles = clientAccess.get("roles");
            if (!(roles instanceof Collection<?> roleNames)) {
                continue;
            }

            for (Object roleName : roleNames) {
                if (roleName instanceof String role) {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE_PREFIX + role.toUpperCase());
                    if (!authorities.contains(authority)) {
                        authorities.add(authority);
                    }
                }
            }
        }
        return authorities;
    }
}
