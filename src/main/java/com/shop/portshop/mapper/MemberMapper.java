package com.shop.portshop.mapper;

import com.shop.portshop.vo.MemberVO;
import org.springframework.stereotype.Repository;

@Repository("MemberMapper")
public interface MemberMapper {
    public boolean addMemberToDB(MemberVO member);

    public MemberVO getMember(String inputId);

    public String getPassword(String id);
}
