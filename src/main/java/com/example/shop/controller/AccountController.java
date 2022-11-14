package com.example.shop.controller;

import com.example.shop.entity.Member;
import com.example.shop.repository.MemberRepository;
import com.example.shop.service.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class AccountController {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtService jwtService;

    @PostMapping("/api/account/login") //params라는 이름으로 인자값을 받아서, findby 매서드에 넘겨서 id값 리턴
    public ResponseEntity login(@RequestBody Map<String, String> params,
                                HttpServletResponse res) {
        Member member = memberRepository.findByEmailAndPassword(params.get("email"), params.get("password"));

        if (member != null) {
            int id = member.getId();                // 1. id 값을 받아온다.
            String token = jwtService.getToken("id", id); // 2. 받아온 id 값을 토큰화(string) 한다.

            Cookie cookie = new Cookie("token", token); // 3. 토큰화된 id값을 cookie에 저장한다.
            cookie.setHttpOnly(true); // js로는 접근 할 수 없도록 처리 (setHttpOnly 매서드 활용)
            cookie.setPath("/");

            res.addCookie(cookie);  // 추가된 쿠키값을 response 변수에 넘겨 응답한다.

            return new ResponseEntity<>(id, HttpStatus.OK); // id값을 응답값으로 준다.
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/api/account/logout") //params라는 이름으로 인자값을 받아서, findby 매서드에 넘겨서 id값 리턴
    public ResponseEntity logout(HttpServletResponse res) {

        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        res.addCookie(cookie);


        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping("/api/account/check")
    public ResponseEntity check(@CookieValue(value = "token", required = false) String token) {
        Claims claims = jwtService.getClaims(token);    // 토큰 값 받아오기

        if (claims != null) {
            int id = Integer.parseInt(claims.get("id").toString()); // 토큰 값 파싱
            return new ResponseEntity<>(id, HttpStatus.OK); // id값을 응답값으로 준다.

        }

        return new ResponseEntity<>(null, HttpStatus.OK); // claims 값이 null이라면, header에 null값을 반환
    }
}
