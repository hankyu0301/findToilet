package com.findToilet.global.auth.handler.login;

import com.findToilet.global.auth.error.AuthenticationError;
import com.findToilet.global.exception.CustomException;
import com.findToilet.global.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MemberAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        log.error("### Authentication failed: {}", exception.getMessage());
        log.error("### Authentication failed: {}", exception.getClass().getName());
        AuthenticationError.sendErrorResponse(response, getCustomException(exception));
    }

    private CustomException getCustomException(AuthenticationException exception) {
        if (exception.getClass().equals(InternalAuthenticationServiceException.class))
            return new CustomException(ExceptionCode.NON_REGISTERED_USER);
        else if (exception.getClass().equals(BadCredentialsException.class))
            return new CustomException(ExceptionCode.PASSWORD_INVALID);
        else
            return new CustomException(ExceptionCode.UNKNOWN_USER);

    }


}
