package com.gemiso.zodiac.app.lbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.app.lbox.categoriesDTO.CategoriesDataDTO;
import com.gemiso.zodiac.app.lbox.contentDTO.DataDTO;
import com.gemiso.zodiac.app.lbox.userInfoDTO.UserInfoDataDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.spring.web.json.Json;

import java.nio.charset.Charset;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class LboxService {

    @Value("${nam.url.key:url}")
    private String namUrl;

    private static final String CONTENTS_URL = "api/ans/v2/contents";
    private static final String  CATEGORIES_URL= "api/ans/v2/categories";
    private static final String  USER_INFO_URL= "api/ans/v2/users";


    //엘박스 영상정보 조회 [anm]
    public JSONObject findAll(String sdate, String edate) throws JsonProcessingException {

        //get parameter 담아주기
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(namUrl+CONTENTS_URL)
                .queryParam("start_date", sdate) //검색 시작일
                .queryParam("end_date", edate); //검색 종료일

        System.out.println(builder.toUriString());

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(30000); // 연결시간 초과

        //Rest template setting
        RestTemplate restTpl = new RestTemplate(httpRequestFactory);
        HttpHeaders headers = new HttpHeaders(); // 담아줄 header
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        HttpEntity entity = new HttpEntity<>(headers); // http entity에 header 담아줌


        ResponseEntity<JSONObject> responseEntity = restTpl.exchange(builder.toUriString(), HttpMethod.GET, entity, JSONObject.class);

        //System.out.println(responseEntity);
        //String ListBody = responseEntity.getBody();

        /*ObjectMapper objectMapper = new ObjectMapper();
        DataDTO dataDTO = new DataDTO();
        dataDTO = objectMapper.readValue(ListBody, DataDTO.class);*/

        //return responseEntity.getBody();
        return responseEntity.getBody();

    }

    //엘박스 카테고리 조회 [anm]
    public JSONObject findCategories(Integer parentId){

        //get parameter 담아주기
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(namUrl+CATEGORIES_URL)
                .queryParam("parent_id", parentId); //parentId

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(30000); // 연결시간 초과

        //Rest template setting
        RestTemplate restTpl = new RestTemplate(httpRequestFactory);
        HttpHeaders headers = new HttpHeaders(); // 담아줄 header
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        HttpEntity entity = new HttpEntity<>(headers); // http entity에 header 담아줌


        ResponseEntity<JSONObject> responseEntity = restTpl.exchange(builder.toUriString(), HttpMethod.GET, entity, JSONObject.class);

        return responseEntity.getBody();
        
    }

    public JSONObject findUserInfo(){

        //get parameter 담아주기
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(namUrl+USER_INFO_URL); //parentId

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(30000); // 연결시간 초과

        //Rest template setting
        RestTemplate restTpl = new RestTemplate(httpRequestFactory);
        HttpHeaders headers = new HttpHeaders(); // 담아줄 header
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        HttpEntity entity = new HttpEntity<>(headers); // http entity에 header 담아줌


        ResponseEntity<JSONObject> responseEntity = restTpl.exchange(builder.toUriString(), HttpMethod.GET, entity, JSONObject.class);

        return responseEntity.getBody();


    }
}


   /* //헤더 설정
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

        return dataDTO;*/