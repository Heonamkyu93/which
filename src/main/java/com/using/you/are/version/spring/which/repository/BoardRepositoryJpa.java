package com.using.you.are.version.spring.which.repository;

import com.using.you.are.version.spring.which.domain.BoardEntity;
import com.using.you.are.version.spring.which.domain.BoardFileEntity;
import com.using.you.are.version.spring.which.domain.ReplyEntity;
import com.using.you.are.version.spring.which.domain.BoardDetailEntity;
import com.using.you.are.version.spring.which.dto.BoardDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BoardRepositoryJpa implements BoardRepository {


    private final EntityManager em;


    @Transactional
    @Override
    public void boardContentSave(BoardEntity boardEntity, ArrayList<BoardFileEntity> fileEntityArrayList) {
        log.info("last={}",boardEntity.getBoardLastModifiedDate());
        em.persist(boardEntity);
        if (fileEntityArrayList.size() > 0) {
            for (BoardFileEntity fileEntity : fileEntityArrayList) {
                fileEntity.setBoardEntity(boardEntity);
                em.persist(fileEntity);
            }
        }

    }

    @Override
    @Transactional(readOnly = true)
    public BoardDetailEntity boardDetail(Long boardId) {
        BoardEntity boardEntity = em.find(BoardEntity.class, boardId);

        TypedQuery<BoardFileEntity> fileQuery = em.createQuery(
                "SELECT f FROM BoardFileEntity f WHERE f.boardEntity.boardId = :boardId",
                BoardFileEntity.class
        );
        fileQuery.setParameter("boardId", boardId);
        List<BoardFileEntity> fileList = null;
        try {
            fileList = fileQuery.getResultList();
        } catch (NoResultException e) {
        }
        TypedQuery<ReplyEntity> query = em.createQuery(
                "SELECT r FROM ReplyEntity r WHERE r.boardEntity.boardId = :boardId ORDER BY r.replyCreatedDate DESC",
                ReplyEntity.class
        );
        query.setParameter("boardId", boardId);
        List<ReplyEntity> replyList = null;
        try {
            replyList = query.getResultList();
        } catch (NoResultException e) {
        }
//        List<ReplyEntity> replyList = replyList(boardId);

        BoardDetailEntity boardDetailEntity = new BoardDetailEntity(boardEntity, fileList, replyList);
        boardDetailEntity.getReplyEntities();
        return boardDetailEntity;
    }

    @Override
    @Transactional
    public void replySave(ReplyEntity replyEntity) {
        em.persist(replyEntity);
    }

    @Override
    @Transactional
    public void deleteFile(BoardFileEntity fileEntity) {
        BoardFileEntity fileEntity1 = em.find(BoardFileEntity.class, fileEntity.getFileServerName());
        em.remove(fileEntity1);
    }

    @Override
    @Transactional
    public void boardUpdate(BoardEntity boardEntity, ArrayList<BoardFileEntity> fileEntityArrayList) {
        log.info("boardTitle={}",boardEntity.getBoardTitle());
        BoardEntity findBoardEntity = em.find(BoardEntity.class, boardEntity.getBoardId());
        findBoardEntity.setBoardContent(boardEntity.getBoardContent());
        findBoardEntity.setBoardTitle(boardEntity.getBoardTitle());
        if (fileEntityArrayList.size() > 0) {
            for (BoardFileEntity fileEntity : fileEntityArrayList) {
                fileEntity.setBoardEntity(boardEntity);
                em.persist(fileEntity);
            }
        }

    }

    @Override
    @Transactional
    public void replyUpdate(ReplyEntity replyEntity) {
        ReplyEntity replyEntity1 = em.find(ReplyEntity.class, replyEntity.getReplyId());
         replyEntity1.setReply(replyEntity.getReply());
    }
    @Transactional
    @Override
    public void replyDelete(ReplyEntity replyEntity) {
        ReplyEntity replyEntity1 = em.find(ReplyEntity.class, replyEntity.getReplyId());
        if (replyEntity1 != null) {
            em.remove(replyEntity1);
        }
    }

    @Override
    @Transactional
    public void deleteBoard(BoardEntity boardEntity) {
        BoardEntity boardEntity1 = em.find(BoardEntity.class, boardEntity.getBoardId());
        if(boardEntity1!=null) em.remove(boardEntity1);
    }
}
