package com.example.shop.controller;

import com.example.shop.entity.Item;
import com.example.shop.entity.Member;
import com.example.shop.repository.ItemRepository;
import com.example.shop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AccountController {

    @Autowired
    MemberRepository memberRepository;

    @GetMapping("/api/account/login")
    public int login(
            @RequestBody Map<String, String> params
    ) {
        Member member = memberRepository.findByEmailAndPassword(params.get("email"), params.get("password"));

        if (member != null) {
            return member.getId();
        }
        return 0;
    }
}
