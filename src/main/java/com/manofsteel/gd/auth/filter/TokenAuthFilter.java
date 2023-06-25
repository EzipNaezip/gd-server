package com.manofsteel.gd.auth.filter;

import com.manofsteel.gd.auth.jwt.AuthToken;
import com.manofsteel.gd.auth.jwt.AuthTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class TokenAuthFilter extends OncePerRequestFilter {

	private final AuthTokenProvider authTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {
		String token = request.getHeader("Authorization");
		log.info("Authorization token data : {}", token);
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring("Bearer ".length());
		}
		AuthToken authToken = authTokenProvider.convertToken(token);
		log.info("authToken : {}", authToken.getToken());
		if (authToken.validate()) {
			log.info("token validate");
			Authentication authentication = authTokenProvider.getAuthentication(authToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.info("Token Authentication : {}", SecurityContextHolder.getContext().getAuthentication());
		}

		filterChain.doFilter(request, response);
	}
}
