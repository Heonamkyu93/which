package com.using.you.are.version.spring.which.service;

import com.using.you.are.version.spring.which.domain.MemberInfo;
import com.using.you.are.version.spring.which.dto.MemberInfoDto;
import com.using.you.are.version.spring.which.repository.MemberPageRepository;
import com.using.you.are.version.spring.which.transObject.MemberTransMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupervisorService {

    private final MemberPageRepository memberPageRepository;
    private final PasswordEncoder utilsPasswordEncoder;
   /* public Page<MemberInfo> memberList(Pageable pageable) {
        return supervisorRepository.findAll(pageable);
    }*/


    public Page<MemberInfo> memberList(Pageable pageable) {
        return memberPageRepository.findByMemberAvailable("yes", pageable);
    }

    public MemberInfoDto memberDetail(Long memberId) {
        Optional<MemberInfo> memberOptional = memberPageRepository.findById(memberId);
        MemberInfo memberInfo = memberOptional.orElseGet(() -> {
            return new MemberInfo(); // 또는 다른 값을 반환
        });
        MemberInfoDto memberInfoDto = MemberTransMapper.INSTANCE.entityToDTO(memberInfo);
        return memberInfoDto;
    }

    public MemberInfoDto memberDetailCheck(MemberInfoDto memberInfoDto) {
        Long id=memberInfoDto.getMemberId();
        Optional<MemberInfo> memberOptional = memberPageRepository.findById(id);
        MemberInfo memberInfo = memberOptional.orElseGet(() -> {
            return new MemberInfo(); // 또는 다른 값을 반환
        });
        boolean password=utilsPasswordEncoder.matches(memberInfoDto.getMemberPassword(),memberInfo.getMemberPassword());
        if(password){
            return MemberTransMapper.INSTANCE.entityToDTO(memberInfo);
        }else {

            return null;
        }
        
       }

}
