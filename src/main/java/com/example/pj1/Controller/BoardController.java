package com.example.pj1.Controller;

import com.example.pj1.dto.BoardForm;
import com.example.pj1.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("write")
    public String writeForm() {

        return "board/write";
    }

    /*    @PostMapping("write")
        public String writePost(String title, String content, String writer) {
            System.out.println("title = " + title);
            System.out.println("content = " + content);
            System.out.println("writer = " + writer);
            return "board/write";
        }*/ // 컨트롤러에 일일이 다 쓸 수 없으니 dto를 만들어서 아래처럼 사용
    @PostMapping("write")
    public String writePost(BoardForm data) {

        boardService.add(data);
        return "redirect:/board/write"; // 글 쓰고 새로고침 방지: redirect:
    }

    @GetMapping("list")
    public String list(
            @RequestParam(defaultValue = "1")
            Integer page,
            Model model) {

        var result = boardService.list(page);

        model.addAttribute("boardList", result);
        return "board/list";
    }
}
