package com.example.signup.service;

import com.example.signup.domain.Member;
import com.example.signup.domain.MemberRole;
import com.example.signup.dto.MemberSignupDto;
import com.example.signup.repository.MemberRepository;
import com.example.signup.repository.MemberRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService {
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public Long signupOwn(MemberSignupDto memberSignupDto) {
        checkDuplicationMember(memberSignupDto);
        comparePassword(memberSignupDto);

        Member member = Member.builder()
                .platformType("OWN")
                .email(memberSignupDto.getEmail())
                .password(passwordEncoder.encode(memberSignupDto.getPassword()))
                .nickname(memberSignupDto.getNickname())
                .build();

        memberRepository.save(member);

        MemberRole memberRole = MemberRole.builder()
                .roleName("ROLE_USER")
                .member(member)
                .build();

        return memberRoleRepository.save(memberRole).getId();
    }

    private void checkDuplicationMember(MemberSignupDto memberSignupDto) {
        memberRepository.findByEmail(memberSignupDto.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    private void comparePassword(MemberSignupDto memberSignupDto) {
        if (!memberSignupDto.getPassword().equals(memberSignupDto.getCheckedPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
    }
}