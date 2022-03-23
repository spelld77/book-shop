package com.shop.portshop.controller;

import com.shop.portshop.commons.Pagination;
import com.shop.portshop.service.BoardService;
import com.shop.portshop.vo.BoardVO;
import com.shop.portshop.vo.CommentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {

    private BoardService boardService;
    private Pagination pagination;
    @Value("${resources.uri_path}")
    private String uploadUriPath;

    @Autowired
    public BoardController(BoardService boardService){
        this.boardService = boardService;
    }

    @GetMapping
    public String displayBoard(
            @RequestParam(name = "page", defaultValue = "1") int nowPage,
            Model model){

        int allBoardCount = boardService.getAllBoardCount();
        pagination = new Pagination(allBoardCount, nowPage, 10, 5);

        List<BoardVO> boardList = boardService.getBoardList(pagination);

        model.addAttribute("boardList",boardList);
        model.addAttribute("pageInfo", pagination);
        return "/board_templates/board";
    }

    @GetMapping("/write")
    public String showWriteBoardPage(){
        return "/board_templates/write";
    }

    @PostMapping("/write")
    public String writeBoardPage(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "files") MultipartFile[] files){

        boolean result = boardService.addBoard(title, content, files);
        return "redirect:/board";
    }

    @GetMapping("/{boardNo}")
    public String viewOneBoard(@PathVariable("boardNo") long boardNo, Model model){

        Map<String, Object> obj = boardService.viewOneBoard(boardNo);

        BoardVO board = (BoardVO) obj.get("board");
        List<String> fileNames = (List<String>)obj.get("fileNames");
        List<CommentVO> comments = (List<CommentVO>)obj.get("comments");

        if(board == null){
            return "redirect:/board";
        }
        model.addAttribute("boardNo", boardNo);
        model.addAttribute("board", board);
        model.addAttribute("comments", comments);
        model.addAttribute("files", fileNames);
        model.addAttribute("uploadUriPath", uploadUriPath);
        return "/board_templates/view";
    }

    //루트 댓글 작성
    @PostMapping("/{boardNo}/comment")
    public String createComment(@PathVariable long boardNo, @RequestParam String writer,
                             @RequestParam String content){
        log.info("BoardController: createComment");
        boolean result = boardService.createRootComment(boardNo, writer, content);
        return "redirect:/board/"+boardNo;

    }

    // 대댓글 작성
//    @PostMapping("/{boardNo}/commentReply")
    @PostMapping("/commentReply")
    public String creatCommentReply(
            @RequestParam long boardNo, @RequestParam String writer,
            @RequestParam long grp,   @RequestParam int lft,
            @RequestParam int rgt,    @RequestParam int level, @RequestParam String content){
        log.info("BoardController: createCommentReply");
        boolean result = boardService.createCommentReply(
                boardNo, writer, content, grp, lft, rgt, level);

        return "redirect:/board/"+boardNo;
    }

}
