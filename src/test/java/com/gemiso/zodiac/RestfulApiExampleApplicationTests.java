package com.gemiso.zodiac;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemiso.zodiac.app.stats.StatsService;
import com.gemiso.zodiac.core.scheduling.BisInterfaceService;
import com.gemiso.zodiac.core.topic.TopicSendService;
import com.mysema.commons.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@SpringBootTest
class RestfulApiExampleApplicationTests {

    @Autowired
    private BisInterfaceService bisInterfaceService;

    @Autowired
    private StatsService statsService;

    @Test
    void contextLoads() {

        Long[] longArr = new Long[4];
        longArr[0] = 123L;
        longArr[1] = 123L;
        longArr[2] = 123L;
        longArr[3] = 1234L;



       // boolean ret = ProcessArticleFix.articleFix( FixEnum.ANCHOR_FIX, FixEnum.FIX_NONE , "gemiso", "joowan");

    }

    @Test
    void bisMapTest() throws Exception {
        bisInterfaceService.bisProgramfindAll();

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


    }

    @Test //Bis 방송길이 와 방송시작시작으로 방송종료시간 구하기
    void getEndTime() throws ParseException {
        String endtime = bisInterfaceService.getEndTime("130","1230");
    }

    @Test
    void  testJson() throws JsonProcessingException {

        String json = "{\"title\": \"0525 17 \uae40\uc138\uc644 \uc99d\uc2dc\uc5f0\uacb0 1_2 \uc720\ub7fd \uc544\uc2dc\uc544 \uc99d\uc2dc\"}";


        ObjectMapper mapper = new ObjectMapper();


            // convert JSON string to Map
            Map<String, String> map = mapper.readValue(json, Map.class);

            // it works
            //Map<String, String> map = mapper.readValue(json, new TypeReference<Map<String, String>>() {});

            System.out.println(map);


    }

    @Test
    void statsCreate(){

        statsService.create();
    }

}
