package com.gemiso.zodiac.core.service;

import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.UserRepository;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtGetUserService {

    private final UserRepository userRepository;

    @Value("${jwt.auth.secret-key:secret}")
    private String secretKey;

    public String getUser(String getJwtToken) throws UnsupportedEncodingException {

        String jwtToken = null;
        String userId = null;

        try {

            if (getJwtToken != null && getJwtToken.startsWith("Bearer")) {

                //헤더에 있는 Bearer Substring => 토큰값을 빼기 위함.
                jwtToken = getJwtToken.substring(6);
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey.getBytes("UTF-8")) // Set Key
                        .parseClaimsJws(jwtToken) // 파싱 및 검증, 실패 시 에러
                        .getBody();


/*                Date expiration = claims.get("exp", Date.class);
                Date nowDate = new Date();

                //토큰만료 시간 체크
                if (expiration.before(nowDate)) {
                    log.error("User Token Parsing Time Error ");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                }*/

                userId = claims.get("userId", String.class);

                String findUser = userId;

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("UserId not found. UserId : " + findUser));


            }
        } catch (ExpiredJwtException ex) {
            log.error("User Token Parsing Error "+ex.toString());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);


        }

        return userId;
    }
}
