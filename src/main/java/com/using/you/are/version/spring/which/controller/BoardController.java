package com.using.you.are.version.spring.which.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.using.you.are.version.spring.which.domain.BoardEntity;
import com.using.you.are.version.spring.which.domain.BoardFileEntity;
import com.using.you.are.version.spring.which.domain.MemberInfo;
import com.using.you.are.version.spring.which.domain.ReplyEntity;
import com.using.you.are.version.spring.which.dto.BoardDetailDto;
import com.using.you.are.version.spring.which.dto.BoardDto;
import com.using.you.are.version.spring.which.dto.BoardFileDto;
import com.using.you.are.version.spring.which.dto.ReplyDto;
import com.using.you.are.version.spring.which.login.CustomUserDetails;
import com.using.you.are.version.spring.which.service.BoardService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @Value("${file.dir}")
    private String path;

    @GetMapping("/private/board-content-save-form")  // 게시글 입력폼 (로그인 안하면 에러남) 세션에서 값찍는거때문
    public String boardContentSaveForm(@AuthenticationPrincipal CustomUserDetails customUserDetails, BoardDto boardDto , Model model){
        model.addAttribute("memberNickname", customUserDetails.getMemberInfo().getMemberNickname());
        model.addAttribute("boardDto", boardDto);
        model.addAttribute("memberId", customUserDetails.getMemberInfo().getMemberId());
        return "form/boardInsertForm";
    }

    @PostMapping("/private/board-content-save")  // 게시글 입력
    public String boardContentSave(@ModelAttribute BoardDto boardDto,BindingResult bindingResult, @RequestPart List<MultipartFile> boardFileList){
        if(bindingResult.hasErrors()) return "form/boardInsertForm";
        boardService.boardContentSave(boardDto,boardFileList);
        return "redirect:/boardList";
    }
    @GetMapping("/boardList") //글목록
    public String boardList(Model model,@PageableDefault(page = 0, size = 10, sort = "boardCreatedDate", direction = Sort.Direction.DESC) Pageable pageable,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        Page<BoardEntity> boardList = boardService.boardList(pageable);
        int totalPage = boardList.getTotalPages();
        int nowPage = pageable.getPageNumber() + 1;
        int startPage = (((nowPage - 1) / 10) * 10) + 1;
        int endPage = (((nowPage - 1) / 10) + 1) * 10;
        if (endPage > totalPage) {
            endPage = totalPage;
        }
        if (nowPage < 0) {
            nowPage = pageable.getPageNumber() + 1;
        }
        if(customUserDetails!=null){
            model.addAttribute("memberRole",customUserDetails.getMemberInfo().getMemberRole());
        }
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("list", boardList);
        return "content/boardList";
    }
    @GetMapping("/boardDetail/{boardId}")   // 게시글 보기
    public String boardDetail(@PathVariable Long boardId,Model model,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        BoardDetailDto boardDetailDto = boardService.boardDetail(boardId);
        if(customUserDetails!=null){
        model.addAttribute("memberNickname", customUserDetails.getMemberInfo().getMemberNickname());
        model.addAttribute("memberId", customUserDetails.getMemberInfo().getMemberId());}
        model.addAttribute("boardDto", boardDetailDto.getBoardDto());
        if(boardDetailDto.getReplyList().size()>0)
        model.addAttribute("reply", boardDetailDto.getReplyList());
        if(boardDetailDto.getFileList().size()>0)
        model.addAttribute("file", boardDetailDto.getFileList());
        return "form/boardDetailForm";
    }

    @PostMapping("/private/reply-save")  // 리플등록
    @ResponseBody
    public String replySave(@RequestBody ReplyDto replyDto){
        log.info("reply={}",replyDto.getReplyCreatedDate());
        boardService.replySave(replyDto);
        return "ok";
    }
    @ResponseBody   //파일다운로드
    @GetMapping("/private/board/download/{originalFileName}")
        public ResponseEntity<Resource> downloadFile(@PathVariable String originalFileName, @RequestParam String fileServerName) throws MalformedURLException {
        UrlResource urlResource= new UrlResource("file:"+path+fileServerName);

        String encodedUploadFileName = UriUtils.encode(originalFileName,
                StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" +
                encodedUploadFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);

    }
    @GetMapping("/private/board-content-updateForm/{boardId}")  // 게시글 수정폼
    public String boardUpdateForm(@PathVariable Long boardId, Model model,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        BoardDetailDto boardDetailDto = boardService.boardDetail(boardId);
        if(!customUserDetails.getMemberInfo().getMemberId().equals(boardDetailDto.getBoardDto().getMemberInfoDto().getMemberId())) return "redirect:/";
        model.addAttribute("boardDto", boardDetailDto.getBoardDto());
        if(boardDetailDto.getFileList().size()>0)
            model.addAttribute("file", boardDetailDto.getFileList());
        return "form/boardUpdateForm";
    }
    @PostMapping("/private/board-content-update/file-delete")  //게시글 수정 파일 삭제
    @ResponseBody
    public BoardFileDto boardDeleteFile(@RequestBody BoardFileDto boardFileDto){
        boardService.deleteFile(boardFileDto);
        return boardFileDto;
    }
    @PostMapping("/private/board-content-update")  //게시글 수정
    public String boardUpdate(@ModelAttribute BoardDto boardDto,@AuthenticationPrincipal CustomUserDetails customUserDetails,List<MultipartFile> fileList){
        log.info("boardDtoId={}",boardDto.getBoardId());
        if(!customUserDetails.getMemberInfo().getMemberId().equals(boardDto.getMemberId())) return "redirect:/";
        log.info("file={}",fileList.size());
        boardService.boardUpdate(boardDto, fileList);

        return "redirect:/boardList";
    }


    @PostMapping("/private/reply-update")  // 댓글업데이트
    @ResponseBody
    public ReplyDto replyUpdate(@RequestBody ReplyDto replyDto){

        boardService.replyUpdate(replyDto);
        return replyDto;
    }


    @PostMapping("/private/reply-delete") //댓글삭제
    @ResponseBody
    public ReplyDto replyDelete(@RequestBody ReplyDto replyDto){
        boardService.replyDelete(replyDto);
        return replyDto;
    }

    @PostMapping("/private/board-delete")  // 게시글삭제 (파일삭제처리안함
    @ResponseBody
    public String deleteBoard(@RequestBody BoardDto boardDto){
        boardService.deleteBoard(boardDto);
        return "/boardList";
    }


}


