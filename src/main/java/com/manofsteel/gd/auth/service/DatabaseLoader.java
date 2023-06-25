package com.manofsteel.gd.auth.service;

import com.manofsteel.gd.repository.UserInfoSetRepository;
import com.manofsteel.gd.repository.UserRepository;
import com.manofsteel.gd.service.ApiKeyService;
import com.manofsteel.gd.type.entity.User;
import com.manofsteel.gd.type.entity.UserInfoSet;
import com.manofsteel.gd.type.etc.OAuthProvider;
import com.manofsteel.gd.type.etc.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ApiKeyService apiKeyService;
    private final UserInfoSetRepository userInfoSetRepository;

    @Override
    public void run(String... strings) {
        User admin = this.userRepository.findByName("admin").orElseGet(() -> {
            User user = User.builder()
                    .name("admin")
                    .email("admin@example.com")
                    .description("This is the admin user")
                    .profileImgUrl("http://example.com/profile.png")
                    .postCount(0L)
                    .followerCount(0L)
                    .followCount(0L)
                    .role(Role.ADMIN)
                    .provider(OAuthProvider.GOOGLE)
                    .build();

            return this.userRepository.save(user);
        });

        if (this.apiKeyService.findByUser(admin) == null) {
            this.apiKeyService.createApiKey(this.apiKeyService.getDefaultApiKey(), admin);
        }

        if (!this.userInfoSetRepository.existsById(admin.getUserId())) {
            UserInfoSet adminUserInfoSet = new UserInfoSet(admin.getUserId());
            this.userInfoSetRepository.save(adminUserInfoSet);
        }
    }

}
