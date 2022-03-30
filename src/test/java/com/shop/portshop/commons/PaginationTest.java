package com.shop.portshop.commons;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PaginationTest {

    @Test
    void paginationNowRangeTest() {
        // 현재 페이지 범위 유효성 검사
        int totalRecord = 37;
        // nowPage(parameter) > 전체페이지수 일때
        Pagination[] paginations = {
                new Pagination(totalRecord, -100, 10, 5),
                new Pagination(totalRecord, 100, 10, 5)
        };
        for(int i=0; i < paginations.length; i++){
            assertThat(paginations[i].getNowPage())
                    .isGreaterThanOrEqualTo(1)
                    .isLessThanOrEqualTo(paginations[i].getTotalPages());
        }

    }
    @Test
    void paginationEndPageNumTest(){
        int[] endPageNum =new int[10];
        int totalRecord = 100;

        for (int i=0; i<10; i++){
            totalRecord += 1;
            int nowPage = 1;
            int recordPerPage = 10;
            int visiblePageSize = 10;
            Pagination pagination = new Pagination(totalRecord, nowPage, recordPerPage, visiblePageSize);
            endPageNum[i] = pagination.getTotalPages();
        }
        Arrays.stream(endPageNum).forEach(endPage ->{
            assertThat(endPage).isEqualTo(11);
        });

    }

}