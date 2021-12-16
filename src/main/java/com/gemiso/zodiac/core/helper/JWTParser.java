package com.gemiso.zodiac.core.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JWTParser {

    @Value("${jwt.auth.secret-key:secret}")
    private String secretKey;

    public int verityJwt(String refreshToken) throws Exception {

        int return_expiration = 0;

        try {

            Map<String, Object> claimMap = null;
            Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes("UTF-8")) // Set Key
                    .parseClaimsJws(refreshToken) // 파싱 및 검증, 실패 시 에러
                    .getBody();

            claimMap = claims;

            Date expiration = claims.get("exp", Date.class);
            Date nowDate = new Date();

            return_expiration = (int) (expiration.getTime() - nowDate.getTime());
            return_expiration = return_expiration / 1000;

        } catch (ExpiredJwtException e) { // 토큰이 만료되었을 경우
            System.out.println(e);
           /* e.printStackTrace();*/

        } catch (JwtException e) { // 그외 에러났을 경우
            System.out.println(e);
            /*e.printStackTrace();*/

        }
        return return_expiration;
    }

    public Date refreshTokenParser(String refreshToken) throws UnsupportedEncodingException {

        Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes("UTF-8")) // Set Key
                .parseClaimsJws(refreshToken) // 파싱 및 검증, 실패 시 에러
                .getBody();

        // 리플레시 토큰 만료 시간
        Date expiration = claims.get("exp", Date.class);

        return expiration;

    }

    //jwt파싱으로 하면 토큰만료시간이 되면 에러가 나기때문에 만료시간에러없이 파싱하는 서비스
    public String refreshTokenVerification(String refreshToken) throws ParseException {

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] parts = refreshToken.split("\\."); // Splitting header, payload and signature

        String exp = new String(decoder.decode(parts[1]));

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(exp);
        JSONObject jsonObj = (JSONObject) obj;

        // long 변환하여 객체 생성
        long ext = (long) jsonObj.get("exp");

        Date date = new Date();

        long DateTime = date.getTime();

        ext = ext * 1000; // 바로 1000을 곱한 수를 넣으면 범위 초과 에러가 나기 때문에.

        Date TokenDate = new Date(ext);
        Date nowDate = new Date(DateTime);

        // 토큰만료 됫을경우 return null
        if (TokenDate.before(nowDate)) {
            return "";
        }
        return refreshToken;
    }

    public String acTokenParser(String Authorization) throws ParseException {

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] parts = Authorization.split("\\."); // Splitting header, payload and signature

        String userId = new String(decoder.decode(parts[1]));

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(userId);
        JSONObject jsonObj = (JSONObject) obj;

        String returnUserId = (String) jsonObj.get("userId");

        return returnUserId;

    }


   /* public void checkRefleshToken(String reflesh_token, String user_id) throws Exception {

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] parts = reflesh_token.split("\\."); // Splitting header, payload and signature

        String exp = new String(decoder.decode(parts[1]));

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(exp);
        JSONObject jsonObj = (JSONObject) obj;

        // long 변환하여 객체 생성
        long ext = (long) jsonObj.get("exp");

        Date date = new Date();

        long DateTime = date.getTime();

        ext = ext * 1000; // 바로 1000을 곱한 수를 넣으면 범위 초과 에러가 나기 때문에.

        Date TokenDate = new Date(ext);
        Date nowDate = new Date(DateTime);

        // 토큰만료 됫을경우 return null
        if (TokenDate.before(nowDate)) {
            userDAO.deleteRefreshToken(user_id);
        }

    }*/
}
