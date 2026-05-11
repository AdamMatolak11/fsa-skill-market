package sk.posam.fsa.skill_market.security;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.profile.UserProfile;
import sk.posam.fsa.skill_market.domain.profile.UserProfileCommandRepository;
import sk.posam.fsa.skill_market.domain.profile.UserProfileQueryRepository;

@Component
public class AuthenticatedUserProvider {

    private static final Logger log = LoggerFactory.getLogger(AuthenticatedUserProvider.class);

    private final UserProfileQueryRepository userProfileQueryRepository;
    private final UserProfileCommandRepository userProfileCommandRepository;

    public AuthenticatedUserProvider(
            UserProfileQueryRepository userProfileQueryRepository,
            UserProfileCommandRepository userProfileCommandRepository
    ) {
        this.userProfileQueryRepository = userProfileQueryRepository;
        this.userProfileCommandRepository = userProfileCommandRepository;
    }

    public Optional<AuthenticatedUser> currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        String email = resolveEmail(authentication);
        if (email == null || email.isBlank()) {
            log.warn("Could not resolve email from authentication: {}", authentication);
            return Optional.empty();
        }

        return userProfileQueryRepository.findByEmail(email)
                .or(() -> provisionNewUser(authentication, email))
                .map(profile -> new AuthenticatedUser(
                        profile.id(),
                        profile.email(),
                        extractRoles(authentication)
                ));
    }

    private Optional<UserProfile> provisionNewUser(Authentication authentication, String email) {
        log.info("Provisioning new user for email: {}", email);
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String displayName = jwt.getClaimAsString("name");
            if (displayName == null || displayName.isBlank()) {
                displayName = jwt.getClaimAsString("preferred_username");
            }

            Set<String> roles = extractRoles(authentication);
            String role = roles.contains("FREELANCER") ? "FREELANCER" :
                    roles.contains("ADMIN") ? "ADMIN" : "CLIENT";

            UserProfile newProfile = UserProfile.restore(
                    UUID.randomUUID(),
                    email,
                    displayName,
                    "",
                    role,
                    Set.of(),
                    BigDecimal.ZERO,
                    0,
                    OffsetDateTime.now()
            );

            log.info("Saving new profile for {} with role {}", email, role);
            return Optional.of(userProfileCommandRepository.save(newProfile));
        }
        log.warn("Authentication principal is not a Jwt: {}", authentication.getPrincipal().getClass());
        return Optional.empty();
    }

    private Set<String> extractRoles(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.substring(5))
                .collect(Collectors.toUnmodifiableSet());
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
}
