package com.using.you.are.version.spring.which.dto;

import com.using.you.are.version.spring.which.domain.BoardEntity;
import lombok.Data;

@Data
public class BoardFileDto {
    private Long boardId;
    private String fileOriginalName;
    private String fileServerName;
    private Long memberId;

}
