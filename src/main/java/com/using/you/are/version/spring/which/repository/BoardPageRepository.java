package com.using.you.are.version.spring.which.repository;

import com.using.you.are.version.spring.which.domain.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardPageRepository extends JpaRepository <BoardEntity,Long> {
}
