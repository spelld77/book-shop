package com.shop.portshop.config;

import com.shop.portshop.interceptor.AdminInterceptor;
import com.shop.portshop.interceptor.LoggerInterceptor;
import com.shop.portshop.interceptor.LoginInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@MapperScan(basePackages = "com.shop.portshop.mapper")
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor( new LoggerInterceptor())
                .excludePathPatterns("/img/**", "/css/**", "/js/**", "/webfonts/**", "/fonts/**");

        //로그인 정보 인터셉터
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/member/**");

        //관리자 정보 인터셉터
        registry.addInterceptor(new AdminInterceptor()).addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login");
    }
}
