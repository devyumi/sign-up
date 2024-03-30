package com.example.signup.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "guestbook")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Guestbook extends Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Guestbook(Long id, String content, Member member) {
        this.id = id;
        this.content = content;
        this.member = member;
    }
}