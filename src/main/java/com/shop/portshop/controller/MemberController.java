package com.shop.portshop.controller;

import com.shop.portshop.service.MemberService;
import com.shop.portshop.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class MemberController {

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String moveLoginPage(){
        return "/member_templates/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String id, @RequestParam String pw, HttpSession session, Model model){

        boolean loginSuccess = memberService.login(id,pw);

        if(loginSuccess){
            session.setAttribute("user", id);
            return "redirect:/";
        }
        model.addAttribute("wrongPassword", true);
        return "/member_templates/login";

    }
    @PostMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping("/join")
    public String moveJoinPage(){
        return "/member_templates/join_register";
    }

    @PostMapping("/join")
    public String joinMember(@ModelAttribute MemberVO member){
        boolean result = memberService.addMember(member);

        return "/member_templates/login";
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
