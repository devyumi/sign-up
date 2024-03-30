package com.example.signup.service;

import com.example.signup.domain.Guestbook;
import com.example.signup.domain.Member;
import com.example.signup.dto.GuestbookRequestDto;
import com.example.signup.dto.GuestbookResponseDto;
import com.example.signup.repository.GuestbookRepository;
import com.example.signup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuestbookService {
    private final GuestbookRepository guestbookRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long saveGuestbook(GuestbookRequestDto guestbookDto, Optional<Member> member) {
        Guestbook guestbook = Guestbook.builder()
                .content(guestbookDto.getContent())
                .member(member.get())
                .build();
        return guestbookRepository.save(guestbook).getId();
    }

    public List<GuestbookResponseDto> findGuestbook() {
        List<Guestbook> guestbooks = guestbookRepository.findAll();
        List<GuestbookResponseDto> guestbooksDto = new ArrayList<>();

        for (Guestbook guestbook : guestbooks) {
            guestbooksDto.add(GuestbookResponseDto.builder()
                    .nickname(guestbook.getMember().getNickname())
                    .content(guestbook.getContent())
                    .createDate(guestbook.getCreateDate())
                    .build());
        }
        return guestbooksDto;
    }

    public Optional<Member> getMember(String username) {
        return memberRepository.findByEmail(username);
    }
}