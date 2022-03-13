package com.shop.portshop.service;

import com.shop.portshop.commons.Pagination;
import com.shop.portshop.commons.StorageService;
import com.shop.portshop.mapper.ArchiveMapper;
import com.shop.portshop.mapper.BoardMapper;
import com.shop.portshop.vo.BoardVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BoardService {

    private BoardMapper boardMapper;
    private StorageService storageService;
    private ArchiveMapper archiveMapper;

    @Autowired
    public BoardService(BoardMapper boardMapper,
                        StorageService storageService,
                        ArchiveMapper archiveMapper){
        this.boardMapper = boardMapper;
        this.storageService = storageService;
        this.archiveMapper = archiveMapper;
    }

    public List<BoardVO> getBoardList(Pagination pagination){
        long startRecord = (pagination.getNowPage()-1) * pagination.getRecordSize();
        List<BoardVO> boards = boardMapper.selectBoards(startRecord, pagination.getRecordSize());
        return boards;
    }


    public boolean addBoard(String title, String content, MultipartFile[] files) {
        BoardVO boardVO = new BoardVO();
        boardVO.setTitle(title);
        boardVO.setContent(content);
        boardMapper.insertOneBoard(boardVO);

        log.info("boardNo: " + boardVO.getNo());
        log.debug("file length: " + files.length);

        // 파일 업로드 (있을때)
        if(files[0].getOriginalFilename().length() > 0){
            long boardNo = boardVO.getNo();
            storageService.uploadFiles(files, boardNo);
        }
        return true;
    }

    public int getAllBoardCount() {
        return boardMapper.countAllBoard();
    }

    public Map<String, Object> viewOneBoard(long boardNo) {
        boardMapper.increaseViews(boardNo);
//        BoardVO board = boardMapper.selectOneBoard(boardNo);
//        return board;

        Map<String, Object> resultInfo = new HashMap<>();
        BoardVO board = boardMapper.selectOneBoard(boardNo);
        //파일 이미지
        List<String> locations = archiveMapper.selectBoardFileNames(boardNo);
        resultInfo.put("board", board);
        resultInfo.put("fileNames", locations);
        return resultInfo;
    }
}
