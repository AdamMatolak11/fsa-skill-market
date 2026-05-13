package sk.posam.fsa.skill_market.security;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.profile.SyncIdentityUserCommand;
import sk.posam.fsa.skill_market.domain.profile.UserRole;
import sk.posam.fsa.skill_market.domain.service.IdentityUserSyncFacade;

@Component
public class AuthenticatedUserProvider {

    private final IdentityUserSyncFacade identityUserSyncFacade;

    public AuthenticatedUserProvider(IdentityUserSyncFacade identityUserSyncFacade) {
        this.identityUserSyncFacade = identityUserSyncFacade;
    }

    public Optional<AuthenticatedUser> currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        String email = resolveEmail(authentication);
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }

        UUID userId = resolveUserId(authentication);
        if (userId == null) {
            return Optional.empty();
        }

        Set<String> roles = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.substring(5))
                .collect(java.util.stream.Collectors.toUnmodifiableSet());

        resolveRole(roles).ifPresent(role -> identityUserSyncFacade.syncRegisteredUser(new SyncIdentityUserCommand(
                userId,
                email,
                resolveFirstName(authentication),
                resolveLastName(authentication),
                role
        )));

        return Optional.of(new AuthenticatedUser(userId, email, roles));
    }

    private String resolveEmail(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            String email = jwt.getClaimAsString("email");
            if (email != null && !email.isBlank()) {
                return email;
            }
            return jwt.getClaimAsString("preferred_username");
        }
        return null;
    }

    private UUID resolveUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            String subject = jwt.getSubject();
            if (subject == null || subject.isBlank()) {
                return null;
            }
            return UUID.fromString(subject);
        }
        return null;
    }

    private String resolveFirstName(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            return jwt.getClaimAsString("given_name");
        }
        return null;
    }

    private String resolveLastName(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            return jwt.getClaimAsString("family_name");
        }
        return null;
    }

    private Optional<UserRole> resolveRole(Set<String> roles) {
        if (roles.contains(UserRole.ADMIN.name())) {
            return Optional.of(UserRole.ADMIN);
        }
        if (roles.contains(UserRole.FREELANCER.name())) {
            return Optional.of(UserRole.FREELANCER);
        }
        if (roles.contains(UserRole.CLIENT.name())) {
            return Optional.of(UserRole.CLIENT);
        }
        return Optional.empty();
    }
}
