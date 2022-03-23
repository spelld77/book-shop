package com.shop.portshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

@SpringBootApplication
public class PortshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortshopApplication.class, args);
	}

}
