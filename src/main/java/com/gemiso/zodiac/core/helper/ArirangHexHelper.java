package com.gemiso.zodiac.core.helper;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ArirangHexHelper {

    @Value("${password.salt-key:saltKey}")
    private String saltKey;

    private String pwd;

    public ArirangHexHelper(String pwd) throws NoSuchAlgorithmException {

        String raw = pwd+saltKey;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(raw.getBytes());
        String hex = String.format("%064x", new BigInteger(1, md.digest()));

        this.pwd=hex;

    }

    public String hexPwd(){ return this.pwd; }
}
