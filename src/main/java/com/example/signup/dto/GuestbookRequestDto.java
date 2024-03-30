package com.example.signup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
public class GuestbookRequestDto {
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 5, message = "5자 이상 입력해주세요.")
    private String content;
}