package sk.posam.fsa.skill_market.security;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthenticatedUserSyncFilter extends OncePerRequestFilter {

    private final AuthenticatedUserProvider authenticatedUserProvider;

    public AuthenticatedUserSyncFilter(AuthenticatedUserProvider authenticatedUserProvider) {
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        authenticatedUserProvider.currentUser();
        filterChain.doFilter(request, response);
    }
}
