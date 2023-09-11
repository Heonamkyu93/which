package com.using.you.are.version.spring.which.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardDetailDto {
    private BoardDto boardDto;
    private List<BoardFileDto> fileList;
    private List<ReplyDto> replyList;

    public BoardDetailDto(BoardDto boardDto, List<BoardFileDto> fileList, List<ReplyDto> replyList) {
        this.boardDto = boardDto;
        this.fileList = fileList;
        this.replyList = replyList;
    }
}
