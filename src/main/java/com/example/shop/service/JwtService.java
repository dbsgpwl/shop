package com.example.shop.service;

import io.jsonwebtoken.Claims;

public interface JwtService {
    String getToken(String key, Object value);

    Claims getClaims(String token);

    boolean isValid(String token);  // 토큰에 문제가 없는지 확인

    int getId(String token);
}
