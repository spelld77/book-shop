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

//    @Autowired
//    public MemberController(MemberService memberService, NaverLoginBO naverLoginBO){
//        this.memberService = memberService;
//        this.naverLoginBO = naverLoginBO;
//    }

    @GetMapping("/login")
    public String moveLoginPage(
            @CookieValue(value="rememberIdCookie", required = false) Cookie rememberIdCookie, Model model, HttpSession session){
        // 쿠키(id, 체크박스)
        if(rememberIdCookie != null){
            model.addAttribute("id", rememberIdCookie.getValue());
            model.addAttribute("remember_id", true);
        }
        //네이버 로그인 관련
        String naverAuthUrl = naverLoginBO.getAuthorizationUrl(session);
        log.info("naverAuthURL: " + naverAuthUrl);
        model.addAttribute("naverUrl", naverAuthUrl);

        return "/member_templates/login";
    }

//    @PostMapping("/login")
//    public String login(@RequestParam String id, @RequestParam String pw, @RequestParam(required = false) boolean remember_id,
//                        HttpSession session, HttpServletResponse response, Model model){
//
//        boolean loginSuccess = memberService.login(id,pw);
//
//        if(!loginSuccess){
//            return "redirect:/login";
//        }
//        // 로그인 세션, 쿠키 설정
//        session.setAttribute("user", id);
//        if(remember_id){
//            Cookie rememberIdCookie = new Cookie("rememberIdCookie", id);
//            rememberIdCookie.setPath("/login");
//            rememberIdCookie.setMaxAge(60 * 60 * 24 * 30);
//            response.addCookie(rememberIdCookie);
//
//        }


//        return "redirect:/";
//    }
//    @PostMapping("/logout")
//    public String logout(HttpSession session){
//        session.removeAttribute("user");
//        return "redirect:/";
//    }

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
//    public String modifyMemberPage( Model model, HttpSession session){
    public String modifyMemberPage( Model model, Principal principal){
//        String userId = (String) session.getAttribute("user");
        String userId = (null == principal) ? "" : principal.getName();

        if(userId == null){
            return "redirect:/login";
        }

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
    // 네이버 로그인 콜백
//    @ResponseBody
    @GetMapping("/login/oauth2/code/naver")
    public String naverLogin(@RequestParam String state, @RequestParam String code,
                             HttpSession session, Model model,
                             HttpServletRequest request) throws IOException, ExecutionException, InterruptedException {
        OAuth2AccessToken accessToken = naverLoginBO.getAccessToken(session, code, state);
        String userProfile = naverLoginBO.getUserProfile(accessToken);
//        model.addAttribute("result", userProfile);
        log.info("profile: " +userProfile);

        Map<String, Object> map = new ObjectMapper().readValue(userProfile, HashMap.class);
        HashMap<String, String> response = (LinkedHashMap<String, String>) map.get("response");

        log.info("id: " + response.get("id"));
        log.info("name: " + response.get("name"));
        log.info("email: " + response.get("email"));
        log.info("resultcode: " + map.get("resultcode"));
        String id = response.get("id");
        String name = response.get("name");
        String email = response.get("email");
        //id없으면 회원가입
        MemberVO member = memberService.getMember(id);
        if(member == null){
            memberService.addMemberOfNaver(id, name, email);
            member = memberService.getMember(id);
        }
        //stackov
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(id, email);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
//        HttpSession httpSession = request.getSession(true);
//        httpSession.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);


        //로그인
        session.setAttribute("user", id);
        return "redirect:/";
//        return userProfile;
    }

//    @GetMapping("/login/naver")
//    public String naverLoginPage(HttpSession session, Model model){
//        String naverAuthUrl = naverLoginBO.getAuthorizationUrl(session);
//        log.info("naverAuthURL: " + naverAuthUrl);
//        model.addAttribute("url", naverAuthUrl);
//        return
//    }

}
