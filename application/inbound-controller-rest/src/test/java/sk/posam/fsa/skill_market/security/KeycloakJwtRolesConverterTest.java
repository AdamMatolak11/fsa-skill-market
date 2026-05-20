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
    void convert_extractsUserRoleFromUserRoleClaim() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("user_role", "Client")
                .build();

        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        assertThat(authorities)
                .extracting(GrantedAuthority::getAuthority)
                .contains("ROLE_CLIENT");
    }

    @Test
    void convert_extractsFreelancerRole() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("user_role", "Freelancer")
                .build();

        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        assertThat(authorities)
                .extracting(GrantedAuthority::getAuthority)
                .contains("ROLE_FREELANCER");
    }

    @Test
    void convert_ignoresMissingUserRole() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "test-subject")
                .build();

        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        assertThat(authorities).isEmpty();
    }

    @Test
    void convert_extractsClientRolesFromResourceAccess() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("user_role", "Client")
                .claim("resource_access", Map.of(
                        "skill-market-client", Map.of("roles", List.of("ADMIN", "MODERATOR"))
                ))
                .build();

        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        assertThat(authorities)
                .extracting(GrantedAuthority::getAuthority)
                .contains("ROLE_CLIENT", "ROLE_ADMIN", "ROLE_MODERATOR");
    }
}
