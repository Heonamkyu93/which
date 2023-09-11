package com.using.you.are.version.spring.which.transObject;

import com.using.you.are.version.spring.which.domain.BoardEntity;
import com.using.you.are.version.spring.which.domain.BoardFileEntity;
import com.using.you.are.version.spring.which.dto.BoardDto;
import com.using.you.are.version.spring.which.dto.BoardFileDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BoardFileTransMapper {

    BoardFileTransMapper INSTANCE = Mappers.getMapper(BoardFileTransMapper.class);


    BoardFileDto entityToDTO(BoardFileEntity entity);

    BoardFileEntity dtoToEntity(BoardFileDto boardFileDto);




}
