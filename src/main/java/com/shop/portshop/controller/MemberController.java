package com.shop.portshop.controller;

import com.shop.portshop.service.MemberService;
import com.shop.portshop.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@Slf4j
@Controller
public class MemberController {

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String moveLoginPage(
            @CookieValue(value="rememberIdCookie", required = false) Cookie rememberIdCookie, Model model){
        // 쿠키(id, 체크박스)
        if(rememberIdCookie != null){
            model.addAttribute("id", rememberIdCookie.getValue());
            model.addAttribute("remember_id", true);
        }

        return "/member_templates/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String id, @RequestParam String pw, @RequestParam(required = false) boolean remember_id,
                        HttpSession session, HttpServletResponse response, Model model){

        boolean loginSuccess = memberService.login(id,pw);

        if(!loginSuccess){
            return "redirect:/login";
        }
        // 로그인 세션, 쿠키 설정
        session.setAttribute("user", id);
        if(remember_id){
            Cookie rememberIdCookie = new Cookie("rememberIdCookie", id);
            rememberIdCookie.setPath("/login");
            rememberIdCookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(rememberIdCookie);

        }


        return "redirect:/";
    }
    @PostMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping("/join")
    public String moveJoinPage(){
        return "member_register";
    }

    @PostMapping("/join")
    public String joinMember(@ModelAttribute MemberVO member){
        boolean result = memberService.addMember(member);

        return "/member_templates/login";
    }

    @GetMapping("/member/modify")
    public String modifyMemberPage( Model model, HttpSession session){
        String userId = (String) session.getAttribute("user");
        MemberVO member = memberService.getMember(userId);
        log.debug(member.toString());
        model.addAttribute("member", member);
        return "/member_templates/member_modify";
    }

    @PostMapping("/member/modify")
    public String modifyMember( @ModelAttribute("member") MemberVO member){
        boolean result = memberService.modifyMember(member);
        return "redirect:/";
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
