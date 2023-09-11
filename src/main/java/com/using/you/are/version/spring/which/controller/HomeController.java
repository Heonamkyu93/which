package com.using.you.are.version.spring.which.controller;

import com.using.you.are.version.spring.which.domain.MemberInfo;
import com.using.you.are.version.spring.which.dto.MemberInfoDto;
import com.using.you.are.version.spring.which.login.CustomUserDetails;
import com.using.you.are.version.spring.which.login.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String index(){
        return "../static/index";
    }

    @GetMapping("/loginForm")
    public String loginForm(RedirectAttributes redirectAttributes,Model model){
        if(redirectAttributes.getAttribute("wrong")!=null)
            model.addAttribute("wrong", redirectAttributes.getAttribute("wrong"));
        return "form/loginForm";
    }

    @GetMapping("/after-login-get")
    public String test(){
        return "content/afterLogin";
    }
    @GetMapping("/after-login")
    public String afterLogin(){
        return "redirect:/after-login-get";
    }

    @GetMapping("/fail")
    public String fail(RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("wrong", "로그인에 실패하셨습니다. 비밀번호와 이메일을 확인해주세요");
        return "redirect:/loginForm";
    }


    @GetMapping("test")
    public String test2(Model model){
       // model.addAttribute("test", "널이아니야");
        return "test";
    }

}
