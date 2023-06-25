package com.manofsteel.gd.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class AuthToken {

	@Getter
	private final String token;

	private final Key key;

	AuthToken(String id, String role, Date expiry, Key key) {
		this.key = key;
		token = createToken(id, role, expiry);
	}

	AuthToken(Map<String, Object> claims, Date expiry, Key key) {
		this.key = key;
		token = createToken(claims, expiry);
	}

	private String createToken(String id, String role, Date expiry) {
		return Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setSubject("token")
				.claim("id", id)
				.claim("role", role)
				.setIssuedAt(new Date())
				.setExpiration(expiry)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	private String createToken(Map<String, Object> claims, Date expiry) {
		return Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setSubject("token")
				.addClaims(claims)
				.setIssuedAt(new Date())
				.setExpiration(expiry)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public boolean validate() {
		return this.getClaims() != null;
	}

	public Claims getClaims() {
		Claims claims = null;
		try {
			claims = Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody();
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException ignored) {
			log.debug("Auth Token is Not Validated. : {}", token);
		}
		return claims;
	}
}
