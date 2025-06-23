package com.example.pj1.member.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberDto {
    private String id;
    private String nickName;
    private String password;
    private String info;
    private LocalDateTime createdAt;
}
