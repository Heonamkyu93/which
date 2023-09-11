package com.using.you.are.version.spring.which.dto;

import com.using.you.are.version.spring.which.domain.MemberInfo;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
public class BoardDto {
    private Long boardId;  // 게시글번호


    @NotBlank
    private String boardTitle; // 제목
    @NotBlank
    private String boardContent; // 내용
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date boardCreatedDate; // 작성일
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date boardLastModifiedDate; // 최종수정일

    private Long memberId;
    private MemberInfoDto memberInfoDto;
}
