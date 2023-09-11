package com.using.you.are.version.spring.which.controller;

import com.using.you.are.version.spring.which.dto.MemberInfoDto;
import com.using.you.are.version.spring.which.login.CustomUserDetails;
import com.using.you.are.version.spring.which.service.MemberService;
import com.using.you.are.version.spring.which.service.SupervisorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final SupervisorService supervisorService;

    @GetMapping("/join")// 회원가입폼으로 이동
    public String joinForm(@ModelAttribute MemberInfoDto memberInfoDto, Model model) {
        model.addAttribute("memberInfoDto", memberInfoDto);
        return "form/joinForm";
    }

    @PostMapping("/join")  //회원가입 요청
    public String memberSave(@Validated @ModelAttribute MemberInfoDto memberInfoDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "form/joinForm";
        memberService.memberSave(memberInfoDto);
        return "redirect:/";
    }

    @GetMapping("/user/email-send") //회원정보폼에서 이메일 인증  나중에 비밀번호 찾기도 재활용하자
    public String emailSend(@RequestParam("what") String what, @AuthenticationPrincipal CustomUserDetails customUserDetails, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        String email = customUserDetails.getUsername();
        Long id = customUserDetails.getMemberInfo().getMemberId();
        memberService.emailSend(id, email, what, session);
        redirectAttributes.addAttribute("memberId", id);
        return "redirect:/user/email-numberInput/{memberId}";
    }

    @GetMapping("/user/email-numberInput/{memberId}")  //이메일인증번호 입력폼   새로고침하면 메일을 보내서 리다이렉트하는거 만듬
    public String emailConfirmForm(@PathVariable String memberId, Model model, RedirectAttributes redirectAttributes) {
        if (redirectAttributes.getAttribute("wrong") != null)
            model.addAttribute("wrong", redirectAttributes.getAttribute("wrong"));
        model.addAttribute("memberId", memberId);
        return "form/emailConfirm";
    }

    @PostMapping("/user/email-confirm")  // 인증번호 확인  여기서 새로고침하면 이메일이 계속 가니깐 처리해야된다
    public String emailConfirm(@RequestParam String emailConfirm, @RequestParam Long memberId, HttpServletRequest request, RedirectAttributes redirectAttributes,@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        boolean result = memberService.emailConfirm(emailConfirm, memberId, request);
        redirectAttributes.addAttribute("wrong", "인증번호가틀렸습니다");  // 에러처리가 안넘어간다
        redirectAttributes.addAttribute("memberId", memberId);
        if (result){
            customUserDetails.getMemberInfo().setMemberRole("ROLE_MEMBER");
            return "redirect:/";}
        else {
            return "redirect:/user/email-numberInput/{memberId}";
        }
    }


    @GetMapping("/oauth/member-info")  // 오스회원 개인정보폼으로
    public String findByMemberId(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        MemberInfoDto memberInfoDto = memberService.findByMemberId(customUserDetails.getMemberInfo().getMemberId());
        model.addAttribute("memberInfoDto", memberInfoDto);
        return "form/memberDetailForm";
    }

    @GetMapping("/user/before")   // 일반 회원 개인정보 수정하기전에 비밀번호확인폼
    public String memberDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        MemberInfoDto memberInfoDto = new MemberInfoDto();
        memberInfoDto.setMemberId(customUserDetails.getMemberInfo().getMemberId());
        model.addAttribute("memberInfoDto", memberInfoDto);
        return "form/beforeDetailForm";
    }

    @PostMapping("/user/memberDetailCheck")//비밀번호 확인후 리다이렉트
    public String memberDetailCheck(@ModelAttribute MemberInfoDto memberInfoDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        Long id = memberInfoDto.getMemberId();
        memberInfoDto = supervisorService.memberDetailCheck(memberInfoDto);
        if (memberInfoDto == null) {
            redirectAttributes.addAttribute("memberId", id);
            return "redirect:/user/before/{memberId}";
        }
        redirectAttributes.addAttribute("memberId", memberInfoDto.getMemberId());
        return "redirect:/user/member-info/{memberId}";
    }

    @GetMapping("/user/before/{memberId}")   // 틀린비밀번호를 입력했을경우 리다이렉트
    public String againBefore(@PathVariable Long memberId, Model model) {
        MemberInfoDto memberInfoDto = new MemberInfoDto();
        memberInfoDto.setMemberId(memberId);
        model.addAttribute("memberInfoDto", memberInfoDto);
        model.addAttribute("wrong", "wrong_pwd");
        return "form/beforeDetailForm";
    }


    @GetMapping("/user/member-info/{memberId}")  // 일반회원 개인정보폼으로
    public String findByMemberId(@PathVariable Long memberId, Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails, RedirectAttributes redirectAttributes) {
        MemberInfoDto memberInfoDto = memberService.findByMemberId(memberId);
        model.addAttribute("memberInfoDto", memberInfoDto);
        if (!memberId.equals(customUserDetails.getMemberInfo().getMemberId()))
            return "redirect:/";
        return "form/memberDetailForm";
    }


    @GetMapping("/duplicateCheck")  // 이메일,닉네임 중복체크
    @ResponseBody
    public boolean duplicateCheck(@RequestParam String category, @RequestParam String value) {
        System.out.println("category = " + category);
        System.out.println("value = " + value);

        boolean result = memberService.findByMemberValue(category, value);
        return result;
    }

    @PostMapping("/user/member-update")  // 회원정보 업데이트 처리
    public String memberUpdate(@ModelAttribute MemberInfoDto memberInfoDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) return "form/memberDetailForm";
        MemberInfoDto memberInfoDto1 = memberService.memberUpdate(memberInfoDto);
        redirectAttributes.addAttribute("memberId", memberInfoDto1.getMemberId());
        redirectAttributes.addAttribute("success", "정보수정완료");
        return "redirect:/user/member-info/{memberId}";
    }

    @GetMapping("/findMemberInfoForm")   //이메일/비밀번호 찾기폼
    public String findMemberInfoForm() {
        return "form/findMemberInfoForm";
    }

    @GetMapping("/findEmail")    //닉네임 입력후 이메일리턴
    @ResponseBody
    public String findEmail(@RequestParam String memberNickname) {
        MemberInfoDto memberInfoDto= memberService.findEmail(memberNickname);
        return memberInfoDto.getMemberEmail();
    }

    @GetMapping("/findPassword")        //비밀번호 찾기
    @ResponseBody
    public String findPassword(@RequestParam String memberEmail) {
        memberService.findPassword(memberEmail);


        return null;
    }
    @GetMapping("/user/before-password-changeForm")  // 비밀번호 변경전 본인확인폼
    public String beforePasswordChangeForm(@AuthenticationPrincipal CustomUserDetails customUserDetails,Model model,RedirectAttributes redirectAttributes){
        if(redirectAttributes.getAttribute("wrong")!=null) model.addAttribute("wrong",redirectAttributes.getAttribute("wrong"));
        MemberInfoDto memberInfoDto = new MemberInfoDto();
        memberInfoDto.setMemberId(customUserDetails.getMemberInfo().getMemberId());
        model.addAttribute("memberInfoDto", memberInfoDto);
        return "form/beforeChangePasswordForm";
    }
    @PostMapping("user/before-member-password-change")    //비밀번호 변경전 비밀번호확인
    public String beforePasswordChangeCheck(@ModelAttribute MemberInfoDto memberInfoDto,BindingResult bindingResult,Model model,RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) return "form/beforeChangePasswordForm";
        memberInfoDto = supervisorService.memberDetailCheck(memberInfoDto);
        if (memberInfoDto == null) {
            redirectAttributes.addAttribute("wron", "잘못된비밀번호");
            return "redirect:/user/before-password-changeForm";
        }
        redirectAttributes.addAttribute("memberId", memberInfoDto.getMemberId());
        return "redirect:/user/password-changeForm/{memberId}";

    }
    @GetMapping("/user/password-changeForm/{memberId}")  // 비밀번호 변경폼
    public String passwordChangeForm(@PathVariable Long memberId,Model model,@ModelAttribute MemberInfoDto memberInfoDto,BindingResult bindingResult,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.info("memberId={}",memberId);
        log.info("customUserDetails={}",customUserDetails.getMemberInfo().getMemberId());
        if(memberId!=customUserDetails.getMemberInfo().getMemberId()) return "redirect:/";
        if (bindingResult.hasErrors()) return "form/changePasswordForm";
        model.addAttribute("memberInfoDto", memberInfoDto);
        return "form/changePasswordForm";
    }
    @PostMapping("/user/password-change")   // 비밀번호 변경
    public String newPasswordSave(@RequestParam Long memberId,@RequestParam String memberPassword){
        memberService.changePassword(memberId,memberPassword);
        return "redirect:/";
    }

    @GetMapping("/user/withdraw")  // 탈퇴전 본인확인폼
    public String beforeMemberWithdrawForm(@AuthenticationPrincipal CustomUserDetails customUserDetails,Model model,@ModelAttribute MemberInfoDto memberInfoDto,RedirectAttributes redirectAttributes){
        if(redirectAttributes.getAttribute("wrong")!=null) model.addAttribute("wrong",redirectAttributes.getAttribute("wrong"));
        model.addAttribute("memberInfoDto", memberInfoDto);
        model.addAttribute("memberEmail", customUserDetails.getUsername());
        model.addAttribute("memberId", customUserDetails.getMemberInfo().getMemberId());
        return "form/beforeWithraw";
    }
    @PostMapping("/user/before-withdraw")  // 탈퇴전 본인확인
    public String beforeMemberWithdraw(@ModelAttribute MemberInfoDto memberInfoDto,BindingResult bindingResult,RedirectAttributes redirectAttributes){
        Long id = null;
        if(memberInfoDto!=null) {id = memberInfoDto.getMemberId();}
        if (bindingResult.hasErrors()) return "form/beforeWithraw";
        memberInfoDto = supervisorService.memberDetailCheck(memberInfoDto);
        if (memberInfoDto == null) {
            redirectAttributes.addAttribute("wron", "잘못된비밀번호");
            return "redirect:/user/withdraw";
        }
        redirectAttributes.addAttribute("memberId", id);
        return "redirect:/user/withdrawForm/{memberId}";
    }
    @GetMapping ("/user/withdrawForm/{memberId}")   //탈퇴폼
    public String memberWithdraw(@PathVariable Long memberId,@AuthenticationPrincipal CustomUserDetails customUserDetails,Model model){
        if(customUserDetails.getMemberInfo().getMemberId()!=memberId) return "redirect:/";
        model.addAttribute("memberId", customUserDetails.getMemberInfo().getMemberId());
        return "form/withdraw";
    }
    @GetMapping("/user/oauth2/withdrawForm")  //오스탈퇴폼호출하는 겟매핑으로 리다이렉트
    public String oauthWithdraw(@AuthenticationPrincipal CustomUserDetails customUserDetails,RedirectAttributes redirectAttributes){
        if(customUserDetails!=null && !customUserDetails.getMemberInfo().getMemberRole().equals("ROLE_OAUTH")) return "redirect:/";
        redirectAttributes.addAttribute("memberId", customUserDetails.getMemberInfo().getMemberId());
        return "redirect:/user/withdrawForm/{memberId}";
    }


    @PostMapping("/user/withdraw/delete")
    @ResponseBody
    public String withdrawDelete(@RequestBody MemberInfoDto memberInfoDto){
        memberService.withdraw(memberInfoDto);
        return "/logout";
    }

}
