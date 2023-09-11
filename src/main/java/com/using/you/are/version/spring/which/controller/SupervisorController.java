package com.using.you.are.version.spring.which.controller;

import com.using.you.are.version.spring.which.domain.MemberInfo;
import com.using.you.are.version.spring.which.dto.MemberInfoDto;
import com.using.you.are.version.spring.which.service.SupervisorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SupervisorController {

    private final SupervisorService supervisorService;

    @GetMapping("/admin/list")  // 관리자 멤버리스트
    public String memberList(Model model, @PageableDefault(page = 0, size = 10, sort = "memberLastLogin", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MemberInfo> list = supervisorService.memberList(pageable);
     /*  int nowPage=pageable.getPageNumber();
        int startPage = Math.max(nowPage-4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());
*/
        int totalPage = list.getTotalPages();
        int nowPage = pageable.getPageNumber() + 1;
       /* int startPage=(((nowPage - 1) / pageable.getPageSize()) * pageable.getPageSize()) + 1;
        int endPage = (((nowPage - 1) / pageable.getPageSize()) + 1) * pageable.getPageSize();*/
        int startPage = (((nowPage - 1) / 10) * 10) + 1;
        int endPage = (((nowPage - 1) / 10) + 1) * 10;
        if (endPage > totalPage) {
            endPage = totalPage;
        }
        if (nowPage < 0) {
            nowPage = pageable.getPageNumber() + 1;
        }
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("list", list);
        return "content/memberList";

    }

    @GetMapping("/admin/memberDetail/{memberId}")  // 관리자 멤버 개인정보 보기
    public String memberDetail(@PathVariable Long memberId, Model model) {
        MemberInfoDto memberInfoDto = supervisorService.memberDetail(memberId);
        model.addAttribute("memberInfoDto", memberInfoDto);
        return "form/memberDetailForm";
    }

   /* @PostMapping("/admin/memberDetailCheck")
    public String memberDetailCheck(@ModelAttribute MemberInfoDto memberInfoDto, BindingResult bindingResult, Model model) {
        memberInfoDto = supervisorService.memberDetailCheck(memberInfoDto);
        model.addAttribute("memberInfoDto", memberInfoDto);
        return "form/memberDetailForm";
    }*/

}
