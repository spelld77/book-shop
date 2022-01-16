package com.shop.portshop.mapper;

import org.springframework.stereotype.Repository;

@Repository("AdminMapper")
public interface AdminMapper {
    public String getAdminPw(String id);
}
