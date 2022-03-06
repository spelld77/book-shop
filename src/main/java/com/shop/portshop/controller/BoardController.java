package com.shop.portshop.controller;

import com.shop.portshop.commons.Pagination;
import com.shop.portshop.service.BoardService;
import com.shop.portshop.vo.BoardVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {

    private BoardService boardService;
    private Pagination pagination;

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
            @RequestParam("content") String content){

        boolean result = boardService.addBoard(title, content);
        if(!result){
            log.info("board add failure!");
        }
        return "redirect:/board";
    }

    @GetMapping("/{boardNo}")
    public String viewOneBoard(@PathVariable("boardNo") long boardNo, Model model){
        BoardVO board = boardService.viewOneBoard(boardNo);
        if(board == null){
            return "redirect:/board";
        }
        model.addAttribute("board", board);
        return "/board_templates/view";
    }
}
