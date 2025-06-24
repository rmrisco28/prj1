package com.example.pj1.board.service;

import com.example.pj1.board.dto.BoardDto;
import com.example.pj1.board.dto.BoardForm;
import com.example.pj1.board.dto.BoardListInfo;
import com.example.pj1.board.entity.Board;
import com.example.pj1.board.repository.BoardRepository;
import com.example.pj1.member.dto.MemberDto;
import com.example.pj1.member.entity.Member;
import com.example.pj1.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    public void add(BoardForm formData, MemberDto user) {
        System.out.println(formData);
        System.out.println(user);
        Board board = new Board();
        board.setTitle(formData.getTitle());
        board.setContent(formData.getContent());

        // board.setWriter(user.getId()); // 멤버 타입으로 바꼈다.
        Member member = memberRepository.findById(user.getId()).get();
        board.setWriter(member);


        boardRepository.save(board);
    }

    public Map<String, Object> list(Integer page) {
//        List<Board> list = boardRepository.findAll();
        // 바로 게시물의 본문까지 가져올 필요없다.
        // 필요한 정보만 가져오도록 변경
        Page<BoardListInfo> boardPage = boardRepository
                .findAllBy(PageRequest.of(page - 1, 10, Sort.by("id").descending()));

        List<BoardListInfo> boardList = boardPage.getContent();

        Integer rightPageNumber = ((page - 1) / 10 + 1) * 10;
        Integer leftPageNumber = rightPageNumber - 9;
        rightPageNumber = Math.min(rightPageNumber, boardPage.getTotalPages());

//        // 마지막 페이지확인하기
//        boardPage.getTotalElements();
//        boardPage.getTotalPages();

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

        MemberDto memberDto = new MemberDto();
        memberDto.setId(board.getWriter().getId());
        memberDto.setNickName(board.getWriter().getNickName());

        dto.setWriter(memberDto);
        dto.setCreatedAt(board.getCreatedAt());
//        dto.setWriter(board.getWriter());
        dto.setCreatedAt(board.getCreatedAt());

        return dto;
    }

    public void remove(Integer id) {
        boardRepository.deleteById(id);
        // 지웠을때 지운 알림을 내고 싶을 때
        // 삭제
    }

    public void update(BoardForm data) {
        // 조회
        Board board = boardRepository.findById(data.getId()).get();
        // 수정
        board.setTitle(data.getTitle());
        board.setContent(data.getContent());


        // 저장
        boardRepository.save(board);
    }
}
