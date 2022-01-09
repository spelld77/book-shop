package com.shop.portshop.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@MapperScan(basePackages = "com.shop.portshop.mapper")
public class WebMvcConfig implements WebMvcConfigurer {
}
