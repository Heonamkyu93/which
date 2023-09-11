package com.using.you.are.version.spring.which.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.using.you.are.version.spring.which.domain.BoardEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ReplyDto {
    private Long replyId;
    private String MemberNickname;
    private String reply;
 //   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
    private Date replyCreatedDate;
    private Long memberId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date replyLastModifiedDate;
    private Long boardId;
    private BoardDto boardDto;
    private MemberInfoDto memberInfoDto;


}
