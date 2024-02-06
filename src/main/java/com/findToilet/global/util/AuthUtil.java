package com.findToilet.global.util;

import com.findToilet.global.exception.CustomException;
import com.findToilet.global.exception.ExceptionCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    public static long getMemberId() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication == null) {
            throw new CustomException(ExceptionCode.UNKNOWN_USER);
        }

        long memberId = Long.parseLong(authentication.getName().toString());

        return memberId;
    }
}