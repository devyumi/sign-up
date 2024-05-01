package com.example.signup.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String token;

    @Builder
    public RefreshToken(Long id, Member member, String token) {
        this.id = id;
        this.member = member;
        this.token = token;
    }
}