package com.gemiso.zodiac.app.lbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemiso.zodiac.app.lbox.dto.DataDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class LboxService {



    //엘박스 영상정보 조회 [anm]
    public DataDTO findAll(String sdate, String edate) throws JsonProcessingException {

        //헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        //httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setContentLength(999999999999999999L);


        Map<String, Object> param = new HashMap<>();
        param.put("start_date", sdate);
        param.put("end_date", edate);

        ObjectMapper objectMapper = new ObjectMapper();
        String params = objectMapper.writeValueAsString(param);

        HttpEntity entity = new HttpEntity(params, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity =
                restTemplate.exchange("https://dev-nam.arirang.com/api/ans/v2/contents", HttpMethod.GET, entity, String.class );

        //DataDTO dataDTO = responseEntity.getBody();

        String results = responseEntity.getBody();
        System.out.println(results);
        DataDTO dataDTO = new DataDTO();
        dataDTO = objectMapper.readValue(results, DataDTO.class);

         return dataDTO;

    }
}
