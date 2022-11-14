package com.example.shop.repository;

import com.example.shop.entity.Item;
import com.example.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Member findByEmailAndPassword(String email, String password);
}
