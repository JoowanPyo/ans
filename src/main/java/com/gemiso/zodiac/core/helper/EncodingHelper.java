package com.gemiso.zodiac.core.helper;

import org.springframework.beans.factory.annotation.Value;

import java.security.NoSuchAlgorithmException;

public class EncodingHelper{

    @Value("${password.salt-key:saltKey}")
    private String saltkey;

    private String pwd;

    public EncodingHelper(String pwd) throws NoSuchAlgorithmException {

        //아리랑 전용 salt key 사용 해싱
        ArirangHexHelper arirangHexHelper = new ArirangHexHelper(pwd);
        // 솔루션 사용시 Key없이 sha256해싱
        //HexHelper hexHelper = new HexHelper(pwd);

        String hex = arirangHexHelper.hexPwd();

        this.pwd = hex;
    }

    public String getHex(){return this.pwd;}
}
