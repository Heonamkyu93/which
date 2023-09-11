package com.using.you.are.version.spring.which.repository;

import com.using.you.are.version.spring.which.domain.MemberInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberPageRepository extends JpaRepository<MemberInfo,Long> {


    Page<MemberInfo> findByMemberAvailable(String memberAvailable, Pageable pageable);

}
