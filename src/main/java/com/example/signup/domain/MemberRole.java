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
    private String roleName;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public MemberRole(Long id, String roleName, Member member) {
        this.id = id;
        this.roleName = roleName;
        this.member = member;
    }
}