package com.example.signup.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Member extends Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accessToken;
    private String email;
    private String password;
    private String nickname;

    @Builder
    public Member(Long id, String accessToken, String email, String password, String nickname) {
        this.id = id;
        this.accessToken = accessToken;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}