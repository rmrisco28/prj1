package com.example.pj1.service;

import com.example.pj1.dto.BoardDto;
import com.example.pj1.dto.BoardForm;
import com.example.pj1.dto.BoardListInfo;
import com.example.pj1.entity.Board;
import com.example.pj1.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    public Map<String, Object> list(Integer page) {
//        List<Board> list = boardRepository.findAll();
        // 바로 게시물의 본문까지 가져올 필요없다.
        // 필요한 정보만 가져오도록 변경
        Page<BoardListInfo> boardPage = boardRepository
                .findAllBy(PageRequest.of(page, 10, Sort.by("id").descending()));

        Integer rightPageNumber = ((page - 1) / 10 + 1) * 10;
        Integer leftPageNumber = rightPageNumber - 9;
        rightPageNumber = Math.min(rightPageNumber, boardPage.getTotalPages());

        // 마지막 페이지확인하기
        List<BoardListInfo> boardList = boardPage.getContent();
        boardPage.getTotalElements();
        boardPage.getTotalPages();

        var result = Map.of("boardList", boardList,
                "totalElements", boardPage.getTotalElements(),
                "totalPages", boardPage.getTotalPages(),
                "rightPageNumber", rightPageNumber,
                "leftPageNumber", leftPageNumber,
                "currentPage", page);

        return result;
    }

    public BoardDto get(Integer id) {
        Board board = boardRepository.findById(id).get();
        // Dto 가서 만들기
        BoardDto dto = new BoardDto();
        dto.setId(id);
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setWriter(board.getWriter());
        dto.setCreatedAt(board.getCreatedAt());

        return dto;
    }
}
