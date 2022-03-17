package com.gemiso.zodiac.core.helper;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateChangeHelper {

    //String To Date
    public Date stringToDateNoComma(String str) throws ParseException {

        Date returnDate = new Date();

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyymmddhhmmss");

        if (str != null && str.trim().isEmpty() == false){
            returnDate = transFormat.parse(str);
        }

        return returnDate;
    }

    //Date형식을 String으로 파싱
    public String dateToStringNormal(Date date) {

        String returnDate = "";

        if (ObjectUtils.isEmpty(date) == false) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            returnDate = simpleDateFormat.format(date);
        }
        return returnDate;
    }

    //Date형식을 String으로 파싱
    public Date StringToDateNormal(String str) throws ParseException {

        Date returnDate = new Date();

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (str != null && str.trim().isEmpty() == false){
            returnDate = transFormat.parse(str);
        }

        return returnDate;
    }
}
