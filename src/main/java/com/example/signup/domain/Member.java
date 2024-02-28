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
    private String platformType;
    private String accessToken;
    private String email;
    private String password;
    private String nickname;
    private Integer status;

    @Builder
    public Member(Long id, String platformType, String accessToken, String email, String password, String nickname, Integer status) {
        this.id = id;
        this.platformType = platformType;
        this.accessToken = accessToken;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.status = status;
    }
}