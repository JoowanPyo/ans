package com.gemiso.zodiac;

import com.gemiso.zodiac.core.fixEnum.FixEnum;
import com.gemiso.zodiac.core.service.ProcessArticleFix;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestfulApiExampleApplicationTests {

    @Test
    void contextLoads() {

        System.out.println( "hello world" );

        boolean ret = ProcessArticleFix.isReporter( FixEnum.ANCHOR_FIX, FixEnum.FIX_NONE , "gemiso", "joowan");

    }

}
