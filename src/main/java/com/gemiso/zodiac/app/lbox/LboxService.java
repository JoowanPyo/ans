package com.gemiso.zodiac.app.lbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemiso.zodiac.app.articleMedia.ArticleMedia;
import com.gemiso.zodiac.app.articleMedia.ArticleMediaRepository;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaDTO;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaMapper;
import com.gemiso.zodiac.app.cueSheetMedia.CueSheetMedia;
import com.gemiso.zodiac.app.cueSheetMedia.CueSheetMediaRepository;
import com.gemiso.zodiac.app.cueSheetMedia.CueSheetMediaService;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaDTO;
import com.gemiso.zodiac.app.cueSheetMedia.mapper.CueSheetMediaMapper;
import com.gemiso.zodiac.app.lbox.mediaTransportDTO.*;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class LboxService {

    @Value("${nam.url.key:url}")
    private String namUrl;

    private final ArticleMediaRepository articleMediaRepository;
    private final CueSheetMediaRepository cueSheetMediaRepository;

    private final ArticleMediaMapper articleMediaMapper;
    private final CueSheetMediaMapper cueSheetMediaMapper;

    private final UserAuthService userAuthService;

    private final CueSheetMediaService cueSheetMediaService;

    private static final String CONTENTS_URL = "api/ans/v2/contents";
    private static final String CATEGORIES_URL = "api/ans/v2/categories";
    private static final String USER_INFO_URL = "api/ans/v2/users";


    //엘박스 영상정보 조회 [anm]
    public JSONObject findAll(String sdate, String edate, String userId, String keyword, String videoId,
                              Integer categoryId, Integer isForever, Integer page, Integer limit, String sort) throws JsonProcessingException {


        //get parameter 담아주기
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(namUrl + CONTENTS_URL)
                .queryParam("start_date", sdate) //검색 시작일
                .queryParam("end_date", edate) //검색 종료일
                .queryParam("user_id", userId)
                .queryParam("keyword", keyword)
                .queryParam("video_id", videoId)
                .queryParam("category_id", categoryId)
                .queryParam("is_forever", isForever)
                .queryParam("page", page)
                .queryParam("limit", limit)
                .queryParam("sort", sort);


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
    public JSONObject findCategories(Integer parentId) {

        //get parameter 담아주기
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(namUrl + CATEGORIES_URL)
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

    public JSONObject findUserInfo() {

        //get parameter 담아주기
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(namUrl + USER_INFO_URL); //parentId

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

    //부조 전송
    public TransportResponseDTO mediaTransfer(Long mediaId, Integer contentId, String subrmNm, String destination, Boolean isUrgent, Boolean isRetry) throws JsonProcessingException {

        TransportResponseDTO transportResponseDTO = new TransportResponseDTO(); //리턴해줄 미디어정보, 오류내용 [전송 오류가 있을시.]
        List<TransportFaildDTO> transportFaildDTOList = new ArrayList<>(); //부조전송시 오류가 있을시 오류내용 Respons

        String userId = userAuthService.authUser.getUserId();

        //헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        int tasksCount = 0;
        int clipInfoCount = 0;
        ClipInfoDTO clipInfoDTO = new ClipInfoDTO();
        List<String> destinations = new ArrayList<>();

        switch (destination) {
            case "T":
                //test 용으로 클라우트 콘피그 구현시 교체
                if ("A".equals(subrmNm)) {
                    destinations.add("NS");
                    destinations.add("PS_A");
                    destinations.add("PS_B");

                } else if ("B".equals(subrmNm)) {
                    destinations.add("NS");
                    destinations.add("PS_A");
                    destinations.add("PS_B");
                    destinations.add("PS_C");
                }
                break;
            case "B":
                if ("A".equals(subrmNm)) {
                    //destinations.add("NS");
                    destinations.add("PS_A");
                    destinations.add("PS_B");

                } else if ("B".equals(subrmNm)) {
                    //destinations.add("NS");
                    destinations.add("PS_A");
                    destinations.add("PS_B");
                    destinations.add("PS_C");
                }
                break;
        }

        String faildDest = ""; //실패된 전송대상명

        if (CollectionUtils.isEmpty(destinations)) {
            throw new ResourceNotFoundException(subrmNm + "부조에 대한 전송대상이 없습니다. ");
        }

        for (String dest : destinations) {

            faildDest = dest;

            //Object Mapper를 통한 Json바인딩할 dmParam생성
            Map<String, Object> params = new HashMap<>();
            params.put("content_id", contentId); //콘텐츠 아이디
            params.put("scr", subrmNm); //부조값 ( A, B )
            params.put("destination", dest);
            params.put("user_account_id", userId); //사용자 아이디
            params.put("is_urgent", isUrgent); //긴급여부
            params.put("is_retry", isRetry); //재전송 여부

            ObjectMapper objectMapper = new ObjectMapper();
            String param = objectMapper.writeValueAsString(params);

            //httpEntity에 헤더 및 params 설정
            HttpEntity entity = new HttpEntity(param, httpHeaders);

            ResponsMediaTransportDTO responsBody = new ResponsMediaTransportDTO();

            try {

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<ResponsMediaTransportDTO> responseEntity =
                        restTemplate.exchange(namUrl + "api/ans/v2/transfer-scr", HttpMethod.POST,
                                entity, ResponsMediaTransportDTO.class);

                responsBody = responseEntity.getBody();

                MediaTransportDataDTO data = responsBody.getData();
                clipInfoDTO = data.getClip_info(); //이미 전송된 영상이거나 전송완료인 영상 데이터DTO
                List<TasksDTO> tasksDTO = data.getTasks(); //전송시작시 데이터

                if (CollectionUtils.isEmpty(tasksDTO) == false && "NS".equals(dest)) {
                    ++tasksCount; //전송중이 한개라도 있으면 전송중으로 값 셋팅하기 위해 체크
                }
                if (ObjectUtils.isEmpty(clipInfoDTO) == false && "NS".equals(dest)) {
                    ++clipInfoCount;//이미전송된 파일이  한개라도 있으면 전송중으로 값 셋팅하기 위해 체크
                }

            } catch (Exception e) { //부조 전송오류가 있을시,

                TransportFaildDTO transportFaildDTO = new TransportFaildDTO();
                transportFaildDTO.setMessage(e.getMessage()); //오류 메시지
                transportFaildDTO.setSubrmNm(subrmNm); //부조 명
                transportFaildDTO.setDestination(faildDest); // 전송대상

                transportFaildDTOList.add(transportFaildDTO);

            }
        }

        ArticleMedia articleMedia = findArticleMedia(mediaId);
        ArticleMediaDTO articleMediaDTO = articleMediaMapper.toDto(articleMedia);

        //전송중 값이 1개라도 체크가 되어있는 경우
        if (tasksCount > 0) {

            //기사 미디어 부조 전송 시작.

            articleMediaDTO.setTrnsfStCd("match_ready"); //전송시작 코드
            articleMediaDTO.setTrnasfVal(0);
            articleMediaMapper.updateFromDto(articleMediaDTO, articleMedia); //기존 기사미디어 정보에 업데이트 정보 업데이트
            articleMediaRepository.save(articleMedia); //수정

            transportResponseDTO.setArticleMediaDTO(articleMediaDTO);// 셋팅된 미디어정보

        } else if (clipInfoCount > 0 && tasksCount == 0) { //전송중 값이 0개이고 전송완료값이 0보다 크면 전송완료값으로 셋팅

            //기사 미디어 부조 전송 완료(이미 전송된 영상)
            articleMediaDTO.setTrnsfFileNm(clipInfoDTO.getFilename()); //전송완료된 파일네임(0001 ~ 9999 + .mxf)
            articleMediaDTO.setTrnsfStCd("match_completed"); //전송완료 코드
            articleMediaDTO.setTrnasfVal(100);
            articleMediaMapper.updateFromDto(articleMediaDTO, articleMedia); //기존 기사미디어 정보에 업데이트 정보 업데이트
            articleMediaRepository.save(articleMedia); //수정

            transportResponseDTO.setArticleMediaDTO(articleMediaDTO); // 셋팅된 미디어정보
        }

        transportResponseDTO.setTransportFaild(transportFaildDTOList);

        log.info("Destination : " + destination + "변경된 미디어 정보 : " + articleMedia + " 에러정보 : " + transportFaildDTOList);

        return transportResponseDTO;

    }

    //부조 전송
    public TransportResponseDTO emergencyTransfer(Integer contentId, String subrmNm, String destination, Boolean isUrgent, Boolean isRetry) throws JsonProcessingException {

        TransportResponseDTO transportResponseDTO = new TransportResponseDTO(); //리턴해줄 미디어정보, 오류내용 [전송 오류가 있을시.]
        List<TransportFaildDTO> transportFaildDTOList = new ArrayList<>(); //부조전송시 오류가 있을시 오류내용 Respons

        String userId = userAuthService.authUser.getUserId();

        //헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        int tasksCount = 0;
        int clipInfoCount = 0;
        ClipInfoDTO clipInfoDTO = new ClipInfoDTO();
        List<String> destinations = new ArrayList<>();


        switch (destination) {
            case "N": //NS긴급전송일 경우
                if ("A".equals(subrmNm)) {
                    destinations.add("NS");

                } else if ("B".equals(subrmNm)) {
                    destinations.add("NS");
                }
                break;
            case "P": //PS긴급전송일 경우
                if ("A".equals(subrmNm)) {
                    destinations.add("PS_A");
                    destinations.add("PS_B");

                } else if ("B".equals(subrmNm)) {
                    destinations.add("PS_A");
                    destinations.add("PS_B");
                    destinations.add("PS_C");
                }
                break;
        }


        String faildDest = ""; //실패된 전송대상명

        if (CollectionUtils.isEmpty(destinations)) {
            throw new ResourceNotFoundException(subrmNm + "부조에 대한 전송대상이 없습니다. ");
        }

        for (String dest : destinations) {

            faildDest = dest;

            //Object Mapper를 통한 Json바인딩할 dmParam생성
            Map<String, Object> params = new HashMap<>();
            params.put("content_id", contentId); //콘텐츠 아이디
            params.put("scr", subrmNm); //부조값 ( A, B )
            params.put("destination", dest);
            params.put("user_account_id", userId); //사용자 아이디
            params.put("is_urgent", isUrgent); //긴급여부
            params.put("is_retry", isRetry); //재전송 여부

            ObjectMapper objectMapper = new ObjectMapper();
            String param = objectMapper.writeValueAsString(params);

            //httpEntity에 헤더 및 params 설정
            HttpEntity entity = new HttpEntity(param, httpHeaders);

            ResponsMediaTransportDTO responsBody = new ResponsMediaTransportDTO();

            try {

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<ResponsMediaTransportDTO> responseEntity =
                        restTemplate.exchange(namUrl + "api/ans/v2/transfer-scr", HttpMethod.POST,
                                entity, ResponsMediaTransportDTO.class);

                responsBody = responseEntity.getBody();

                MediaTransportDataDTO data = responsBody.getData();

                clipInfoDTO = data.getClip_info(); //이미 전송된 영상이거나 전송완료인 영상 데이터DTO
                List<TasksDTO> tasksDTO = data.getTasks(); //전송시작시 데이터

                if (CollectionUtils.isEmpty(tasksDTO) == false && "NS".equals(dest)) {
                    ++tasksCount; //전송중이 한개라도 있으면 전송중으로 값 셋팅하기 위해 체크
                }
                if (ObjectUtils.isEmpty(clipInfoDTO) == false && "NS".equals(dest)) {
                    ++clipInfoCount;//이미전송된 파일이  한개라도 있으면 전송중으로 값 셋팅하기 위해 체크
                }

            } catch (Exception e) { //부조 전송오류가 있을시,

                TransportFaildDTO transportFaildDTO = new TransportFaildDTO();
                transportFaildDTO.setMessage(e.getMessage()); //오류 메시지
                transportFaildDTO.setSubrmNm(subrmNm); //부조 명
                transportFaildDTO.setDestination(faildDest); // 전송대상

                transportFaildDTOList.add(transportFaildDTO);

            }
        }

        //전송중 완료값이 1개라도 있는경우
        if (clipInfoCount > 0) {

            transportResponseDTO.setClipInfo(true);


        } else{ //전송완료값이 없는 경우

            transportResponseDTO.setClipInfo(false);
        }

        transportResponseDTO.setTransportFaild(transportFaildDTOList);

        log.info("Ps 긴급전송 : " + "Destination : " + destination + " 에러정보 : " + transportFaildDTOList);

        return transportResponseDTO;

    }

    //기사 미디어 정보 조회 및 존재유무 확인
    public ArticleMedia findArticleMedia(Long mediaId) {

        Optional<ArticleMedia> articleMediaEntity = articleMediaRepository.findByArticleMedia(mediaId);

        if (articleMediaEntity.isPresent() == false) {
            throw new ResourceNotFoundException("기사 미디어를 찾을 수 없습니다. MediaId : " + mediaId);
        }

        return articleMediaEntity.get();
    }

    //큐시트 미디어 부조 전송
    public CueMediaTransportResponseDTO cueMediaTransfer(Long cueMediaId, Integer contentId, String subrmNm
            , Boolean isUrgent, Boolean isRetry, String isType) throws JsonProcessingException {

        CueMediaTransportResponseDTO transportResponseDTO = new CueMediaTransportResponseDTO(); //리턴해줄 미디어정보, 오류내용 [전송 오류가 있을시.]
        List<TransportFaildDTO> transportFaildDTOList = new ArrayList<>(); //부조전송시 오류가 있을시 오류내용 Respons

        String userId = userAuthService.authUser.getUserId();

        //헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        int tasksCount = 0;
        int clipInfoCount = 0;
        ClipInfoDTO clipInfoDTO = new ClipInfoDTO();
        List<String> destinations = new ArrayList<>();

        switch (isType) {
            case "T":
                //test 용으로 클라우트 콘피그 구현시 교체
                if ("A".equals(subrmNm)) {
                    destinations.add("NS");
                    destinations.add("PS_A");
                    destinations.add("PS_B");

                } else if ("B".equals(subrmNm)) {
                    destinations.add("NS");
                    destinations.add("PS_A");
                    destinations.add("PS_B");
                    destinations.add("PS_C");
                }
                break;
            case "B":
                if ("A".equals(subrmNm)) {
                    //destinations.add("NS");
                    destinations.add("PS_A");
                    destinations.add("PS_B");

                } else if ("B".equals(subrmNm)) {
                    //destinations.add("NS");
                    destinations.add("PS_A");
                    destinations.add("PS_B");
                    destinations.add("PS_C");
                }
                break;
        }

        String faildDest = ""; //실패된 전송대상명

        if (CollectionUtils.isEmpty(destinations)) {
            throw new ResourceNotFoundException(subrmNm + "부조에 대한 전송대상이 없습니다. ");
        }

        for (String dest : destinations) {

            faildDest = dest;

            //Object Mapper를 통한 Json바인딩할 dmParam생성
            Map<String, Object> params = new HashMap<>();
            params.put("content_id", contentId); //콘텐츠 아이디
            params.put("scr", subrmNm); //부조값 ( A, B )
            params.put("destination", dest);
            params.put("user_account_id", userId); //사용자 아이디
            params.put("is_urgent", isUrgent); //긴급여부
            params.put("is_retry", isRetry); //재전송 여부

            ObjectMapper objectMapper = new ObjectMapper();
            String param = objectMapper.writeValueAsString(params);

            //httpEntity에 헤더 및 params 설정
            HttpEntity entity = new HttpEntity(param, httpHeaders);

            ResponsMediaTransportDTO responsBody = new ResponsMediaTransportDTO();

            try {

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<ResponsMediaTransportDTO> responseEntity =
                        restTemplate.exchange(namUrl + "api/ans/v2/transfer-scr", HttpMethod.POST,
                                entity, ResponsMediaTransportDTO.class);

                responsBody = responseEntity.getBody();

                MediaTransportDataDTO data = responsBody.getData();
                clipInfoDTO = data.getClip_info(); //이미 전송된 영상이거나 전송완료인 영상 데이터DTO
                List<TasksDTO> tasksDTO = data.getTasks(); //전송시작시 데이터

                if (CollectionUtils.isEmpty(tasksDTO) == false && "NS".equals(dest)) {
                    ++tasksCount; //전송중이 한개라도 있으면 전송중으로 값 셋팅하기 위해 체크
                }
                if (ObjectUtils.isEmpty(clipInfoDTO) == false && "NS".equals(dest)) {
                    ++clipInfoCount;//이미전송된 파일이  한개라도 있으면 전송중으로 값 셋팅하기 위해 체크
                }

            } catch (Exception e) { //부조 전송오류가 있을시,

                TransportFaildDTO transportFaildDTO = new TransportFaildDTO();
                transportFaildDTO.setMessage(e.getLocalizedMessage()); //오류 메시지
                transportFaildDTO.setSubrmNm(subrmNm); //부조 명
                transportFaildDTO.setDestination(faildDest); // 전송대상

                transportFaildDTOList.add(transportFaildDTO);

            }
        }

        CueSheetMedia cueSheetMedia = cueSheetMediaService.cueSheetMediaFindOrFail(cueMediaId);
        CueSheetMediaDTO cueSheetMediaDTO = cueSheetMediaMapper.toDto(cueSheetMedia);

        //전송중 값이 1개라도 체크가 되어있는 경우
        if (tasksCount > 0) {

            //기사 미디어 부조 전송 시작.

            cueSheetMediaDTO.setTrnsfStCd("match_ready"); //전송시작 코드
            cueSheetMediaMapper.updateFromDto(cueSheetMediaDTO, cueSheetMedia); //기존 기사미디어 정보에 업데이트 정보 업데이트
            cueSheetMediaRepository.save(cueSheetMedia); //수정

            transportResponseDTO.setCueSheetMediaDTO(cueSheetMediaDTO);// 셋팅된 미디어정보

        } else if (clipInfoCount > 0 && tasksCount == 0) { //전송중 값이 0개이고 전송완료값이 0보다 크면 전송완료값으로 셋팅

            //기사 미디어 부조 전송 완료(이미 전송된 영상)
            cueSheetMediaDTO.setTrnsfFileNm(clipInfoDTO.getFilename()); //전송완료된 파일네임(0001 ~ 9999 + .mxf)
            cueSheetMediaDTO.setTrnsfStCd("match_completed"); //전송완료 코드
            cueSheetMediaMapper.updateFromDto(cueSheetMediaDTO, cueSheetMedia); //기존 기사미디어 정보에 업데이트 정보 업데이트
            cueSheetMediaRepository.save(cueSheetMedia); //수정

            transportResponseDTO.setCueSheetMediaDTO(cueSheetMediaDTO); // 셋팅된 미디어정보
        }

        transportResponseDTO.setTransportFaild(transportFaildDTOList);

        log.info("SubrmNm : " + subrmNm + "변경된 미디어 정보 : " + cueSheetMedia + " 에러정보 : " + transportFaildDTOList);

        return transportResponseDTO;

    }


  /*  //전송상태 업데이트
    public void stateChange(Integer contentId, String videoId,String trnsfFileNm, String mediaTypCd){

        //콘텐츠아이디 + 비디오아이디 로 기사 미디어 검색.
        List<ArticleMedia> articleMediaList = articleMediaRepository.findArticleMediaListByContentId(contentId, videoId);

        //검색된 기사 미디어 값 업데이트
        for (ArticleMedia articleMedia : articleMediaList){

            ArticleMediaDTO articleMediaDTO = articleMediaMapper.toDto(articleMedia);
            articleMediaDTO.setMediaTypCd(mediaTypCd); //변경코드 업데이트
            articleMediaDTO.setTrnsfFileNm(trnsfFileNm); //전송파일명 업데이트

            articleMediaMapper.updateFromDto(articleMediaDTO, articleMedia);

            articleMediaRepository.save(articleMedia);

        }


    }*/
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