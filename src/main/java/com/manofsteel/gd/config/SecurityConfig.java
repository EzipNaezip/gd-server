package com.manofsteel.gd.config;

import com.manofsteel.gd.auth.filter.TokenAuthFilter;
import com.manofsteel.gd.auth.handler.OAuth2AuthenticationFailureHandler;
import com.manofsteel.gd.auth.handler.OAuth2AuthenticationSuccessHandler;
import com.manofsteel.gd.auth.jwt.AuthTokenProvider;
import com.manofsteel.gd.auth.service.CustomOAuth2UserService;
import com.manofsteel.gd.type.dto.ResponseModel;
import com.manofsteel.gd.type.etc.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.OutputStream;
import java.util.List;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final AuthTokenProvider authTokenProvider;

    private static final String[] PERMIT_ALL = {

//            "/v2/api-docs",
//            "/swagger-resources",
//            "/swagger-resources/**",
//            "/swagger-ui.html",
//            "/v3/api-docs/**",
//            "/swagger-ui/**",
//            "/login/**",
//            "/configuration/ui",
//            "/configuration/security",
//            "/webjars/**",
//            "/interior",
//            "/tags/extract/6",
//            "/posts/list",
//            "/dalle/image",
//            "/tags/**",
//            "/user/**",
//            "/comments/**",
//            "/",
            "/**"
    };

    private static final String[] SwaggerPetern = {


    };
    /*
    HttpSecurity 의 sessionManagement()는 Exception을 발생시킨다.
    상세 타입이 아니라 선언 자체가 Exception.class를 발생시킨다.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .failureHandler(oAuth2AuthenticationFailureHandler())
                .and()
                .authorizeHttpRequests()
           .requestMatchers(SwaggerPetern).hasRole("ADMIN")
                .requestMatchers(PERMIT_ALL).permitAll()
                .anyRequest().authenticated();


        //Rest API 이기에, Stateless라 CSRF 방어 불필요
        http.csrf().disable();

        http.addFilterBefore(tokenAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        http.cors().configurationSource(corsConfigurationSource());

        http.exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    ResponseModel responseModel = ResponseModel.builder()
                            .httpStatus(HttpStatus.UNAUTHORIZED)
                            .message("인증되지 않은 사용자이거나 권한이 부족합니다.")
                            .build();
                    response.setStatus(401);
                    response.setContentType("application/json");
                    OutputStream outputStream = response.getOutputStream();
                    outputStream.write(responseModel.toJson().getBytes());
                });
        return http.build();
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(authTokenProvider);
    }

    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler();
    }

    @Bean
    public TokenAuthFilter tokenAuthFilter() {
        return new TokenAuthFilter(authTokenProvider);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
