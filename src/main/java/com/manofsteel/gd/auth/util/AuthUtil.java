package com.manofsteel.gd.auth.util;

import com.manofsteel.gd.type.dto.UserPrincipal;
import com.manofsteel.gd.type.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class AuthUtil {

    public static User getAuthenticationInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            return null;
        }
        log.info("AuthUtil Authentication: {}", authentication);
        return ((UserPrincipal) authentication.getPrincipal()).toUser();
    }


    public static Long getAuthenticationInfoUserId() {
        if(SecurityContextHolder.getContext().getAuthentication() == null || !(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserPrincipal)) {
            return null;
        }
        return getAuthenticationInfo().getUserId();
    }
}