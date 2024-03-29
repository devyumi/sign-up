package com.example.signup.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class MembersDto {
    private String email;
    private String nickname;

    @Builder
    public MembersDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}