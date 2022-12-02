package com.gemiso.zodiac.core.helper;

import org.springframework.stereotype.Component;

@Component
public class CapSeparatorHelper {

    public String separator(String ctt){

        //ctt = ctt+System.lineSeparator();
        if (ctt != null && ctt.trim().isEmpty() == false) {
            ctt = ctt + "\n";
        }


        return ctt;
    }
}
