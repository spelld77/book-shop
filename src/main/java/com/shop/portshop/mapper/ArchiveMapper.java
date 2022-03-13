package com.shop.portshop.mapper;

import com.shop.portshop.vo.BoardVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("ArchiveMapper")
public interface ArchiveMapper {

    void insertFileLocation(@Param("list") List<String> filesLocation, @Param("boardNo") Long boardNo);
    List<String> selectBoardFileNames(@Param("boardNo") long boardNo);
}
