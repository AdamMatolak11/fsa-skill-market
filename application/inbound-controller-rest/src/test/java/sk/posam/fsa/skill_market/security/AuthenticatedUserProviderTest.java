package sk.posam.fsa.skill_market.security;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import sk.posam.fsa.skill_market.domain.profile.SyncIdentityUserCommand;
import sk.posam.fsa.skill_market.domain.service.IdentityUserSyncFacade;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class AuthenticatedUserProviderTest {

    private final IdentityUserSyncFacade identityUserSyncFacade = mock(IdentityUserSyncFacade.class);
    private final AuthenticatedUserProvider authenticatedUserProvider = new AuthenticatedUserProvider(identityUserSyncFacade);

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void currentUser_syncsMissingProfileFromJwtClaimsAndRoles() {
        UUID userId = UUID.randomUUID();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject(userId.toString())
                .claim("email", "first.login@skillmarket.local")
                .claim("given_name", "First")
                .claim("family_name", "Login")
                .build();

        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(
                jwt,
                List.of(new SimpleGrantedAuthority("ROLE_FREELANCER"))
        ));

        Optional<AuthenticatedUser> currentUser = authenticatedUserProvider.currentUser();

        assertThat(currentUser).isPresent();
        assertThat(currentUser.get().userId()).isEqualTo(userId);
        assertThat(currentUser.get().email()).isEqualTo("first.login@skillmarket.local");
        assertThat(currentUser.get().roles()).isEqualTo(Set.of("FREELANCER"));

        verify(identityUserSyncFacade).syncRegisteredUser(argThat(command ->
                command instanceof SyncIdentityUserCommand sync
                        && sync.userId().equals(userId)
                        && sync.email().equals("first.login@skillmarket.local")
                        && "First".equals(sync.firstName())
                        && "Login".equals(sync.lastName())
                        && sync.role().name().equals("FREELANCER")
        ));
    }

    @Test
    void currentUser_doesNotSyncWhenMarketplaceRoleIsMissing() {
        UUID userId = UUID.randomUUID();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject(userId.toString())
                .claim("email", "norole@skillmarket.local")
                .build();

        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(
                jwt,
                List.of(new SimpleGrantedAuthority("SCOPE_openid"))
        ));

        Optional<AuthenticatedUser> currentUser = authenticatedUserProvider.currentUser();

        assertThat(currentUser).isPresent();
        assertThat(currentUser.get().roles()).isEmpty();
        verifyNoInteractions(identityUserSyncFacade);
    }
}
