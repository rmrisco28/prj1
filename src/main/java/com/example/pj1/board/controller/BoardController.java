package com.example.pj1.board.controller;

import com.example.pj1.board.dto.BoardForm;
import com.example.pj1.board.service.BoardService;
import com.example.pj1.member.dto.MemberDto;
import com.example.pj1.member.entity.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("write")
    public String writeForm(HttpSession session, RedirectAttributes rttr) {
        Object result = session.getAttribute("loggedInUser");
        // 로그인 여부로 다르게 표시하기
        if (result != null) {
            // 로그인 됐을 때
            return "board/write";
        } else {
            // 로그인 안됐을 때
            rttr.addFlashAttribute("alert",
                    Map.of("code", "warning",
                            "message", "로그인 후 글을 작성해주세요.."));


            return "redirect:/member/login";

        }
    }

    /*    @PostMapping("write")
        public String writePost(String title, String content, String writer) {
            System.out.println("title = " + title);
            System.out.println("content = " + content);
            System.out.println("writer = " + writer);
            return "board/write";
        }*/ // 컨트롤러에 일일이 다 쓸 수 없으니 dto를 만들어서 아래처럼 사용
    @PostMapping("write")
    public String writePost(BoardForm data,
                            @SessionAttribute(name = "loggedInUser", required = false)
                            MemberDto user,
                            RedirectAttributes rttr) {

        if (user != null) {
            boardService.add(data, user);
            rttr.addFlashAttribute("alert",
                    Map.of("code", "primary", "message", " 새 게시물이 등록되었습니다."));
            return "redirect:/board/list"; // 글 쓰고 새로고침 방지: redirect:
        } else {
            return "redirect:/member/login";
        }
    }

    @GetMapping("list")
    public String list(
            @RequestParam(defaultValue = "1")
            Integer page,
            Model model) {

        var result = boardService.list(page);

//        model.addAttribute("boardList", result);
        model.addAllAttributes(result);
        return "board/list";
    }

    @GetMapping("view")
    public String view(Integer id, Model model) {
        // TODO: 임시로 만들었기 때문에
        //service로 일 시키고
        var dto = boardService.get(id);

        // model에 넣고
        model.addAttribute("board", dto);

        // view로 forward
        return "board/view";
    }

    @PostMapping("remove")
    public String remove(Integer id, RedirectAttributes rttr) {
        boardService.remove(id);
        rttr.addFlashAttribute("alert",
                Map.of("code", "danger", "message", id + "번 게시물이 삭제되었습니다."));


        return "redirect:/board/list";
    }

    @GetMapping("edit")
    public String edit(Integer id, Model model) {
        var dto = boardService.get(id);
        model.addAttribute("board", dto);
        return "board/edit";
    }

    @PostMapping("edit")
    public String editPost(BoardForm data, RedirectAttributes rttr) {
        boardService.update(data);
        rttr.addFlashAttribute("alert",
                Map.of("code", "success", "message",
                        data.getId() + "번 게시물이 수정되었습니다."));
        rttr.addAttribute("id", data.getId());
        return "redirect:/board/list";
    }
}
