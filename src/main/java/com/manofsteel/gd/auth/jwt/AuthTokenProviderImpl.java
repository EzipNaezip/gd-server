package com.manofsteel.gd.auth.jwt;

import com.manofsteel.gd.auth.exception.TokenValidateException;
import com.manofsteel.gd.type.dto.UserPrincipal;
import com.manofsteel.gd.type.etc.OAuthProvider;
import com.manofsteel.gd.type.etc.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

public class AuthTokenProviderImpl implements AuthTokenProvider {

	private final Key key;

	public AuthTokenProviderImpl(String secret) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public AuthToken createToken(UserPrincipal userPrincipal, Date expiry) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", userPrincipal.getUserId().toString());
		claims.put("name", userPrincipal.getName());
		claims.put("email", userPrincipal.getEmail());
		claims.put("description", userPrincipal.getDescription());
		claims.put("profileImgUrl", userPrincipal.getProfileImgUrl());
		claims.put("postCount", userPrincipal.getPostCount().toString());
		claims.put("followerCount", userPrincipal.getFollowerCount().toString());
		claims.put("followCount", userPrincipal.getFollowCount().toString());
		claims.put("role", userPrincipal.getRole().name());
		claims.put("provider", userPrincipal.getProvider().name());

		// Add other claims as needed

		return new AuthToken(claims, expiry, key);
	}

	@Override
	public AuthToken createToken(String id, String role, Date expiry) {
		return new AuthToken(id, role, expiry, key);
	}

	public AuthToken convertToken(String token) {
		return new AuthToken(token, key);
	}

	public Authentication getAuthentication(AuthToken authToken) {
		if (authToken.validate()) {
			Claims claims = authToken.getClaims();
			Collection<? extends GrantedAuthority> authorities = Arrays.stream(
							((String)claims.get("role")).split("\\|"))
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());

			// Convert the information in the claims to match UserPrincipal properties
			Long userId = Long.valueOf((String) claims.get("userId"));
			String name = (String) claims.get("name");
			String email = (String) claims.get("email");
			String description = (String) claims.get("description");
			String profileImgUrl = (String) claims.get("profileImgUrl");
			Long postCount = Long.valueOf((String) claims.get("postCount"));
			Long followerCount = Long.valueOf((String) claims.get("followerCount"));
			Long followCount = Long.valueOf((String) claims.get("followCount"));
			Role role = Role.valueOf((String) claims.get("role"));
			OAuthProvider provider = OAuthProvider.valueOf((String) claims.get("provider"));

			UserPrincipal principal = new UserPrincipal(userId, name, email, description, profileImgUrl, postCount, followerCount, followCount, role, provider, (Collection<GrantedAuthority>) authorities, new HashMap<>());
			return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
		} else {
			throw new TokenValidateException();
		}
	}


}