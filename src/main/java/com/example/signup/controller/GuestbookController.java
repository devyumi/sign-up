package com.example.signup.controller;

import com.example.signup.config.auth.CustomUserDetails;
import com.example.signup.domain.Member;
import com.example.signup.dto.GuestbookRequestDto;
import com.example.signup.dto.GuestbookResponseDto;
import com.example.signup.service.GuestbookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("guestbook")
@RequiredArgsConstructor
public class GuestbookController {
    private final GuestbookService guestbookService;
    private final Logger log = LoggerFactory.getLogger(GuestbookController.class);

    @GetMapping
    public String findGuestbook(Model model){
        List<GuestbookResponseDto> guestbooksList = guestbookService.findGuestbook();
        model.addAttribute("guestbooksList", guestbooksList);

        return "guestbook";
    }

    @PostMapping("write")
    public String WriteGuestbook(@AuthenticationPrincipal CustomUserDetails auth,
                                 @Valid GuestbookRequestDto guestbookDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                log.error("{}: {}", fieldError.getField(), fieldError.getDefaultMessage());
            }
            return "redirect:/guestbook";
        }

        Optional<Member> member = guestbookService.getMember(auth.getUsername());
        guestbookService.saveGuestbook(guestbookDto, member);
        log.info("[{}] 방명록 등록 완료", auth.getUsername());
        return "redirect:/guestbook";
    }
}