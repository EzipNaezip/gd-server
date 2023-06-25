package com.manofsteel.gd.auth.service;

import com.manofsteel.gd.auth.jwt.AuthToken;
import com.manofsteel.gd.auth.jwt.AuthTokenProvider;
import com.manofsteel.gd.repository.UserRepository;
import com.manofsteel.gd.type.dto.UserPrincipal;
import com.manofsteel.gd.type.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutoLoginService {

    private final UserRepository userRepository;
    private final Environment environment;
    private final AuthTokenProvider authTokenProvider;

    // Add static variable to store tokens
    public static final Map<String, String> tokenStore = new HashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void doAutoLogin() {
        if (Arrays.asList(this.environment.getActiveProfiles()).contains("dev")) {
            renewAuthToken();
        }
    }

    @Scheduled(cron = "0 0 0 1 1 ?")  // Runs on the 1st day of January at midnight
    public void renewAuthToken() {
        User admin = this.userRepository.findByName("admin").orElseThrow(() -> new UsernameNotFoundException("User 'admin' not found."));
        UserPrincipal userPrincipal = UserPrincipal.create(admin);
        Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, null, List.of(new SimpleGrantedAuthority(admin.getRole().toString())));

        SecurityContextHolder.getContext().setAuthentication(auth);
        log.info("Authentication Principal: {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add(Calendar.YEAR, 1);

        AuthToken authToken = authTokenProvider.createToken(userPrincipal, expirationDate.getTime());
        log.info("Admin UserId: {}", String.valueOf(admin.getUserId()));
        tokenStore.put(admin.getName(), authToken.getToken());
        log.info("tokenStore key 'admin': {}", tokenStore.get("admin"));
    }


}
