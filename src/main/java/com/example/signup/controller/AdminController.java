package com.example.signup.controller;

import com.example.signup.config.auth.CustomUserDetails;
import com.example.signup.dto.MembersDto;
import com.example.signup.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("search/members")
    public String searchMember(Model model) {
        List<MembersDto> membersList = adminService.findMembers();
        model.addAttribute("membersList", membersList);

        return "searchMember";
    }
}
