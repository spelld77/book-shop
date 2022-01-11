package com.shop.portshop.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 로그인 되어있는지 확인, redirect
        String user = (String) request.getSession().getAttribute("user");
        log.info("LoginInterceptor user : " + user);
        if(user == null){
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
