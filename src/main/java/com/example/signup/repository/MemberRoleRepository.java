package com.example.signup.repository;

import com.example.signup.domain.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {
    List<MemberRole> findAllByMemberId(Long memberId);
}