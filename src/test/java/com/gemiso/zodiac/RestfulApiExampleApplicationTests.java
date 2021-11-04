package com.gemiso.zodiac;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestfulApiExampleApplicationTests {


    @Test
    void contextLoads() {

        Long[] longArr = new Long[4];
        longArr[0] = 123L;
        longArr[1] = 123L;
        longArr[2] = 123L;
        longArr[3] = 1234L;


        System.out.println( longArr.toString());

       // boolean ret = ProcessArticleFix.articleFix( FixEnum.ANCHOR_FIX, FixEnum.FIX_NONE , "gemiso", "joowan");

    }


}
