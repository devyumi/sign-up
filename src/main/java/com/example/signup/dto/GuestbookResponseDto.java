package com.example.signup.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class GuestbookResponseDto {
    private String nickname;
    private String content;
    private LocalDateTime createDate;

    @Builder
    public GuestbookResponseDto(String nickname, String content, LocalDateTime createDate) {
        this.nickname = nickname;
        this.content = content;
        this.createDate = createDate;
    }
}