package com.example.shop.service;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("jwtService")
public class JwtServiceImpl implements JwtService{

    private String secretKey = "acacdfet!!!!!secretKey###!!-@@@avbbeeddaaf1234!!4-test-jwt-secretKey";
    @Override
    public String getToken(String key, Object value) {

        Date expTime = new Date();
        expTime.setTime(expTime.getTime()+1000*60*30);  //30분간 유효시간 유지

        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
        Key signKey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("type", "JWT");
        headerMap.put("alg", "HS256");

        Map<String, Object> map = new HashMap<>();
        map.put(key, value);

        JwtBuilder builder= Jwts.builder().setHeader(headerMap)
                .setClaims(map)
                .setExpiration(expTime)
                .signWith(signKey, SignatureAlgorithm.HS256);

        return builder.compact();

    }

    @Override
    public Claims getClaims(String token) {
        if (token != null && !"".equals(token)
        ) {
            try{
                byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
                Key signKey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());
                return Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token).getBody();
            }catch (ExpiredJwtException e){
                // jwt 만료됨
            }catch (JwtException e){
                // jwt 유효하지 않음
            }
        }
        return null;
    }

    @Override
    public boolean isValid(String token) {

        return this.getClaims(token) != null;   // 토큰값이 null이 아니라면, 문제 없음(회원id 정상 처리 완료)
    }

    @Override
    public int getId(String token) {
        Claims claims = this.getClaims(token);

        if(claims != null){
            return Integer.parseInt(claims.get("id").toString());
        }

        return 0;
    }
}
