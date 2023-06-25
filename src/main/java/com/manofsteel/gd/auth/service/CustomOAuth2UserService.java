package com.manofsteel.gd.auth.service;

import com.manofsteel.gd.auth.factory.OAuth2UserInfoFactory;
import com.manofsteel.gd.repository.OAuthUserRepository;
import com.manofsteel.gd.repository.UserInfoSetRepository;
import com.manofsteel.gd.repository.UserRepository;
import com.manofsteel.gd.service.ApiKeyService;
import com.manofsteel.gd.type.dto.OAuthUserInfo;
import com.manofsteel.gd.type.dto.UserPrincipal;
import com.manofsteel.gd.type.entity.OAuthUser;
import com.manofsteel.gd.type.entity.User;
import com.manofsteel.gd.type.entity.UserInfoSet;
import com.manofsteel.gd.type.etc.OAuthProvider;
import com.manofsteel.gd.type.etc.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService  {

    private final OAuthUserRepository oAuthUserRepository;
    private final UserRepository userRepository;
    private final UserInfoSetRepository userInfoSetRepository;
    private final ApiKeyService apiKeyService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return process(userRequest, oAuth2User);
    }

    OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        OAuthProvider oAuthProvider = OAuthProvider.valueOf(
                userRequest.getClientRegistration().getClientName().toUpperCase());
        OAuthUserInfo oAuth2UserInfo = OAuth2UserInfoFactory.of(oAuthProvider, oAuth2User.getAttributes());
        OAuth2User user = saveOrUpDate(oAuth2UserInfo);
        return user;
    }

    OAuth2User saveOrUpDate(OAuthUserInfo oAuth2UserInfo) {
        Optional<OAuthUser> savedUser;
        savedUser = oAuthUserRepository.findByProviderUserIdAndOap(
                oAuth2UserInfo.getId(),
                OAuthProvider.valueOf(oAuth2UserInfo.getOAuthProviderName().toUpperCase())
        );
        User user;
        OAuthUser oAuthUser;
        if (savedUser.isPresent()) {
            oAuthUser = savedUser.get();
            user = oAuthUser.getUser();
        } else {
            user = User.builder()
                    .name(oAuth2UserInfo.getName())
                    .email(oAuth2UserInfo.getEmail())
                    .description("당신이 꿈꾸는 방, AI 인테리어 디자이너!")
                    .profileImgUrl(oAuth2UserInfo.getPicture())
                    .postCount(0l)
                    .followerCount(0l)
                    .followCount(0l)
                    .role(Role.USER)
                    .provider(OAuthProvider.GOOGLE)
                    .build();
            user = userRepository.save(user);

            userInfoSetRepository.save(new UserInfoSet(user.getUserId()));

            apiKeyService.createApiKey(apiKeyService.getDefaultApiKey(), user);

            oAuthUser = OAuthUser.builder()
                    .providerUserId(oAuth2UserInfo.getId())
                    .email(oAuth2UserInfo.getEmail())
                    .name(oAuth2UserInfo.getName())
                    .picture(oAuth2UserInfo.getPicture())
                    .oap(OAuthProvider.valueOf(oAuth2UserInfo.getOAuthProviderName().toUpperCase()))
                    .user(user)
                    .build();
        }
        oAuthUserRepository.save(oAuthUser);
        return UserPrincipal.create(user);
    }

}