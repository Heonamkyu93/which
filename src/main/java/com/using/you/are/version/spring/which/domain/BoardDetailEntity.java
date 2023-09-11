package com.using.you.are.version.spring.which.domain;

import com.using.you.are.version.spring.which.domain.BoardEntity;
import com.using.you.are.version.spring.which.domain.BoardFileEntity;
import com.using.you.are.version.spring.which.domain.ReplyEntity;
import lombok.Data;

import java.util.List;

@Data
public class BoardDetailEntity {


    private BoardEntity boardEntity;
    private List<ReplyEntity> replyEntities;
    private List<BoardFileEntity> boardFileEntities;

    private BoardDetailEntity(){

    }
    public BoardDetailEntity(BoardEntity boardEntity, List<BoardFileEntity> boardFileEntities, List<ReplyEntity> replyEntities) {
        this.boardEntity=boardEntity;
        this.boardFileEntities = boardFileEntities;
        this.replyEntities = replyEntities;
    }

}
