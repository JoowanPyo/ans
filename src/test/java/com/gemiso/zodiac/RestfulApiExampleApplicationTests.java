package com.gemiso.zodiac;

import com.gemiso.zodiac.core.service.BisInterfaceService;
import com.mysema.commons.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class RestfulApiExampleApplicationTests {

    @Autowired
    private BisInterfaceService bisInterfaceService;

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

    @Test
    void bisMapTest() throws Exception {
        System.out.println("----------test start-----------");
        /*BisInterfaceService bisInterfaceService = new BisInterfaceService();*/
        /*bisInterfaceService.restTemplateTest();*/
        bisInterfaceService.bisProgramfindAll();
        System.out.println("----------test end-----------");

    }

    @Test
    void bisBasicTest() throws Exception {

        bisInterfaceService.bisBasicSchedulefindAll();
    }

    @Test
    void password(){

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean isMatch = encoder.matches("1234", "$2a$10$AmMoftfjRv/VFgO3Qs8ZBOqAsOc3Q5JXgf.E0sF42HJrDQL8gloXi");
        Assert.isTrue(isMatch, "error");

        System.out.println("boolean : " + isMatch);

    }


}
