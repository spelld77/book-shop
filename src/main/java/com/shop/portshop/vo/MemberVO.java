package com.shop.portshop.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class MemberVO {
    private String id;
    private String pw;
    private String name;
    private String email;
    private String tel;
    private String address;
    private Date regDate;
    private int grade;

    public MemberVO(){}

    public MemberVO(String id, String pw, String name, String email, String tel, String address){
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.address = address;
    }

    @Override
    public String toString() {
        return "MemberVO{" +
                "id='" + id + '\'' +
                ", pw='" + pw + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                ", regDate=" + regDate +
                ", grade=" + grade +
                '}';
    }
}
