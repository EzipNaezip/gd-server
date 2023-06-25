package com.manofsteel.gd.controller;

import com.manofsteel.gd.auth.jwt.AuthToken;
import com.manofsteel.gd.auth.util.AuthUtil;
import com.manofsteel.gd.exception.NotAllowValueException;
import com.manofsteel.gd.exception.PermissionDeniedException;
import com.manofsteel.gd.repository.UserInfoSetRepository;
import com.manofsteel.gd.repository.UserRepository;
import com.manofsteel.gd.service.FollowService;
import com.manofsteel.gd.service.PostService;
import com.manofsteel.gd.service.UserService;
import com.manofsteel.gd.type.dto.ResponseModel;
import com.manofsteel.gd.type.dto.UserDto;
import com.manofsteel.gd.type.entity.Follow;
import com.manofsteel.gd.type.entity.User;
import com.manofsteel.gd.type.vo.PostVo;
import com.manofsteel.gd.type.vo.UserVo;
import com.manofsteel.gd.util.EntityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User")
@Slf4j
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final UserRepository userRepository;

    private final UserInfoSetRepository userInfoSetRepository;
    private final FollowService followService;

    @Operation(summary = "Firebase 유저 연동", description = "Firebase를 통해 가입한 유저 MariaDB 연동" +
            "\n PathVariable : uid(Firebase uid)")
//    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/firebase/{uid}", method = RequestMethod.POST)
    public ResponseModel saveUserFromFirebase(@PathVariable String uid) {
        User user = userService.saveUserFromFirebase(uid);
        AuthToken authToken = userService.getUserToken(user);
        log.debug("AuthToken Data : {}", authToken.getToken());
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("user", user);
        responseModel.addData("jwt", authToken.getToken());
        return responseModel;
    }

    @Operation(summary = "유저 정보 조회", description = "유저 정보 조회" + "PathVariable : userId(조회할 유저의 userId)")
//    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/info/{userId}")
    public ResponseModel userInfo(
            @PathVariable("userId") Long userId
    ) {
       User LoginUser= AuthUtil.getAuthenticationInfo();
       User user = EntityUtil.findUser(userRepository,userId);
        boolean isMe = false;
        boolean isFollow =false;
        if(LoginUser!=null)
        {
            isFollow=followService.isFollow(LoginUser,user);
            if(LoginUser.getUserId()==userId)
                isMe=true;
        }

        UserVo userVo = new UserVo(userService.findUserById(userId), isMe,isFollow);
        List<PostVo> myPosts = postService.getPostsByWriterId(userId);
        List<PostVo> bookmarkedPosts = postService.getBookmarkedPostsByUserId(userId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("user",userVo);
        responseModel.addData("myPosts", myPosts);
        responseModel.addData("bookmarkedPosts", bookmarkedPosts);
        responseModel.addData("loginUser", LoginUser);
        return responseModel;
    }

    @Operation(summary = "유저 정보 조회(관리자)", description = "특정 유저 정보 조회" + "PathVariable : userId(조회할 유저의 userId)")
//    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/admin/{userId}", method = RequestMethod.GET)
    public ResponseModel userGetAsAdmin(
            @PathVariable("userId") Long userId
    ) {
        User user = userService.findUserById(userId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("user", user);
        return responseModel;
    }

    @Operation(summary = "유저 정보 리스트 조회", description = "모든 유저 정보 조회" +
            "\n RequestParam : start(시작 인덱스), display(마지막 인덱스)")
//    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel userList(
            @RequestParam(name = "start", required = false, defaultValue = "0") long start,
            @RequestParam(name = "display", required = false, defaultValue = "20") long display
    ) {
        List<User> userList = userService.findAll(start, display);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("userList", userList);
        return responseModel;
    }

    @Operation(summary = "유저 업데이트", description = "유저 정보 업데이트" +
            "<br> RequestBody : UserDto   private Long userId;<br>" +
            "    String name;<br>" +
            "    String description;<br>" +
            "    String profileImgUrl;<br>")
   // @Secured({"ROLE_USER"})
    @PutMapping("/update")
    public ResponseModel userUpdate(
            @RequestBody UserDto userDto
    ) throws NotAllowValueException {
        Long userId = AuthUtil.getAuthenticationInfoUserId();
        //Long userId = AuthUtil.getAuthenticationInfoUserId();

        User user= EntityUtil.findUser(userRepository,userId);
        userDto.setUserId(user.getUserId());
        userService.updateUser(userDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "유저 삭제(일반 유저)", description = "회원 탈퇴" + "PathVariable : userId(삭제할 유저의 userId)")
//    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @DeleteMapping("/delete/{userId}")
    public ResponseModel userDeleteAsUser(
            @PathVariable("userId") Long userId
    ) throws PermissionDeniedException {
        if (!userId.equals(AuthUtil.getAuthenticationInfoUserId())) {
            throw new PermissionDeniedException();
        }

        followService.FollowUserDelete(userId);
        userInfoSetRepository.deleteById(userId);
        userService.deleteUserById(userId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}