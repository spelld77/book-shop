package com.shop.portshop.controller;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.shop.portshop.constant.WorkState;
import com.shop.portshop.oauth2.NaverLoginBO;
import com.shop.portshop.service.MailService;
import com.shop.portshop.service.MemberService;
import com.shop.portshop.vo.MailDto;
import com.shop.portshop.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.concurrent.ExecutionException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController{

    private final MemberService memberService;
    private final NaverLoginBO naverLoginBO;
    private final MailService mailService;

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
    public String joinMember(@ModelAttribute MemberVO member, RedirectAttributes redirectAttributes){
        boolean state = memberService.addMember(member);

        if(state){
            redirectAttributes.addFlashAttribute("state", WorkState.SUCCESS);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
        } else{
            redirectAttributes.addFlashAttribute("state", WorkState.FAILURE);
            redirectAttributes.addFlashAttribute("message", "회원가입에 실패했습니다.");
        }
        return "redirect:/login";
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
    public String modifyMember( @ModelAttribute("member") MemberVO member, RedirectAttributes redirectAttributes){
        boolean state = memberService.modifyMember(member);

        if(state){
            redirectAttributes.addFlashAttribute("state", WorkState.SUCCESS);
            redirectAttributes.addFlashAttribute("message", "회원정보 변경에 성공했습니다");
        } else{
            redirectAttributes.addFlashAttribute("state", WorkState.FAILURE);
            redirectAttributes.addFlashAttribute("message", "회원정보 변경에 실패했습니다.");
        }

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

    // 비밀번호 재설정 요청 페이지
    @GetMapping("/lostPass")
    public String forgetPasswordPage(){
        return "/member_templates/member_forget";
    }

    // 비밀번호 재설정 요청 제출
    @PostMapping("/forgetPassword")
    public String processForgetPassword(@RequestParam(defaultValue = "") String id, RedirectAttributes redirectAttributes){
        // find member
        MemberVO member = memberService.getMember(id);
        // 등록된 이메일로 비밀번호 재설정 보내기
        if(member != null){

            // reset token 설정
            String resetToken = memberService.changeAndGetResetToken(id);

            //리셋토큰 url만들기----------------------------
            String url = "http://localhost:8080/resetPassword?id=" + id + "&token=" + resetToken;

            // get email
            String findEmail = member.getEmail();

            MailDto mailDto = new MailDto();
            mailDto.setAddress(findEmail);
            mailDto.setTitle("PortShop 패스워드 변경 안내");
            String message = "<h2>안녕하세요. PortShop입니다.</h2>" +
                    "<p>다음페이지에서 비밀번호를 변경하세요<br>" +
                    "<a href=" + url +"> 비밀번호 변경 페이지로 이동</a>" +
                    "<br>감사합니다.";
            mailDto.setMessage(message);

            // send email
            boolean state = mailService.sendEmailForChangingPassword(mailDto);

            if(state){
                redirectAttributes.addFlashAttribute("state", WorkState.SUCCESS);
                redirectAttributes.addFlashAttribute("message", "메일을 전송했습니다. 메일을 확인해주세요");
            } else{
                redirectAttributes.addFlashAttribute("state", WorkState.FAILURE);
                redirectAttributes.addFlashAttribute("message", "메일을 전송하지 못했습니다. 다시 시도해주세요");
            }
        }

        return "redirect:/";
    }

    @GetMapping("/resetPassword")
    public String resetPaswordPage(@RequestParam String id, @RequestParam String token,
                                   Model model){

        //리셋토큰과 id 체크
        MemberVO member = memberService.getMember(id);
        String realToken = member.getResetToken();
        if(!StringUtils.equals(token, realToken)){
            return "redirect:/";
        }

        model.addAttribute("id", id);
        model.addAttribute("token", token);

        return "/member_templates/member_reset_password";
    }

    @PostMapping("/resetPassword")
    public String resetPassword( @RequestParam String id, @RequestParam String token,
                                 @RequestParam String pwd, RedirectAttributes redirectAttributes){



        //리셋토큰과 id 체크
        MemberVO member = memberService.getMember(id);
        String realToken = member.getResetToken();

        boolean state = false;

        // 비밀번호 변경
        if(StringUtils.equals(token, realToken)){
            state = memberService.changePassword(id, pwd);
        }

        if(state){
            redirectAttributes.addFlashAttribute("state", WorkState.SUCCESS);
            redirectAttributes.addFlashAttribute("message", "비밀번호 변경에 성공했습니다");
        } else{
            redirectAttributes.addFlashAttribute("state", WorkState.FAILURE);
            redirectAttributes.addFlashAttribute("message", "비밀번호 변경에 실패했습니다.");
        }

        return "redirect:/";
    }



}
