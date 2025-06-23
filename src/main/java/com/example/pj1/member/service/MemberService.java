package com.example.pj1.member.service;

import com.example.pj1.member.dto.MemberForm;
import com.example.pj1.member.entity.Member;
import com.example.pj1.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public void add(MemberForm data) {
        // id가 먼저 있는지 확인하고싶다.
        Optional<Member> db = memberRepository.findById(data.getId());
        if (db.isEmpty()) {
            // 아이디가 없으면
            // 새 entity 객체 생성해서
            Member member = new Member();
            // data에 있는 것 entity에 옮겨 담고
            member.setId(data.getId());
            member.setPassword(data.getPassword());
            member.setNickName(data.getNickName());
            member.setInfo(data.getInfo());
            // repository.save()
            memberRepository.save(member);
        } else {
            // 있으면 익셉션을 발생시킨다.
            throw new DuplicateKeyException(data.getId() + "는 이미 있는 아이디입니다.");
        }

    }
}
