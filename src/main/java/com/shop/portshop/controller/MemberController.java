package com.shop.portshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.shop.portshop.oauth2.NaverLoginBO;
import com.shop.portshop.service.MemberService;
import com.shop.portshop.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController{

    private final MemberService memberService;
    private final NaverLoginBO naverLoginBO;
    private final AuthenticationManager authenticationManager;


    @GetMapping("/login")
    public String moveLoginPage(
            @CookieValue(value="rememberIdCookie", required = false) Cookie rememberIdCookie, Model model, HttpSession session){

        // 리멤버id 체크(id, 체크박스)
        if(rememberIdCookie != null){
            model.addAttribute("id", rememberIdCookie.getValue());
            model.addAttribute("remember_id", true);
        }

        //네이버 로그인 url생성
        String naverAuthUrl = naverLoginBO.getAuthorizationUrl(session);
        model.addAttribute("naverUrl", naverAuthUrl);

        return "/member_templates/login";
    }

    @GetMapping("/join")
    public String moveJoinPage(){
        return "/member_templates/member_register";
    }

    @PostMapping("/join")
    public String joinMember(@ModelAttribute MemberVO member){

        boolean result = memberService.addMember(member);
        return "/member_templates/login";
    }

    @GetMapping("/member/modify")
    public String modifyMemberPage( Model model, Principal principal){
        String userId = (null == principal) ? "" : principal.getName();

        if(userId == null){
            return "redirect:/login";
        }

        MemberVO member = memberService.getMember(userId);
        model.addAttribute("member", member);
        return "/member_templates/member_modify";
    }

    @PostMapping("/member/modify")
    public String modifyMember( @ModelAttribute("member") MemberVO member){
        boolean result = memberService.modifyMember(member);
        return "redirect:/";
    }

    // db에 있는id인지 체크
    @ResponseBody
    @GetMapping("/checkUniqueId")
    public String checkUniqueId(@RequestParam("inputId") String inputId){
        boolean result = memberService.checkUniqueId(inputId);
        if(result){
            return "unique";
        }
        return "duplicated";
    }

    // 네이버 로그인 콜백
    @GetMapping("/login/oauth2/code/naver")
    public String naverLogin(@RequestParam String state, @RequestParam(required = false) String code,
                             @RequestParam(required = false) String error,
                             HttpSession session) throws IOException, ExecutionException, InterruptedException {

        if(null != error){
            return "redirect:/";
        }

        OAuth2AccessToken accessToken = naverLoginBO.getAccessToken(session, code, state);
        String userProfile = naverLoginBO.getUserProfile(accessToken);
        memberService.processNaverLogin(userProfile);
        return "redirect:/";
    }

}
