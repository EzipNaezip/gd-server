package com.manofsteel.gd.controller;

import com.manofsteel.gd.auth.util.AuthUtil;
import com.manofsteel.gd.repository.UserRepository;
import com.manofsteel.gd.service.FollowService;
import com.manofsteel.gd.type.dto.FollowDto;
import com.manofsteel.gd.type.dto.ResponseModel;
import com.manofsteel.gd.type.dto.UserDto;
import com.manofsteel.gd.type.entity.Follow;
import com.manofsteel.gd.type.entity.User;
import com.manofsteel.gd.util.EntityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
@Tag(name = "Follow")
@Slf4j
public class FollowController {

    private final FollowService followService;
    private final UserRepository userRepository;

    @Operation(summary="팔로우",description = "팔로우가 되어 있지 않는 유저를 팔로우" + " PathVariable : followingId(팔로우할 유저의 userId)")
//    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @PostMapping("/following/{followingId}")
    public ResponseModel follow(
            @PathVariable Long followingId
    ) {
       /* User user = AuthUtil.getAuthenticationInfo();
         if(user == null) {
            return ResponseModel.builder().httpStatus(HttpStatus.UNAUTHORIZED).build();
        }*/
        Long userId = EntityUtil.findUser(userRepository,1L).getUserId();
        followService.follow(userId, followingId);
        ResponseModel responseModel = ResponseModel.builder().httpStatus(HttpStatus.CREATED).build();
        return responseModel;
    }

    @Operation(summary="언팔로우",description = "팔로우가 되어 있는 유저를 언팔로우" + " PathVariable : followingId(언팔로우할 유저의 userId)")
//    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @DeleteMapping("/unfollowing/{followerId}")
    public ResponseModel unfollow(
            @PathVariable Long followerId
    ) {
       // User user = AuthUtil.getAuthenticationInfo();
         /*if(user == null) {
            return ResponseModel.builder().httpStatus(HttpStatus.UNAUTHORIZED).build();
        }*/
        Long userId = EntityUtil.findUser(userRepository,1L).getUserId();
        followService.unfollow(userId, followerId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary="팔로워 목록",description = "팔로워 목록 가져오기")
//    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @GetMapping("/followers/{userId}")
    public ResponseModel getFollowers(@PathVariable Long userId) {

        User loginUser = AuthUtil.getAuthenticationInfo();
        List<User> follower = followService.getFollowersForUser(userId);
        List<FollowDto> userList = new ArrayList<>();
        boolean isFollow;
        for(User users: follower)
        {
            if(loginUser==null)
            {
                isFollow = false;
            }
            else {
                isFollow = followService.isFollow(loginUser, users);
            }

            FollowDto dto = FollowDto.builder()
                    .userId(users.getUserId())
                    .name(users.getName())
                    .isFollow(isFollow)
                    .profileImgUrl(users.getProfileImgUrl())
                    .build();
            userList.add(dto);

        }

        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("follower", userList);
        return responseModel;
    }

    @Operation(summary="팔로잉 목록",description = "팔로잉 목록 가져오기")
//    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @GetMapping("/followings/{userId}")
    public ResponseModel getFollowing(@PathVariable Long userId) {
        User loginUser = AuthUtil.getAuthenticationInfo();
        List<User> following = followService.getFollowingForUser(userId);
        List<FollowDto> userList = new ArrayList<>();
        boolean isFollow;
        for(User users: following)
        {

            if(loginUser==null)
            {
                isFollow = false;
            }
            else {
                isFollow = followService.isFollow(loginUser, users);
            }
            FollowDto dto = FollowDto.builder()
                    .userId(users.getUserId())
                    .name(users.getName())
                    .isFollow(isFollow)
                    .profileImgUrl(users.getProfileImgUrl())
                .build();
           userList.add(dto);


        }
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("following", userList);
        return responseModel;
    }

}