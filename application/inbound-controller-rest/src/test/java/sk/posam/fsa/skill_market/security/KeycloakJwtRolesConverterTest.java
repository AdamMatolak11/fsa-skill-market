package sk.posam.fsa.skill_market.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import static org.assertj.core.api.Assertions.assertThat;

class KeycloakJwtRolesConverterTest {

    private final KeycloakJwtRolesConverter converter = new KeycloakJwtRolesConverter();

    @Test
    void convert_extractsClientRolesFromResourceAccess() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("resource_access", Map.of(
                        "skill-market-client", Map.of("roles", List.of("CLIENT", "FREELANCER"))
                ))
                .build();

        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        assertThat(authorities)
                .extracting(GrantedAuthority::getAuthority)
                .contains("ROLE_CLIENT", "ROLE_FREELANCER");
    }

    @Test
    void convert_extractsRealmRolesAlongsideClientRoles() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("realm_access", Map.of("roles", List.of("ADMIN")))
                .claim("resource_access", Map.of(
                        "skill-market-client", Map.of("roles", List.of("CLIENT"))
                ))
                .build();

        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        assertThat(authorities)
                .extracting(GrantedAuthority::getAuthority)
                .contains("ROLE_ADMIN", "ROLE_CLIENT");
    }
}
