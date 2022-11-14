package com.example.shop.repository;

import com.example.shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    List<Cart> findByMemberId(int memberId);    // 회원 id에 있는 카트 정보 가져오기
    Cart findByMemberIdAndItemId(int memberId, int itemId); // 회원 id와 상품 id 가져오기
}
