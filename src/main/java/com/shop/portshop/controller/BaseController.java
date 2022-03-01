package com.shop.portshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseController {

    @GetMapping("/")
    public String moveMain(){
        return "/member_templates/index";
    }

    @GetMapping("/index")
    public String moveMain2(){
        return "/member_templates/index";
    }

    @GetMapping("/feature")
    public String moveFeature(){
        return "/aranoz/feature";
    }

//    @GetMapping("/board")
//    public String moveBoard(){
//        BoardController
//        return "/board_templates/board";
//    }




}
