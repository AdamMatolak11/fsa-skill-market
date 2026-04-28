package sk.posam.fsa.skill_market.security;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import sk.posam.fsa.skill_market.domain.profile.UserProfileQueryRepository;

@Component
public class AuthenticatedUserProvider {

    private final UserProfileQueryRepository userProfileQueryRepository;

    public AuthenticatedUserProvider(UserProfileQueryRepository userProfileQueryRepository) {
        this.userProfileQueryRepository = userProfileQueryRepository;
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

        return userProfileQueryRepository.findByEmail(email)
                .map(profile -> new AuthenticatedUser(
                        profile.id(),
                        profile.email(),
                        authentication.getAuthorities().stream()
                                .map(grantedAuthority -> grantedAuthority.getAuthority())
                                .filter(authority -> authority.startsWith("ROLE_"))
                                .map(authority -> authority.substring(5))
                                .collect(java.util.stream.Collectors.toUnmodifiableSet())
                ));
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
