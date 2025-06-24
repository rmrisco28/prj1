package com.example.pj1.member.service;

import com.example.pj1.member.dto.MemberDto;
import com.example.pj1.member.dto.MemberForm;
import com.example.pj1.member.dto.MemberListInfo;
import com.example.pj1.member.entity.Member;
import com.example.pj1.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
            Optional<Member> byNickName = memberRepository.findByNickName(data.getNickName());
            if (byNickName.isEmpty()) { // 없으면
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
                throw new DuplicateKeyException(data.getNickName() + "는 이미 있는 별명입니다.");
            }
        } else {
            // 있으면 익셉션을 발생시킨다.
            throw new DuplicateKeyException(data.getId() + "는 이미 있는 아이디입니다.");
        }

    }

    public List<MemberListInfo> list() {
        // entity의 모든 값을 가져올 필요없으니까
        // id, nick 프로젝션 인터페이스를 추가로 만듭니다.
        // MemberListInfo.interface
        return memberRepository.findAllBy();
    }

    public Object get(String id) {
        Member member = memberRepository.findById(id).get();
        // 암호까지 넘겨줄 필요 없으니까
        // 새로 dto 만들기
        MemberDto dto = new MemberDto();
        dto.setId(member.getId());
        dto.setNickName(member.getNickName());
        dto.setInfo(member.getInfo());
        dto.setCreatedAt(member.getCreatedAt());
        return dto;
    }

    public boolean remove(MemberForm data) {
        Member member = memberRepository.findById(data.getId()).get();
        String dbPw = member.getPassword();
        String formPw = data.getPassword();

        if (dbPw.equals(formPw)) {
            memberRepository.delete(member);
            return true;
        } else {
            return false;
        }
    }

    public boolean updata(MemberForm data) {
        // 조회
        Member member = memberRepository.findById(data.getId()).get();

        String dbPw = member.getPassword();
        String formPw = data.getPassword();

        if (dbPw.equals(formPw)) {
            // 변경
            member.setNickName(data.getNickName());
            member.setInfo(data.getInfo());
            // 저장
            memberRepository.save(member);
            return true;
        } else {
            return false;
        }
    }

    public boolean updatePassword(String id, String oldPassword, String newPassword) {
        // 꼭 조회를 해야함. 안그러면 나머지 값들이 null 로 변경된다.
        Member db = memberRepository.findById(id).get();
        String dbPw = db.getPassword();
        if (dbPw.equals(oldPassword)) {
            db.setPassword(newPassword);
            memberRepository.save(db);

            return true;
        } else {
            return false;
        }
    }

    public boolean login(String id, String password, HttpSession session) {
        Optional<Member> db = memberRepository.findById(id);
        if (db.isPresent()) {
            String dbPassword = db.get().getPassword();
            if (dbPassword.equals(password)) {
                // memberDto를 session에 넣기
                MemberDto dto = new MemberDto();
                dto.setId(db.get().getId());
                dto.setNickName(db.get().getNickName());
                dto.setInfo(db.get().getInfo());
                dto.setCreatedAt(db.get().getCreatedAt());
                // TODO: 패스워드 검색
                session.setAttribute("loggedInUser", dto);
                return true;
            }
        }
        return false;
    }

}
