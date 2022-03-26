package com.shop.portshop.mapper;

import com.shop.portshop.vo.BoardVO;
import com.shop.portshop.vo.CommentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("BoardMapper")
public interface BoardMapper {
    List<BoardVO> selectBoards(@Param("startRecord") long startRecord, @Param("recordPerPage") int recordPerPage);

    int insertOneBoard(BoardVO boardVO);
    int countAllBoard();

    BoardVO selectOneBoard(long boardNo);
    void increaseViews(long boardNo);

    boolean insertNewRootComment(CommentVO commentVO);

    List<CommentVO> selectComments(long boardNo);

    // 부모 댓글의 마지막 자식 rgt를 구한다
    int selectLastChildRgt(CommentVO commentVO);

    void updateIncreaseCommentLft(@Param("commentVO") CommentVO commentVO, @Param("lastChildRgt") int lastChildRgt);
    void updateIncreaseCommentRgt(@Param("commentVO") CommentVO commentVO, @Param("lastChildRgt") int lastChildRgt);

    boolean insertNewChildComment(@Param("commentVO") CommentVO commentVO, @Param("lastChildRgt") int lastChildRgt);

    int deleteBoard(long boardNo);
    int deleteAllComment(long boardNo);
    List<String> selectAllFiles(long boardNo);
    int deleteAllFileNames(long boardNo);

    int updateBoard(@Param("boardNo") long boardNo,
                    @Param("title") String title,
                    @Param("content") String content);
}
