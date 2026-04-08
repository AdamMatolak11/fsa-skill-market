package sk.posam.fsa.skill_market.infrastructure.security;

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
		authorities.addAll(extractRealmRoles(jwt));
		return authorities;
	}

	@SuppressWarnings("unchecked")
	private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
		Object realmAccess = jwt.getClaim("realm_access");
		if (!(realmAccess instanceof Map<?, ?> realmMap)) {
			return List.of();
		}

		Object roles = realmMap.get("roles");
		if (!(roles instanceof Collection<?> roleNames)) {
			return List.of();
		}

		return roleNames.stream()
			.filter(String.class::isInstance)
			.map(String.class::cast)
			.map(String::toUpperCase)
			.map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
			.map(GrantedAuthority.class::cast)
			.toList();
	}
}
