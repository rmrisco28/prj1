package com.example.pj1.board.repository;

import com.example.pj1.board.dto.BoardListInfo;
import com.example.pj1.board.entity.Board;
import com.example.pj1.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<BoardListInfo> findAllBy();

    Page<BoardListInfo> findAllBy(Pageable pageable);

    void deleteByWriter(Member member);

    @Query("""
            SELECT b
            FROM Board b
            WHERE b.title LIKE: keyword
            OR b.content LIKE : keyword
            OR b.writer.nickName LIKE :keyword
            """)
    Page<BoardListInfo> searchByKeyword(String keyword, PageRequest id);

    // 눈에 안보이지만 jpa 의 기본적인 crud가 있다.
    // 필요하다면 sql, jpql, query method 작성해서 쓰면 된다.
}