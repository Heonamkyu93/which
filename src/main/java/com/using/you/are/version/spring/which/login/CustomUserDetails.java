package com.using.you.are.version.spring.which.login;

import com.using.you.are.version.spring.which.domain.MemberInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class CustomUserDetails implements UserDetails , OAuth2User {

    private final MemberInfo memberInfo;
    private Map<String,Object> attributes;
    public CustomUserDetails(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
    }
    public CustomUserDetails(MemberInfo memberInfo, Map<String, Object> attributes) {
        this.memberInfo = memberInfo;
        this.attributes = attributes;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {  //권한
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(() -> {
            return memberInfo.getMemberRole();
        });
        return grantedAuthorities;
    }
    @Override
    public String getPassword() {
        return memberInfo.getMemberPassword();
    }

    @Override
    public String getUsername() {
        return memberInfo.getMemberEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        boolean result=(memberInfo.getMemberAvailable().equals("yes")) ? true:false;
        return result;
    }


//OAuth2User 의 메소드
    //attributes에 oauth2 정보가 들어있음

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return attributes.get("sub").toString();
    }
}
