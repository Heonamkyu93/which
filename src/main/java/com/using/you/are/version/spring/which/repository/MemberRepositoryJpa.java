package com.using.you.are.version.spring.which.repository;

import com.using.you.are.version.spring.which.domain.MemberInfo;
import com.using.you.are.version.spring.which.dto.MemberInfoDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MemberRepositoryJpa implements MemberRepository {

    private final EntityManager em;


    @Override
    @Transactional
    public void updatePassword(Long memberId,String newPassword) {
        MemberInfo memberInfo = em.find(MemberInfo.class, memberId);
        memberInfo.setMemberPassword(newPassword);
    }

    @Override
    @Transactional(readOnly = true)
    public  List<MemberInfo> findEmail(String Nickname) {
        TypedQuery<MemberInfo> query = em.createQuery(
                "SELECT m FROM MemberInfo m WHERE m.memberNickname=:name", MemberInfo.class);
        query.setParameter("name", Nickname);
        List<MemberInfo> resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public Long memberSave(MemberInfo memberInfo) {
        em.persist(memberInfo);
        Long id = memberInfo.getMemberId();
        return id;
    }

    @Override
    @Transactional
    public void loginDateUpdate(MemberInfo memberInfo) {
        Long id = memberInfo.getMemberId();
        memberInfo = em.find(MemberInfo.class, id);
        memberInfo.setMemberLastLogin(new Date());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MemberInfo> findByMemberId(Long memberId) {
        MemberInfo memberInfo = em.find(MemberInfo.class, memberId);
        return Optional.ofNullable(memberInfo);

    }

    @Override
    @Transactional
    public void findByIdUpdateRole(Long memberId) {
        MemberInfo memberInfo = em.find(MemberInfo.class, memberId);
        memberInfo.setMemberRole("ROLE_MEMBER");

    }

    @Override
    @Transactional(readOnly = true)
    public MemberInfo findByMemberValue(String category, String value) {
        TypedQuery<MemberInfo> query = em.createQuery(
                "SELECT m FROM MemberInfo m WHERE m." + category + "= :" + category, MemberInfo.class);
        query.setParameter(category, value);
        MemberInfo memberInfo = null;
        try {
            memberInfo = query.getSingleResult();
        } catch (NoResultException ex) {
            memberInfo = null;
        }
        return memberInfo;
    }

    @Override
    @Transactional
    public MemberInfo memberUpdate(MemberInfoDto memberInfoDto) {
        MemberInfo memberInfo = em.find(MemberInfo.class, memberInfoDto.getMemberId());
        memberInfo.setMemberName(memberInfoDto.getMemberName());
        memberInfo.setMemberNickname(memberInfoDto.getMemberNickname());
        return memberInfo;
    }

    @Override
    @Transactional
    public void withdraw(Long memberId,String fakePassword) {
        MemberInfo memberInfo = em.find(MemberInfo.class, memberId);
        memberInfo.setMemberAvailable("no");
        memberInfo.setMemberNickname("탈퇴한회원");
        memberInfo.setMemberName("탈퇴한회원");
        memberInfo.setMemberRole("ROLE_NONE");
        memberInfo.setMemberEmail("탈퇴한회원"+memberId);
        memberInfo.setMemberPassword(fakePassword);
    }
}