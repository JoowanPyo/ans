package com.gemiso.zodiac.core.helper;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ArirangHexHelper {


    private String pwd;

    public ArirangHexHelper(String pwd, String saltKey) throws NoSuchAlgorithmException {

        String raw = pwd+saltKey;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(raw.getBytes());
        String hex = String.format("%064x", new BigInteger(1, md.digest()));

        this.pwd=hex;

    }

    public String hexPwd(){ return this.pwd; }
}


/*
    byte[] salt = saltKey.getBytes();
    String hex = "";

    byte[] temp = pwd.getBytes();
    byte[] bytes = new byte[temp.length + salt.length];

        try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(bytes);

                byte[] b = md.digest();

                StringBuffer sb = new StringBuffer();

                for(int i=0; i<b.length; i++) {
        sb.append(Integer.toString((b[i] & 0xFF) + 256, 16).substring(1));
        }

        hex = sb.toString();

        } catch (Exception e) {
        log.error(e.getLocalizedMessage());
        }



        this.pwd=hex;

        }*/
