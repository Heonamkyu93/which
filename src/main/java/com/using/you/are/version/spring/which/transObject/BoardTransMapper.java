package com.using.you.are.version.spring.which.transObject;

import com.using.you.are.version.spring.which.domain.BoardEntity;
import com.using.you.are.version.spring.which.dto.BoardDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BoardTransMapper {

    BoardTransMapper INSTANCE = Mappers.getMapper(BoardTransMapper.class);

    BoardDto entityToDTO(BoardEntity entity);

    BoardEntity dtoToEntity(BoardDto boardDto);

}
