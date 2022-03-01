package com.shop.portshop.controller;

import com.shop.portshop.commons.Pagination;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class BoardControllerTest {

    @Autowired
    private BoardController boardController;

    @Test
    void contextLoad(){
        assertThat(boardController).isNotNull();
    }


}