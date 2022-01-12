package com.gemiso.zodiac.core.helper;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HexHelper {

    private String pwd;

    public HexHelper(String pwd) throws NoSuchAlgorithmException {

        String raw = pwd;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(raw.getBytes());
        String hex = String.format("%064x", new BigInteger(1, md.digest()));

        this.pwd=hex;
    }
}
