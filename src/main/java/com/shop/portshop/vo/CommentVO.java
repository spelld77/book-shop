package com.shop.portshop.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter
@Builder
public class CommentVO {
    private long no; //pk
    private String writer;
    private String content;
    private LocalDateTime regDate;
    private long board;
    private long grp;
    private int lft; //nested set 사용
    private int rgt;
    private int level; // 계층

}
