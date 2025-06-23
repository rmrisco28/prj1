package com.example.pj1.member.repository;

import com.example.pj1.member.dto.MemberListInfo;
import com.example.pj1.member.entity.Member;
import com.example.pj1.member.service.MemberService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByNickName(String nickName);

    List<MemberListInfo> findAllBy();

}