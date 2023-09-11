package com.using.you.are.version.spring.which.service;


import com.using.you.are.version.spring.which.domain.MemberInfo;
import com.using.you.are.version.spring.which.dto.MemberInfoDto;
import com.using.you.are.version.spring.which.repository.MemberRepository;
import com.using.you.are.version.spring.which.transObject.MemberTransMapper;
import com.using.you.are.version.spring.which.utils.MainSend;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder utilsPasswordEncoder;
    private final MemberRepository memberRepositoryJpa;
    private final MainSend mainSend;
    public void memberSave(MemberInfoDto memberInfoDto) {
        MemberInfo memberInfo = MemberTransMapper.INSTANCE.dtoToEntity(memberInfoDto);
        memberInfo = joinVailid(memberInfo);
        memberInfo.setMemberPassword(utilsPasswordEncoder.encode(memberInfo.getMemberPassword()));
        Long l = memberRepositoryJpa.memberSave(memberInfo);
    }

    private MemberInfo joinVailid(MemberInfo memberInfo) {  // 회원가입시만 vailed
        memberInfo.setMemberAvailable("yes");
        if (memberInfo.getMemberGender() == null || memberInfo.getMemberGender().isEmpty())
            memberInfo.setMemberGender("ze");
        memberInfo.setMemberJoinDate(new Date());
        memberInfo.setMemberUpdatePassword(new Date());
        memberInfo.setMemberRole("ROLE_GUEST");
        return memberInfo;
    }

    public MemberInfoDto findByMemberId(Long memberId) {
        Optional<MemberInfo> optionalMemberInfoMember = memberRepositoryJpa.findByMemberId(memberId);
        MemberInfo memberInfo=optionalMemberInfoMember.orElseGet(() -> {
            return new MemberInfo();
        });
        MemberInfoDto memberInfoDto = MemberTransMapper.INSTANCE.entityToDTO(memberInfo);
        return memberInfoDto;
    }

    public void emailSend(Long id, String email, String what, HttpSession session) {
        String uuid = UUID.randomUUID().toString();
        if(session.getAttribute(id.toString())!=null) session.removeAttribute(id.toString());
        session.setAttribute(id.toString(),uuid);
        session.setMaxInactiveInterval(300);
        if(what.equals("email")){
            mainSend.sendEmail(email,"spring site 이메일인증번호입니다.", "인증번호는 15분안에 입력해야하고 15분이 지난경우 다시 인증번호를 받아주세요 "+uuid);
            
        }else{

        }

    }

    public boolean emailConfirm(String emailConfirm, Long memberId, HttpServletRequest request) {
    HttpSession session = request.getSession();
    String confirm=session.getAttribute(memberId.toString()).toString();
    if(emailConfirm.equals(confirm)){
        session.removeAttribute(memberId.toString());
        memberRepositoryJpa.findByIdUpdateRole(memberId);
        return true;
    }else{
        return false;
    }
    }

    public boolean findByMemberValue(String category, String value) {
        MemberInfo memberInfo=memberRepositoryJpa.findByMemberValue(category, value);
        if(memberInfo==null) return true;
        else return false;
    }

    public MemberInfoDto memberUpdate(MemberInfoDto memberInfoDto) {
        MemberInfo memberInfo = memberRepositoryJpa.memberUpdate(memberInfoDto);
        return memberInfoDto;
    }

    /*public MemberInfoDto findEmail(String memberNickname) {
        List<MemberInfo> email = memberRepositoryJpa.findEmail(memberNickname);
        List<MemberInfoDto> memberList=new ArrayList<>();
        if(email.size()>0){
            for (MemberInfo memberInfo : email) {
               MemberInfoDto memberInfoDto = MemberTransMapper.INSTANCE.entityToDTO(memberInfo);
                memberList.add(memberInfoDto);
            }
            return memberList.get(0);
        }
        MemberInfoDto memberInfoDto = new MemberInfoDto();
        memberInfoDto.setMemberEmail("등록된 닉네임이 없습니다");
        return memberInfoDto;
    }*/

    public MemberInfoDto findEmail(String memberNickname) {
        String category = "memberNickname";
        MemberInfo memberInfo = memberRepositoryJpa.findByMemberValue(category, memberNickname);
        if(memberInfo!=null){
        MemberInfoDto memberInfoDto = MemberTransMapper.INSTANCE.entityToDTO(memberInfo);
            String result = "입력하신 닉네임으로 찾은 이메일주소입니다. "+memberInfoDto.getMemberEmail();
            memberInfoDto.setMemberEmail(result);
        return memberInfoDto;}
        else {
            MemberInfoDto memberInfoDto = new MemberInfoDto();
            memberInfoDto.setMemberEmail("등록된 닉네임이 없습니다");
            return memberInfoDto;
        }
    }

    public void findPassword(String memberEmail) {  // 나중에 비밀번호 변경이랑 겹치는부분 따로 메소드로 뺴서 재활용
        String category = "memberEmail";
        MemberInfo memberInfo = memberRepositoryJpa.findByMemberValue(category, memberEmail);
        if(memberInfo!=null && memberInfo.getMemberEmail().equals(memberEmail)){
            String newNotEncodePassword = changePassword(memberInfo.getMemberId(), null);
            mainSend.sendEmail(memberEmail,"변경된비밀번호입니다",newNotEncodePassword);
        }

        }
        public String changePassword(Long memberId,String newPassword){
            log.info("newPassword={}",newPassword);
        if(newPassword==null) {
                System.out.println("여기안들어오지?");
                String uuid = UUID.randomUUID().toString();
                String newNotEncodePassword = uuid.substring(0, 14);
                newPassword = utilsPasswordEncoder.encode(newNotEncodePassword);
                memberRepositoryJpa.updatePassword(memberId,newPassword);
                return newNotEncodePassword;
            }else{
            newPassword = utilsPasswordEncoder.encode(newPassword);
        }
            memberRepositoryJpa.updatePassword(memberId,newPassword);
            return newPassword;
        }

    public void withdraw(MemberInfoDto memberInfoDto) {
        String uuid = UUID.randomUUID().toString();
        String newNotEncodePassword = uuid.substring(0, 14);
        String fakePassword = utilsPasswordEncoder.encode(newNotEncodePassword);
        memberRepositoryJpa.withdraw(memberInfoDto.getMemberId(),fakePassword);
    }
}
