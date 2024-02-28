package com.example.signup.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member_role")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class MemberRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String role_name;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public MemberRole(Long id, String role_name, Member member) {
        this.id = id;
        this.role_name = role_name;
        this.member = member;
    }
}