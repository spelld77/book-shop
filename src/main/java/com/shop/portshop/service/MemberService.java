package com.shop.portshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.portshop.commons.MailHandler;
import com.shop.portshop.mapper.MemberMapper;
import com.shop.portshop.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.websocket.MessageHandler;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MemberService implements UserDetailsService {

    private MemberMapper memberMapper;

    @Autowired
    public MemberService(MemberMapper memberMapper){
        this.memberMapper = memberMapper;
    }

    public boolean addMember(MemberVO member){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        member.setPw(passwordEncoder.encode(member.getPw()));
        passwordEncoder = null;
        return memberMapper.addMemberToDB(member);
    }

    // id가 중복인지 체크
    public boolean checkUniqueId(String inputId) {
        // 입력이 공백일때 처리
        if(inputId.strip() == ""){
            return false;
        }
        MemberVO member = memberMapper.getMember(inputId);

        if(member == null){
            return true;
        }
        return false;
    }

//    public boolean login(String id, String pw) {
//
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        pw = passwordEncoder.encode(pw);
//        passwordEncoder = null;
//
//        String realPass = memberMapper.getPassword(id);
//
//        if(pw.equals(realPass)){
//            return true;
//        }
//        return false;
//    }

    public MemberVO getMember(String userId) {
        return memberMapper.getMember(userId);
    }

    public boolean modifyMember(MemberVO member) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        member.setPw(passwordEncoder.encode(member.getPassword()));
        passwordEncoder = null;
        return memberMapper.modifyMember(member);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberVO member = memberMapper.getMember(username);
        if(member == null){
            throw new UsernameNotFoundException("Member Not Found");
        }

        UserDetails user = User
                .withUsername(member.getUsername())
                .password(member.getPassword())
                .authorities("USER").build();

        return user;
    }

    public boolean addMemberOfNaver(String id, String name, String email){

        MemberVO member = new MemberVO(id, null, name, email, "","");
        return memberMapper.addMemberToDB(member);
    }

    // 네이버 처리 로직
    public void processNaverLogin(String userProfile) throws JsonProcessingException {

        Map<String, Object> map = new ObjectMapper().readValue(userProfile, HashMap.class);
        HashMap<String, String> response = (LinkedHashMap<String, String>) map.get("response");

        String id = response.get("id");
        String name = response.get("name");
        String email = response.get("email");

        //id없으면 회원가입
        MemberVO member = getMember(id);
        if(member == null){
            addMemberOfNaver(id, name, email);
            member = getMember(id);
        }

        //manual login
        Authentication authentication = new UsernamePasswordAuthenticationToken(id, null, AuthorityUtils.createAuthorityList("USER"));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    // 리셋토큰 설정
    public String changeAndGetResetToken(String id) {

        String token = UUID.randomUUID().toString();

        //리셋토큰 update
        int result = memberMapper.updateResetToken(id, token);

        return token;

    }

    public int changePassword(String id, String purePwd) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePwd = passwordEncoder.encode(purePwd);

        return memberMapper.updatePassword(id, encodePwd);

    }
}
