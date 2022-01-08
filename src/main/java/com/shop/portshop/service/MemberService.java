package com.shop.portshop.service;

import com.shop.portshop.mapper.MemberMapper;
import com.shop.portshop.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private MemberMapper memberMapper;

    @Autowired
    public MemberService(MemberMapper memberMapper){
        this.memberMapper = memberMapper;
    }

    public boolean addMember(MemberVO member){
        return memberMapper.addMemberToDB(member);
    }
}
