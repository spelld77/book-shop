package com.shop.portshop.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class BoardVO {
    private long no;
    private String title;
    private String content;
    private String writer;
    private int views;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
}
