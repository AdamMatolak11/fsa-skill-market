package sk.posam.fsa.skill_market.registration;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import sk.posam.fsa.skill_market.domain.profile.UserAlreadyExistsException;
import sk.posam.fsa.skill_market.domain.registration.CreateIdentityUserCommand;
import sk.posam.fsa.skill_market.domain.registration.IdentityRegistrationGateway;

@Component
@Profile("keycloak")
public class KeycloakIdentityRegistrationGateway implements IdentityRegistrationGateway {

    private final RestClient restClient;
    private final String baseUrl;
    private final String realm;
    private final String adminRealm;
    private final String adminClientId;
    private final String adminUsername;
    private final String adminPassword;
    private final String applicationClientId;

    public KeycloakIdentityRegistrationGateway(
            RestClient.Builder restClientBuilder,
            @Value("${app.keycloak.base-url:http://localhost:8081}") String baseUrl,
            @Value("${app.keycloak.realm:skill-market}") String realm,
            @Value("${app.keycloak.admin-realm:master}") String adminRealm,
            @Value("${app.keycloak.admin-client-id:admin-cli}") String adminClientId,
            @Value("${app.keycloak.admin-username:admin}") String adminUsername,
            @Value("${app.keycloak.admin-password:admin}") String adminPassword,
            @Value("${app.keycloak.client-id:skill-market-client}") String applicationClientId
    ) {
        this.restClient = restClientBuilder.build();
        this.baseUrl = trimTrailingSlash(baseUrl);
        this.realm = realm;
        this.adminRealm = adminRealm;
        this.adminClientId = adminClientId;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.applicationClientId = applicationClientId;
    }

    @Override
    public UUID register(CreateIdentityUserCommand command) {
        String adminToken = obtainAdminToken();
        String clientUuid = findClientUuid(adminToken);
        Map<String, Object> roleRepresentation = findClientRole(adminToken, clientUuid, command.role().name());

        try {
            URI location = restClient.post()
                    .uri(baseUrl + "/admin/realms/{realm}/users", realm)
                    .header(HttpHeaders.AUTHORIZATION, bearer(adminToken))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "username", command.email().trim().toLowerCase(),
                            "email", command.email().trim().toLowerCase(),
                            "enabled", true,
                            "emailVerified", true,
                            "firstName", command.firstName().trim(),
                            "lastName", command.lastName().trim(),
                            "credentials", List.of(Map.of(
                                    "type", "password",
                                    "value", command.password(),
                                    "temporary", false
                            ))
                    ))
                    .retrieve()
                    .toBodilessEntity()
                    .getHeaders()
                    .getLocation();

            UUID userId = extractUserId(location);

            restClient.post()
                    .uri(baseUrl + "/admin/realms/{realm}/users/{userId}/role-mappings/clients/{clientUuid}", realm, userId, clientUuid)
                    .header(HttpHeaders.AUTHORIZATION, bearer(adminToken))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(List.of(roleRepresentation))
                    .retrieve()
                    .toBodilessEntity();

            return userId;
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().value() == 409) {
                throw new UserAlreadyExistsException(command.email());
            }
            throw exception;
        }
    }

    @Override
    public void delete(UUID userId) {
        String adminToken = obtainAdminToken();
        restClient.delete()
                .uri(baseUrl + "/admin/realms/{realm}/users/{userId}", realm, userId)
                .header(HttpHeaders.AUTHORIZATION, bearer(adminToken))
                .retrieve()
                .toBodilessEntity();
    }

    private String obtainAdminToken() {
        LinkedMultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", adminClientId);
        form.add("username", adminUsername);
        form.add("password", adminPassword);

        Map<?, ?> response = restClient.post()
                .uri(baseUrl + "/realms/{realm}/protocol/openid-connect/token", adminRealm)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(Map.class);

        Object accessToken = response == null ? null : response.get("access_token");
        if (!(accessToken instanceof String token) || token.isBlank()) {
            throw new IllegalStateException("Keycloak admin token was not returned");
        }
        return token;
    }

    @SuppressWarnings("unchecked")
    private String findClientUuid(String adminToken) {
        List<Map<String, Object>> response = restClient.get()
                .uri(baseUrl + "/admin/realms/{realm}/clients?clientId={clientId}", realm, applicationClientId)
                .header(HttpHeaders.AUTHORIZATION, bearer(adminToken))
                .retrieve()
                .body(List.class);

        if (response == null || response.isEmpty()) {
            throw new IllegalStateException("Keycloak client '" + applicationClientId + "' was not found");
        }

        Object clientUuid = response.getFirst().get("id");
        if (!(clientUuid instanceof String clientId) || clientId.isBlank()) {
            throw new IllegalStateException("Keycloak client '" + applicationClientId + "' is missing internal id");
        }
        return clientId;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> findClientRole(String adminToken, String clientUuid, String roleName) {
        Map<String, Object> response = restClient.get()
                .uri(baseUrl + "/admin/realms/{realm}/clients/{clientUuid}/roles/{roleName}", realm, clientUuid, roleName)
                .header(HttpHeaders.AUTHORIZATION, bearer(adminToken))
                .retrieve()
                .body(Map.class);

        if (response == null || response.isEmpty()) {
            throw new IllegalStateException("Keycloak role '" + roleName + "' was not found");
        }

        return response;
    }

    private UUID extractUserId(URI location) {
        if (location == null) {
            throw new IllegalStateException("Keycloak user creation did not return a location header");
        }

        String path = location.getPath();
        String userId = path.substring(path.lastIndexOf('/') + 1);
        return UUID.fromString(userId);
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }

    private String trimTrailingSlash(String value) {
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }
}
