package com.shop.portshop.mapper;

import com.shop.portshop.vo.MemberVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("MemberMapper")
public interface MemberMapper {
    public boolean addMemberToDB(MemberVO member);

    public MemberVO getMember(String inputId);

    public String getPassword(String id);

    public boolean modifyMember(MemberVO member);

    int updateResetToken(@Param("id") String id, @Param("token") String token);

    int updatePassword(@Param("id") String id, @Param("pwd") String pwd);
}
