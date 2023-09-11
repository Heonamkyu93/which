package com.using.you.are.version.spring.which.transObject;

import com.using.you.are.version.spring.which.domain.MemberInfo;
import com.using.you.are.version.spring.which.dto.MemberInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberTransMapper {

    MemberTransMapper INSTANCE = Mappers.getMapper(MemberTransMapper.class);
    MemberInfoDto entityToDTO(MemberInfo memberInfo);
    MemberInfo dtoToEntity(MemberInfoDto memberInfoDto);


}
