package com.example.pj1.service;

import com.example.pj1.dto.BoardForm;
import com.example.pj1.dto.BoardListInfo;
import com.example.pj1.entity.Board;
import com.example.pj1.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    public void add(BoardForm formData) {
        Board board = new Board();
        board.setTitle(formData.getTitle());
        board.setContent(formData.getContent());
        board.setWriter(formData.getWriter());

        boardRepository.save(board);
    }

    public List<BoardListInfo> list(Integer page) {
//        List<Board> list = boardRepository.findAll();
        // 바로 게시물의 본문까지 가져올 필요없다.
        // 필요한 정보만 가져오도록 변경
        List<BoardListInfo> boardList = boardRepository
                .findAllBy(PageRequest.of(page, 10, Sort.by("id").descending()));

        return boardList;
    }


}
