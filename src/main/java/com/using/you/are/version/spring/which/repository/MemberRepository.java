package com.using.you.are.version.spring.which.repository;

import com.using.you.are.version.spring.which.domain.MemberInfo;
import com.using.you.are.version.spring.which.dto.MemberInfoDto;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {


    Long memberSave(MemberInfo memberInfo);


    void loginDateUpdate(MemberInfo memberInfo);

    Optional<MemberInfo> findByMemberId(Long memberId);

    void findByIdUpdateRole(Long memberId);

    MemberInfo findByMemberValue(String category, String value);

    MemberInfo memberUpdate(MemberInfoDto memberInfoDto);

    List<MemberInfo> findEmail(String memberNickname);

    void updatePassword(Long memberId,String newPassword);

    void withdraw(Long memberId,String fakePassword);
}
