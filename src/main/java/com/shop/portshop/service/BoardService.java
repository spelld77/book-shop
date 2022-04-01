package com.shop.portshop.service;

import com.shop.portshop.commons.Pagination;
import com.shop.portshop.commons.StorageService;
import com.shop.portshop.mapper.ArchiveMapper;
import com.shop.portshop.mapper.BoardMapper;
import com.shop.portshop.vo.BoardVO;
import com.shop.portshop.vo.CommentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Slf4j
@Service
public class BoardService {

    @Value("${spring.servlet.multipart.location}")
    String archivePath; // 이미지 저장 폴더

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


    public boolean addBoard(String writer, String title, String content, MultipartFile[] files) {
        BoardVO boardVO = new BoardVO();
        boardVO.setWriter(writer);
        boardVO.setTitle(title);
        boardVO.setContent(content);
        boardMapper.insertOneBoard(boardVO);

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

        Map<String, Object> resultInfo = new HashMap<>();
        BoardVO board = boardMapper.selectOneBoard(boardNo);
        List<CommentVO> comments = boardMapper.selectComments(boardNo);
        //파일 이미지
        List<String> locations = archiveMapper.selectBoardFileNames(boardNo);

        resultInfo.put("board", board);
        resultInfo.put("fileNames", locations);
        resultInfo.put("comments", comments);
        return resultInfo;
    }

    public boolean createRootComment(long boardNo, String writer, String content) {

        CommentVO commentVO = CommentVO.builder()
                .board(boardNo)
                .writer(writer)
                .content(content)
                .build();

       return boardMapper.insertNewRootComment(commentVO);
    }

    // 답글 작성
    @Transactional
    public boolean createCommentReply(long boardNo, String writer, String content, long grp, int lft, int rgt, int level) {

        log.info("BoardService: createCommentReply");
        CommentVO commentVO = CommentVO.builder()
                .board(boardNo)
                .writer(writer)
                .content(content)
                .grp(grp)
                .lft(lft)
                .rgt(rgt)
                .level(level)
                .build();


        // 부모와 자식노드의 max(rgt)
        int lastChildRgt = boardMapper.selectLastChildRgt(commentVO);
//
//        // 기존 노드의 lft, rgt를 증가
        boardMapper.updateIncreaseCommentLft(commentVO, lastChildRgt);
        boardMapper.updateIncreaseCommentRgt(commentVO, lastChildRgt);

        // 새로운 노드 추가
        boardMapper.insertNewChildComment(commentVO, lastChildRgt);

        return true;
    }

    @Transactional
    public void deleteBoard(long boardNo) {

        //파일 삭제
        List<String> fileNames = boardMapper.selectAllFiles(boardNo);
        for(String fileName : fileNames){
           try{
               Files.delete(Path.of(archivePath,"/",fileName));
           } catch (IOException e){
               log.error("file delete failed", e);
           }

        }
        int deletedBoardNum = boardMapper.deleteBoard(boardNo);
        int deletedCommentNum = boardMapper.deleteAllComment(boardNo);
        int deletedFilesNum = boardMapper.deleteAllFileNames(boardNo);

    }


    public BoardVO getEditBoardForm(long boardNo) {

        return boardMapper.selectOneBoard(boardNo);
    }

    public boolean editBoard(long boardNo, String title, String content) {

        boardMapper.updateBoard(boardNo, title, content);

        return true;
    }

    // 게시판 검색 기능
    public List<Object> searchBoard(String target, String keyword, int nowPage) {

        List<Object> obj = new ArrayList<>();

        // 검색한 전체 게시물
        List<BoardVO> tempList = boardMapper.selectBoardBySearch(target, keyword);

        int allBoardCount = 0;
        List<BoardVO> boardList = null;
        boardList = new ArrayList<>();

        if(null != tempList){
            allBoardCount = tempList.size(); //글의 전체 갯수
        }

        // 게시물 수를 페이지 수 맞게 나누어 저장
        for(int i = (nowPage - 1) * 10, maxNum = nowPage * 10; i < maxNum; i++){
            if(allBoardCount <= i){
                break;
            }
            boardList.add(tempList.get(i));
        }

        Pagination pagination = new Pagination(allBoardCount, nowPage, 10, 5);
        obj.add(boardList);
        obj.add(pagination);

        return obj;
    }
}
