package com.shop.portshop.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 로그인 되어있는지 확인, redirect
        String admin = (String) request.getSession().getAttribute("admin");
        if(admin == null){
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
