package com.example.shop.controller;

import com.example.shop.entity.Cart;
import com.example.shop.entity.Item;
import com.example.shop.repository.CartRepository;
import com.example.shop.repository.ItemRepository;
import com.example.shop.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CartController {

    @Autowired
    JwtService jwtService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ItemRepository itemRepository;

    /*
        장바구니 목록 조회
            - itemId를 리스트화하여 상품정보를 추출->리턴
     */
    @GetMapping("/api/cart/items")
    public ResponseEntity getCartItems(@CookieValue(value = "token", required = false) String token) {
        if (jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //token값이 유효하지 않다면, 401에러 반환
        }
        int memberId = jwtService.getId(token);
        List<Cart> carts = cartRepository.findByMemberId(memberId); // cart에 있는 memberId를 찾기 -> itemid, memberid
        List<Integer> itemIds = carts.stream().map(Cart::getItemId).collect(Collectors.toList()); // itemId를 추출하여, list 형태로 값을 반환

        List<Item> items = itemRepository.findByIdIn(itemIds);  // 상품정보 추출

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    /*
        장바구니 목록 추가
            - 특정 상품코드를 파라미터 값으로 요청
            - token 값 확인 -> 유효하지 않으면 401 에러 반환 / 유효하면 토큰값에 있는 회원 id 값 추출
     */
    @PostMapping("/api/cart/items/{itemId}")
    public ResponseEntity pushCartItem(
            @PathVariable("itemId") int itemId,
            @CookieValue(value = "token", required = false) String token
    ) {
        if (jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        Cart cart = cartRepository.findByMemberIdAndItemId(memberId, itemId);

        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setMemberId(memberId);   // 쿠키에서 받은 memberId
            newCart.setItemId(itemId);       // 인자로 받은 itemId
            cartRepository.save(newCart);    // 새로운 장바구니 코드 추가
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}