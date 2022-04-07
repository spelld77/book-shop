package com.shop.portshop.service;

import com.shop.portshop.mapper.MemberMapper;
import com.shop.portshop.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    //네이버로 가입
    public boolean addMemberOfNaver(String id, String name, String email){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pw = passwordEncoder.encode(email);

        MemberVO member = new MemberVO(id, pw, name, email, "","");
        passwordEncoder = null;

        return memberMapper.addMemberToDB(member);
    }
}
