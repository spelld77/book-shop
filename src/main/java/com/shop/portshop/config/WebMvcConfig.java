package com.shop.portshop.config;

import com.shop.portshop.interceptor.LoggerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@MapperScan(basePackages = "com.shop.portshop.mapper")
public class WebMvcConfig implements WebMvcConfigurer {

    // 외부의 파일 저장 경로
    @Value("${spring.servlet.multipart.location}")
    private String archivePath;

    // 외부 파일 저장 uri path
    @Value("${resources.uri_path}")
    private String uriPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor( new LoggerInterceptor())
                .excludePathPatterns("/img/**", "/css/**", "/js/**", "/webfonts/**", "/fonts/**", "/upload/**");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        // 외부 이미지 파일
        registry.addResourceHandler(uriPath + "**")
                .addResourceLocations("file:///" + archivePath + "/");

    }
}
