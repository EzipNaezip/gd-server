package com.manofsteel.gd.service;

import com.google.common.base.Strings;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.manofsteel.gd.auth.jwt.AuthToken;
import com.manofsteel.gd.auth.jwt.AuthTokenProvider;
import com.manofsteel.gd.exception.NotAllowValueException;
import com.manofsteel.gd.exception.NotFoundException;
import com.manofsteel.gd.exception.SearchResultNotExistException;
import com.manofsteel.gd.repository.ApiKeyRepository;
import com.manofsteel.gd.repository.OAuthUserRepository;
import com.manofsteel.gd.repository.UserInfoSetRepository;
import com.manofsteel.gd.repository.UserRepository;
import com.manofsteel.gd.type.dto.UserDto;
import com.manofsteel.gd.type.dto.UserPrincipal;
import com.manofsteel.gd.type.entity.ApiKey;
import com.manofsteel.gd.type.entity.OAuthUser;
import com.manofsteel.gd.type.entity.User;
import com.manofsteel.gd.type.entity.UserInfoSet;
import com.manofsteel.gd.type.etc.OAuthProvider;
import com.manofsteel.gd.type.etc.Role;
import com.manofsteel.gd.util.EntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserInfoSetRepository userInfoSetRepository;
    private final OAuthUserRepository oAuthUserRepository;

    private final ApiKeyRepository apiKeyRepository;
    private final FirebaseApp firebaseApp;
    private final ApiKeyService apiKeyService;
    private final AuthTokenProvider authTokenProvider;
    private static final long TOKEN_DURATION = 1000L * 60L * 60L * 24L;

    public void deleteUserById(Long userId) {
        User user = EntityUtil.findUser(userRepository, userId);
       userRepository.deleteById(userId);
    }

    public User findUserById(Long userId) throws SearchResultNotExistException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        }
        throw new SearchResultNotExistException();
    }

    public void updateUser(UserDto userDto) throws SearchResultNotExistException, NotAllowValueException {
        User user = userRepository.findByUserId(userDto.getUserId()) .orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));

        if (!Strings.isNullOrEmpty(userDto.getName().trim())) {
            user.setName(userDto.getName().trim());
        } else {
            throw new NotAllowValueException();
        }
        if (!Strings.isNullOrEmpty(userDto.getProfileImgUrl().trim())) {
            user.setProfileImgUrl(userDto.getProfileImgUrl().trim());
        }
        user.setDescription(userDto.getDescription());

        //userRepository.deleteById(userDto.getUserId());
        userRepository.save(user);

    }

    public void removeUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public List<User> findAll(Long start, Long display) {
        List<User> userList = userRepository.findAll(start, display);
        List<User> results = userList.stream()
                .collect(Collectors.toList());
        return results;
    }

    public boolean userInfoIsSet(Long userId) {
        return userInfoSetRepository.findById(userId).isEmpty();
    }

    public User saveUserFromFirebase(String uid) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
        UserRecord userRecord;
        try {
            userRecord = firebaseAuth.getUser(uid);
        } catch (FirebaseAuthException e) {
            throw new NotFoundException("Could not find user in Firebase");
        }
//email 중복 x

        List<User> users = userRepository.findAllByEmail(userRecord.getEmail());
        if (!users.isEmpty()) {
            User existingUser = users.get(0); // Choose the first user from the list
            return existingUser;
        }

// Rest of the code to create a new user


        User newUser = User.builder()
                .name(userRecord.getDisplayName())
                .email(userRecord.getEmail())
                .description("당신이 꿈꾸는 방, AI 인테리어 디자이너!")
                .profileImgUrl(userRecord.getPhotoUrl())
                .postCount(0L)
                .followerCount(0L)
                .followCount(0L)
                .role(Role.USER)
                .provider(OAuthProvider.GOOGLE)
                .build();
        userRepository.save(newUser);

        userInfoSetRepository.save(new UserInfoSet(newUser.getUserId()));

        apiKeyService.createApiKey(apiKeyService.getDefaultApiKey(), newUser);

        OAuthUser oAuthUser = OAuthUser.builder()
                .providerUserId(userRecord.getProviderId())
                .email(userRecord.getEmail())
                .name(userRecord.getDisplayName())
                .picture(userRecord.getPhotoUrl())
                .oap(OAuthProvider.GOOGLE)
                .user(newUser)
                .build();
        oAuthUserRepository.save(oAuthUser);

        return newUser;
    }

    public AuthToken getUserToken(User user) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        Date expiry = new Date();
        expiry.setTime(expiry.getTime() + (TOKEN_DURATION));

        AuthToken authToken = authTokenProvider.createToken(userPrincipal, expiry);
        return authToken;
    }

}