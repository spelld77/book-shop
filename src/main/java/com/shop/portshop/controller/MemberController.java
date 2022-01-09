package com.shop.portshop.controller;

import com.shop.portshop.service.MemberService;
import com.shop.portshop.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MemberController {

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @GetMapping("/login.html")
    public String moveLoginPage(){
        return "/aranoz/login";
    }

    @GetMapping("/join")
    public String moveJoinPage(){
        return "/my_template/member_join";
    }

    @PostMapping("/join")
    public String joinMember(@ModelAttribute MemberVO member){
        boolean result = memberService.addMember(member);

        return "/aranoz/login";
    }

    @ResponseBody
    @GetMapping("/checkUniqueId")
    public String checkUniqueId(@RequestParam("inputId") String inputId){
        boolean result = memberService.checkUniqueId(inputId);
        if(result){
            return "unique";
        }
        return "duplicated";
    }
}
