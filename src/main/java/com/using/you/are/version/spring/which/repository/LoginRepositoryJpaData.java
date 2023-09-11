package com.using.you.are.version.spring.which.repository;

import com.using.you.are.version.spring.which.domain.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepositoryJpaData extends JpaRepository<MemberInfo,Long> {

    MemberInfo findByMemberEmail(String memberEmail);
}
