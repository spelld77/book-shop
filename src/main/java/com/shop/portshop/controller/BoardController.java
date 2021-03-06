package com.shop.portshop.controller;

import com.shop.portshop.commons.Pagination;
import com.shop.portshop.constant.WorkState;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            @RequestParam(defaultValue = "") String target,
            @RequestParam(defaultValue = "") String keyword,
            Model model){

        int allBoardCount = 0;
        List<BoardVO> boardList = null;
        // 검색 할 경우
        if(!keyword.equals("")){
            List<Object> searchResult = boardService.searchBoard(target, keyword, nowPage);
            boardList = (ArrayList) searchResult.get(0);
            pagination = (Pagination) searchResult.get(1);

        // 검색 안할때
        } else{
            allBoardCount = boardService.getAllBoardCount();
            pagination = new Pagination(allBoardCount, nowPage, 10, 5);
            boardList = boardService.getBoardList(pagination);
        }

        model.addAttribute("target", target);
        model.addAttribute("keyword", keyword);
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
            Principal principal,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "files") MultipartFile[] files,
            RedirectAttributes redirectAttributes){

        String writer = (null == principal) ? "익명" : principal.getName();
        boolean state = boardService.addBoard(writer, title, content, files);

        if(state){
            redirectAttributes.addFlashAttribute("state",WorkState.SUCCESS);
            redirectAttributes.addFlashAttribute("message", "게시글 작성에 성공했습니다");
        } else{
            redirectAttributes.addFlashAttribute("state",WorkState.FAILURE);
            redirectAttributes.addFlashAttribute("message", "게시글 작성에 실패했습니다");
        }
        return "redirect:/board";
    }

    //게시글 보기
    @GetMapping("/{boardNo}")
    public String viewOneBoard(
            @PathVariable("boardNo") long boardNo, Model model){

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

    // 게시글 삭제
    @PostMapping("/{boardNo}/delete")
    public String deleteBoard(@PathVariable long boardNo,
                              @RequestParam String writer,
                              Principal principal,
                              RedirectAttributes redirectAttributes){


        log.info("deleteBoard");

        // 글 작성자가 아닐때
        if( null == principal || !writer.equals(principal.getName())){
            return "redirect:/board";
        }

        boolean state= boardService.deleteBoard(boardNo);

        if(state){
            redirectAttributes.addFlashAttribute("state", WorkState.SUCCESS);
            redirectAttributes.addFlashAttribute("message", "게시글 삭제에 성공했습니다");
        } else{
            redirectAttributes.addFlashAttribute("state", WorkState.FAILURE);
            redirectAttributes.addFlashAttribute("message", "게시글 삭제에 실패했습니다");
        }

        return "redirect:/board";
    }

    //게시글 수정 페이지
    @GetMapping("/{boardNo}/edit")
    public String editBoardForm(
            @PathVariable long boardNo, Model model){
        log.info("BoardController: editBoardForm");

        boardService.getEditBoardForm(boardNo);
        BoardVO board = boardService.getEditBoardForm(boardNo);
        model.addAttribute("board", board);
        return "/board_templates/edit";
    }

    // 게시글 수정
    @PostMapping("/{boardNo}/edit")
    public String editBoard(
            @PathVariable long boardNo,
            @RequestParam String title, @RequestParam String content, RedirectAttributes redirectAttributes){

        boolean state = boardService.editBoard(boardNo, title, content);

        if(state){
            redirectAttributes.addFlashAttribute("state", WorkState.SUCCESS);
            redirectAttributes.addFlashAttribute("message", "게시글 수정에 성공했습니다");
        } else{
            redirectAttributes.addFlashAttribute("state", WorkState.FAILURE);
            redirectAttributes.addFlashAttribute("message", "게시글 수정에 실패했습니다");
        }

        return "redirect:/board";
    }

    //루트 댓글 작성
    @PostMapping("/{boardNo}/comment")
    @ResponseBody
    public String createComment(@PathVariable long boardNo, @RequestParam String writer,
                             @RequestParam String content){
        boolean result = boardService.createRootComment(boardNo, writer, content);
        return result ? "success" : "fail";

    }

    // 대댓글 작성
    @ResponseBody
    @PostMapping("/commentReply")
    public String creatCommentReply(
            @RequestParam long boardNo, @RequestParam String writer,
            @RequestParam long grp,   @RequestParam int lft,
            @RequestParam int rgt,    @RequestParam int level, @RequestParam String content){

        boolean result = boardService.createCommentReply(
                boardNo, writer, content, grp, lft, rgt, level);

        return result ? "success" : "fail";
    }


}
