package com.shop.portshop.service;

import com.shop.portshop.commons.Pagination;
import com.shop.portshop.mapper.BoardMapper;
import com.shop.portshop.vo.BoardVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BoardService {

    private BoardMapper boardMapper;

    @Autowired
    public BoardService(BoardMapper boardMapper){
        this.boardMapper = boardMapper;
    }

    public List<BoardVO> getBoardList(Pagination pagination){
        long startRecord = (pagination.getNowPage()-1) * pagination.getRecordSize();
        List<BoardVO> boards = boardMapper.selectBoards(startRecord, pagination.getRecordSize());
        return boards;
    }


    public boolean addBoard(String title, String content) {
        return boardMapper.insertOneBoard(title, content);
    }

    public int getAllBoardCount() {
        return boardMapper.countAllBoard();
    }

    public BoardVO viewOneBoard(long boardNo) {
        boardMapper.increaseViews(boardNo);
        BoardVO board = boardMapper.selectOneBoard(boardNo);
        return board;
    }
}
