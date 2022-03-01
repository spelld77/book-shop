package com.shop.portshop.mapper;

import com.shop.portshop.vo.BoardVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("BoardMapper")
public interface BoardMapper {
    public List<BoardVO> selectBoards(@Param("startRecord") long startRecord, @Param("recordPerPage") int recordPerPage);

    public boolean insertOneBoard(@Param("title") String title, @Param("content") String content);
    public int countAllBoard();
}
