package com.shop.portshop.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //remember id 체크박스
        String rememberId = request.getParameter("remember_id");
        //remember id 쿠키추가
        if(null != rememberId){
            String userId = authentication.getName();
            Cookie rememberIdCookie = new Cookie("rememberIdCookie", userId);
            rememberIdCookie.setPath("/login");
            rememberIdCookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(rememberIdCookie);
        }

        response.sendRedirect("/");


    }
}
