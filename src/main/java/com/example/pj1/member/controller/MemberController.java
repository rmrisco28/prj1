package com.example.pj1.member.controller;

import com.example.pj1.member.dto.MemberForm;
import com.example.pj1.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("signup")
    public String signup() {

        return "member/signup";
    }

    @PostMapping("signup")
    public String signup(MemberForm data, RedirectAttributes rttr) {
        try {
            // service
            memberService.add(data);
            rttr.addFlashAttribute("alert",
                    Map.of("code", "success", "message", "회원 가입되었습니다."));

            return "redirect:/board/list";
        } catch (DuplicateKeyException e) {
            rttr.addFlashAttribute("alert",
                    Map.of("code", "warning", "message", e.getMessage()));
            // 작성했던 다른 값이 사라지게 하고 싶지 않다.
            // data에서 받은 값이 model에 들어가있으니까,
            rttr.addFlashAttribute("member", data);
            return "redirect:/member/signup";
        }
    }

    @GetMapping("list")
    public String list(Model model) {
        model.addAttribute("memberList", memberService.list());

        return "member/list";
    }

}
