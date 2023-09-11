package com.using.you.are.version.spring.which.service;

import com.using.you.are.version.spring.which.domain.*;
import com.using.you.are.version.spring.which.dto.*;
import com.using.you.are.version.spring.which.repository.BoardPageRepository;
import com.using.you.are.version.spring.which.repository.BoardRepository;
import com.using.you.are.version.spring.which.transObject.BoardFileTransMapper;
import com.using.you.are.version.spring.which.transObject.BoardTransMapper;
import com.using.you.are.version.spring.which.transObject.MemberTransMapper;
import com.using.you.are.version.spring.which.transObject.ReplyTransMapper;
import com.using.you.are.version.spring.which.utils.MultipleFileUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepositoryJpa;
    private final MultipleFileUpload multipleFileUpload;
    private final BoardPageRepository boardPageRepository;

    public void boardContentSave(BoardDto boardDto, List<MultipartFile> fileList) {
        BoardEntity boardEntity = BoardTransMapper.INSTANCE.dtoToEntity(boardDto);
        MemberInfo memberInfo = new MemberInfo();
        ArrayList<BoardFileEntity> fileEntityArrayList = multipleFileUpload.fileSave(fileList);
        memberInfo.setMemberId(boardDto.getMemberId());
        boardEntity.setMemberInfo(memberInfo);
        boardRepositoryJpa.boardContentSave(boardEntity, fileEntityArrayList);
    }

    public Page<BoardEntity> boardList(Pageable pageable) {
        return boardPageRepository.findAll(pageable);
    }

    public BoardDetailDto boardDetail(Long boardId) {
        BoardDetailEntity boardDetailEntity = boardRepositoryJpa.boardDetail(boardId);
        BoardDto boardDto = BoardTransMapper.INSTANCE.entityToDTO(boardDetailEntity.getBoardEntity());
        MemberInfoDto memberInfoDto = MemberTransMapper.INSTANCE.entityToDTO(boardDetailEntity.getBoardEntity().getMemberInfo());
        boardDto.setMemberInfoDto(memberInfoDto);
        List<ReplyEntity> replyEntities = boardDetailEntity.getReplyEntities();
        List<ReplyDto> replyDtoList = new ArrayList<>();
        for (ReplyEntity replyEntity : replyEntities) {
            if (replyEntity != null) {
                ReplyDto replyDto = new ReplyDto();
                replyDto = ReplyTransMapper.INSTANCE.entityToDTO(replyEntity);
                MemberInfoDto memberInfoDto1 = new MemberInfoDto();
                memberInfoDto1 = MemberTransMapper.INSTANCE.entityToDTO(replyEntity.getMemberInfo());
                replyDto.setMemberInfoDto(memberInfoDto1);
                replyDtoList.add(replyDto);
            }
        }

        List<BoardFileEntity> boardFileEntities = boardDetailEntity.getBoardFileEntities();
        List<BoardFileDto> fileDtoList = new ArrayList<>();
        for (BoardFileEntity boardFileEntity : boardFileEntities) {
            if (boardFileEntity != null) {
                BoardFileDto boardFileDto = new BoardFileDto();
                boardFileDto = BoardFileTransMapper.INSTANCE.entityToDTO(boardFileEntity);
                boardFileDto.setBoardId(boardDetailEntity.getBoardEntity().getBoardId());
                fileDtoList.add(boardFileDto);
            }
        }
        BoardDetailDto boardDetailDto = new BoardDetailDto(boardDto, fileDtoList, replyDtoList);
        return boardDetailDto;
    }

    public void replySave(ReplyDto replyDto) {
        ReplyEntity replyEntity = ReplyTransMapper.INSTANCE.dtoToEntity(replyDto);
        BoardEntity boardEntity = new BoardEntity();
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setMemberId(replyDto.getMemberId());
        boardEntity.setBoardId(replyDto.getBoardId());
        replyEntity.setBoardEntity(boardEntity);
        replyEntity.setMemberInfo(memberInfo);
        boardRepositoryJpa.replySave(replyEntity);
    }



    public void boardUpdate(BoardDto boardDto, List<MultipartFile> fileList) {
        BoardEntity boardEntity = BoardTransMapper.INSTANCE.dtoToEntity(boardDto);
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setMemberId(boardDto.getMemberId());
        boardEntity.setMemberInfo(memberInfo);
        ArrayList<BoardFileEntity> fileEntityArrayList = multipleFileUpload.fileSave(fileList);
        boardRepositoryJpa.boardUpdate(boardEntity, fileEntityArrayList);
    }

    public void replyUpdate(ReplyDto replyDto) {
        ReplyEntity replyEntity = ReplyTransMapper.INSTANCE.dtoToEntity(replyDto);
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setMemberId(replyDto.getMemberId());
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardId(replyDto.getBoardId());
        replyEntity.setMemberInfo(memberInfo);
        replyEntity.setBoardEntity(boardEntity);
        boardRepositoryJpa.replyUpdate(replyEntity);

    }

    public void replyDelete(ReplyDto replyDto) {
        ReplyEntity replyEntity = ReplyTransMapper.INSTANCE.dtoToEntity(replyDto);
        boardRepositoryJpa.replyDelete(replyEntity);
    }
    public void deleteFile(BoardFileDto boardFileDto) {
        BoardFileEntity fileEntity = BoardFileTransMapper.INSTANCE.dtoToEntity(boardFileDto);
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardId(boardFileDto.getBoardId());
        fileEntity.setBoardEntity(boardEntity);
        boardRepositoryJpa.deleteFile(fileEntity);
        multipleFileUpload.fileDelete(boardFileDto.getFileServerName());

    }
    public void deleteBoard(BoardDto boardDto) {
        BoardEntity boardEntity = BoardTransMapper.INSTANCE.dtoToEntity(boardDto);
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setMemberId(boardDto.getMemberId());
        boardEntity.setMemberInfo(memberInfo);
        boardRepositoryJpa.deleteBoard(boardEntity);
    }
}
