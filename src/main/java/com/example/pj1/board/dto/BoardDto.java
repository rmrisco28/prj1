package com.example.pj1.board.dto;

import com.example.pj1.board.entity.Board;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Board}
 */
//@Value
@Data
public class BoardDto implements Serializable {
    Integer id;
    String title;
    String content;
    String writer;
    LocalDateTime createdAt;
}