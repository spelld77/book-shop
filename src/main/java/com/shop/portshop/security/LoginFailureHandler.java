package com.shop.portshop.security;

import com.shop.portshop.constant.WorkState;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        FlashMap flashMap = new FlashMap();
        flashMap.put("loginFail", true);
        flashMap.put("state", WorkState.FAILURE);
        flashMap.put("message", "로그인에 실패했습니다");
        FlashMapManager flashMapManager = new SessionFlashMapManager();
        flashMapManager.saveOutputFlashMap(flashMap, request, response);
        response.sendRedirect("/login?error=true");
    }
}
