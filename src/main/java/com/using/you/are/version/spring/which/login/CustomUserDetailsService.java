package com.using.you.are.version.spring.which.login;

import com.using.you.are.version.spring.which.domain.MemberInfo;
import com.using.you.are.version.spring.which.repository.LoginRepositoryJpaData;
import com.using.you.are.version.spring.which.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final LoginRepositoryJpaData loginRepositoryJpaData;
    private final MemberRepository memberRepositoryJpa;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberInfo memberInfo =loginRepositoryJpaData.findByMemberEmail(username);
        if(memberInfo!=null){
            memberRepositoryJpa.loginDateUpdate(memberInfo);
            return new CustomUserDetails(memberInfo);
        }
        return null;
    }
}
