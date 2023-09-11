package com.using.you.are.version.spring.which.transObject;

import com.using.you.are.version.spring.which.domain.ReplyEntity;
import com.using.you.are.version.spring.which.dto.ReplyDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReplyTransMapper {


    ReplyTransMapper INSTANCE = Mappers.getMapper(ReplyTransMapper.class);

    ReplyDto entityToDTO(ReplyEntity entity);
    ReplyEntity dtoToEntity(ReplyDto replyDto);

}
