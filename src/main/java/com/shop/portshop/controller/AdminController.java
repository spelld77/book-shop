package com.shop.portshop.controller;

import com.shop.portshop.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/admin/*")
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/main")
    public String mainPage(){
        return "/admin_templates/admin_main";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session){
        if(session.getAttribute("admin") != null){
            return "redirect:/admin/main";
        }
        return "/admin_templates/admin_login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String id, @RequestParam String pw, HttpSession session){
        log.info("input id = " + id + ",  pw: " + pw);
        boolean result = adminService.login(id, pw);
        if(!result){
            log.info("login fail");
            return "redirect:/admin/login";
        }
        log.info("login success");
        session.setAttribute("admin", id);
        return "redirect:/admin/main";
    }

    @GetMapping("/logout")
    public String logoutPage(){
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session){

        session.removeAttribute("admin");
        return "redirect:/";
    }

    @GetMapping("/productRegister")
    public String productRegister(){

        return "/admin_templates/admin_product_register";
    }
}
