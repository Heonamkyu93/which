package com.using.you.are.version.spring.which.login;

import com.using.you.are.version.spring.which.domain.MemberInfo;
import com.using.you.are.version.spring.which.oAuth2Object.GithubUserInfo;
import com.using.you.are.version.spring.which.oAuth2Object.GoogleUserInfo;
import com.using.you.are.version.spring.which.oAuth2Object.NaverUserInfo;
import com.using.you.are.version.spring.which.oAuth2Object.Oauth2UserInfo;
import com.using.you.are.version.spring.which.repository.LoginRepositoryJpaData;
import com.using.you.are.version.spring.which.repository.MemberRepository;
import com.using.you.are.version.spring.which.utils.UtilsPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserDetailService extends DefaultOAuth2UserService {

    private final UtilsPasswordEncoder utilsPasswordEncoder;
    private final MemberRepository memberRepositoryJpa;
    private final LoginRepositoryJpaData loginRepositoryJpaData;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);   // userRequest 안에 정보 엑세스토큰이나 email같은게 들어있음
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google
        Oauth2UserInfo oauth2UserInfo = null;
        if (registrationId.equals("google")) {
            oauth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (registrationId.equals("naver")) {
            oauth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        } else if (registrationId.equals("github")) {
            oauth2UserInfo = new GithubUserInfo(oAuth2User.getAttributes());
        }
        MemberInfo memberInfo = loginRepositoryJpaData.findByMemberEmail(oauth2UserInfo.getMail()); // 중복을 찾고 이미 회원가입이 되어있으면 그냥 로그인처리
        if (memberInfo == null) {
            String role = "ROLE_OAUTH";
            String gender = "ze";
            String available = "yes";
            memberInfo = new MemberInfo();
            memberInfo.setMemberEmail(oauth2UserInfo.getMail());
            memberInfo.setMemberName(oauth2UserInfo.getName());
            memberInfo.setMemberNickname(oauth2UserInfo.getNickname()+"_"+registrationId);
            memberInfo.setMemberPassword(utilsPasswordEncoder.encode(registrationId+"_"+new Date()));
            memberInfo.setMemberBirth(oauth2UserInfo.getBirth());
            memberInfo.setMemberGender(gender);
            memberInfo.setMemberAvailable(available);
            memberInfo.setMemberRole(role);
            memberInfo.setMemberLastLogin(new Date());
            memberInfo.setMemberJoinDate(new Date());
            memberInfo.setMemberUpdatePassword(new Date());
            memberRepositoryJpa.memberSave(memberInfo);
        }else {
            memberRepositoryJpa.loginDateUpdate(memberInfo);
        }

        return new CustomUserDetails(memberInfo, oAuth2User.getAttributes());
    }
}
