package com.example.pj1.board.dto;

import com.example.pj1.member.dto.MemberListInfo;

import java.time.LocalDateTime;

public interface BoardListInfo {
    public Integer getId();

    public String getTitle();

    public MemberListInfo getWriter();

    public LocalDateTime getCreatedAt(); // 생략 가능?

}

