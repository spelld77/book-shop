package com.shop.portshop.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

@Getter @Setter
public class MemberVO implements UserDetails {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String auth = "";

        if(grade == 0){
            auth = "ADMIN";
        } else{
            auth = "USER";
        }
        return Collections.singleton(new SimpleGrantedAuthority(auth));

    }

    @Override
    public String getPassword() {
        return this.pw;
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
