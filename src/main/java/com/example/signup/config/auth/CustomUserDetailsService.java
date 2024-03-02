package com.example.signup.config.auth;

import com.example.signup.domain.Member;
import com.example.signup.domain.MemberRole;
import com.example.signup.repository.MemberRepository;
import com.example.signup.repository.MemberRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private CustomUserDetails createUserDetails(Member member) {
        List<MemberRole> customRoles = memberRoleRepository.findAllByMemberId(member.getId());
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (!customRoles.isEmpty()) {
            for (MemberRole memberRole : customRoles) {
                authorities.add(new SimpleGrantedAuthority(memberRole.getRoleName()));
            }
        }

        return CustomUserDetails.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .authorities(authorities)
                .build();
    }
}