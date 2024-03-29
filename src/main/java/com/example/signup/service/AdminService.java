package com.example.signup.service;

import com.example.signup.domain.Member;
import com.example.signup.dto.MembersDto;
import com.example.signup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;

    public List<MembersDto> findMembers() {
        List<Member> members = memberRepository.findAll();
        List<MembersDto> membersDto = new ArrayList<>();

        for (Member member : members) {
            membersDto.add(MembersDto.builder()
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .build());
        }
        return membersDto;
    }
}