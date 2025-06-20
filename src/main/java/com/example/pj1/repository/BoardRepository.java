package com.example.pj1.repository;

import com.example.pj1.dto.BoardListInfo;
import com.example.pj1.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<BoardListInfo> findAllBy();

    List<BoardListInfo> findAllBy(Pageable pageable);

    // 눈에 안보이지만 jpa 의 기본적인 crud가 있다.
    // 필요하다면 sql, jpql, query method 작성해서 쓰면 된다.
}