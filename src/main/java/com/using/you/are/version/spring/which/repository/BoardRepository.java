package com.using.you.are.version.spring.which.repository;

import com.using.you.are.version.spring.which.domain.BoardEntity;
import com.using.you.are.version.spring.which.domain.BoardFileEntity;
import com.using.you.are.version.spring.which.domain.BoardDetailEntity;
import com.using.you.are.version.spring.which.domain.ReplyEntity;
import com.using.you.are.version.spring.which.dto.BoardDto;

import java.util.ArrayList;
import java.util.List;

public interface BoardRepository {
    void boardContentSave(BoardEntity boardEntity, ArrayList<BoardFileEntity> fileEntityArrayList);

    BoardDetailEntity boardDetail(Long boardId);

    void replySave(ReplyEntity replyEntity);


    void deleteFile(BoardFileEntity fileEntity);

    void boardUpdate(BoardEntity boardEntity, ArrayList<BoardFileEntity> fileEntityArrayList);

    void replyUpdate(ReplyEntity replyEntity);

    void replyDelete(ReplyEntity replyEntity);

    void deleteBoard(BoardEntity boardEntity);
}
