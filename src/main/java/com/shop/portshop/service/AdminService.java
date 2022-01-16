package com.shop.portshop.service;

import com.shop.portshop.mapper.AdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminService {

    private AdminMapper adminMapper;

    @Autowired
    public AdminService(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    public boolean login(String inputId, String inputPw){

        String realPw = adminMapper.getAdminPw(inputId);

        if(inputPw.equals(realPw)){
            return true;
        }
        return false;
    }
}
