package com.gemiso.zodiac.core.helper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTBuilder {

    @Value("${jwt.auth.secret-key:secret}")
    private String secretKey;

    public String createAccessToken(String userId) {

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("userId", userId);
        //Long expiredTime = 1000 * 60L * 60L * 2L;
        Long expiredTime = 1000 * 60L;

        //expiredTime, subject --cloud config로 처리??
        return createToken(payloads, expiredTime, "AccessToken");

    }

    //Refresh 토큰 생성
    public String createRefreshToken(String userId) {

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("", userId);
        //Long expirationIn =  1000 * 60L * 60L * 24L;
        Long expirationIn =  1000 * 60L;
        return createToken(payloads, expirationIn, "RefreshToken");
    }


    public String createToken(Map<String, Object> payloads, Long expiredTime, String subject) {

        //Header 부분 설정
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        //토큰만료시간 설정
        Date ext = new Date(); // 토큰 만료 시간
        ext.setTime(ext.getTime() + expiredTime);

        // 토큰 Builder
        String jwt = Jwts.builder()
                .setHeader(headers) // Headers 설정
                .setClaims(payloads) // Claims 설정
                .setSubject(subject) // 토큰 용도
                .setExpiration(ext) // 토큰 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes()) // HS256과 Key로 Sign
                .compact(); // 토큰 생성

        return jwt;
    }

}