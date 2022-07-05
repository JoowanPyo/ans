package com.gemiso.zodiac.app.yonhapWire;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.app.file.AttachFileRepository;
import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.file.mapper.AttachFileMapper;
import com.gemiso.zodiac.app.user.UserService;
import com.gemiso.zodiac.app.yonhap.Yonhap;
import com.gemiso.zodiac.app.yonhap.YonhapService;
import com.gemiso.zodiac.app.yonhapAssign.YonhapAssign;
import com.gemiso.zodiac.app.yonhapAssign.YonhapAssignRepository;
import com.gemiso.zodiac.app.yonhapAssign.dto.YonhapAssignSimpleDTO;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileCreateDTO;
import com.gemiso.zodiac.app.yonhapPhoto.dto.YonhapExceptionDomain;
import com.gemiso.zodiac.app.yonhapWire.dto.*;
import com.gemiso.zodiac.app.yonhapWire.mapper.YonhapCreateMapper;
import com.gemiso.zodiac.app.yonhapWire.mapper.YonhapWireMapper;
import com.gemiso.zodiac.app.yonhapWireAttchFile.YonhapWireAttchFile;
import com.gemiso.zodiac.app.yonhapWireAttchFile.YonhapWireAttchFileRepository;
import com.gemiso.zodiac.app.yonhapWireAttchFile.dto.YonhapWireAttchFileDTO;
import com.gemiso.zodiac.app.yonhapWireAttchFile.mapper.YonhapWireAttchFileMapper;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import com.gemiso.zodiac.core.helper.PageHelper;
import com.gemiso.zodiac.core.page.PageResultDTO;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class YonhapWireService {

    @Value("${nam.url.injest.key:url}")
    private String namUrl;

    private final YonhapWireRepository yonhapWireRepository;
    private final YonhapAssignRepository yonhapAssignRepository;
    //private final AttachFileRepository attachFileRepository;
    //private final YonhapWireAttchFileRepository yonhapWireAttchFileRepository;

    private final YonhapWireMapper yonhapWireMapper;
    //private final AttachFileMapper attachFileMapper;
    //private final YonhapWireAttchFileMapper yonhapWireAttchFileMapper;


    private final DateChangeHelper dateChangeHelper;
    private final UserService userService;


    public PageResultDTO<YonhapWireDTO, YonhapWire> findAll(Date sdate, Date edate, String agcyCd, String agcyNm,  String source,
                                       String svcTyp,  String searchWord, List<String> imprtList, Integer page, Integer limit,
                                                            String mediaNo){

        //페이지 셋팅 page, limit null일시 page = 1 limit = 50 디폴트 셋팅
        PageHelper pageHelper = new PageHelper(page, limit);
        Pageable pageable = pageHelper.getYonhapPage();

       // BooleanBuilder booleanBuilder = getSearch(sdate, edate, agcyCd, agcyNm, source, svcTyp, searchWord, imprtList);

        Page<YonhapWire> yonhapWireList = yonhapWireRepository.findYonhapWireList(sdate, edate, agcyCd, agcyNm, source,
                svcTyp, searchWord, imprtList, pageable, mediaNo);

        Function<YonhapWire, YonhapWireDTO> fn = (entity -> yonhapWireMapper.toDto(entity));
        //List<YonhapWireDTO> yonhapWireDTOList = yonhapWireMapper.toDtoList(yonhapWireList);

        return new PageResultDTO<YonhapWireDTO, YonhapWire>(yonhapWireList, fn);

    }

    public Long create(YonhapWireCreateDTO yonhapWireCreateDTO) throws Exception {

        Long yhWireId = null;

        //새로 들어온 연합에 contId로 기존에 데이터가 있는지 조회.
        String contId = yonhapWireCreateDTO.getCont_id();
        List<YonhapWire> yonhapWireList = yonhapWireRepository.findYhArtclId(contId);

        //기존연합이 있을경우 updqte
        if (CollectionUtils.isEmpty(yonhapWireList) == false) {

            /*String getCmnt = yonhapWireCreateDTO.getVideo_nm();

            //Nam에 미디어 등록 후 id값을 받아온다.
            YonhapNamResponseDTO mamCont = mamCreateReuterMedia(getCmnt, yonhapReuterCreateDTO);
            NamResponseDTO data = mamCont.getData();
            Long mamContId = null;
            if (ObjectUtils.isEmpty(data) == false){
                mamContId = data.getId();
            }*/

            Long OrgYhArtclId = yonhapWireList.get(0).getWireId();
            yonhapWireCreateDTO.setWire_id(OrgYhArtclId);

            YonhapWire yonhapWire = updateBuildToEntity(yonhapWireCreateDTO);

            yonhapWireRepository.save(yonhapWire);

            yhWireId = yonhapWire.getWireId();

        } else { //기존연합이 없는경우 post

            YonhapWire yonhapWire = postBuildToEntity(yonhapWireCreateDTO);

            yonhapWireRepository.save(yonhapWire);

            yhWireId = yonhapWire.getWireId();
        }

        return yhWireId;
    }

    public YonhapWire updateBuildToEntity(YonhapWireCreateDTO yonhapWireCreateDTO) throws Exception {

        //SimpleDateFormat transFormat = new SimpleDateFormat("yyyymmddhhmmss");

        Date embgDtm = null;
        Date inputDtm = null;
        Date trnsfDtm = null;
        if(!StringUtils.isEmpty(yonhapWireCreateDTO.getEmbg_dtm())) {
            embgDtm = dateChangeHelper.stringToDateNoComma(yonhapWireCreateDTO.getEmbg_dtm());
        }
        if(!StringUtils.isEmpty(yonhapWireCreateDTO.getInput_dtm())) {
            inputDtm = dateChangeHelper.stringToDateNoComma(yonhapWireCreateDTO.getInput_dtm());
        }
        if(!StringUtils.isEmpty(yonhapWireCreateDTO.getTrnsf_dtm())) {
            trnsfDtm = dateChangeHelper.stringToDateNoComma(yonhapWireCreateDTO.getTrnsf_dtm());
        }
        int artclqnty = Integer.parseInt(yonhapWireCreateDTO.getArtclqnty());

        YonhapWire yonhapWire = YonhapWire.builder()
                .wireId(yonhapWireCreateDTO.getWire_id())
                .contId(yonhapWireCreateDTO.getCont_id())
                .imprt(yonhapWireCreateDTO.getImprt())
                .svcTyp(yonhapWireCreateDTO.getSvc_typ())
                .artclTitl(yonhapWireCreateDTO.getArtcl_titl())
                .artclCtt(yonhapWireCreateDTO.getArtcl_ctt())
                .agcyCd(yonhapWireCreateDTO.getAgcy_cd())
                .agcyNm(yonhapWireCreateDTO.getAgcy_nm())
                .credit(yonhapWireCreateDTO.getCredit())
                .artclqnty(artclqnty)
                .source(yonhapWireCreateDTO.getSource())
                .cmnt(yonhapWireCreateDTO.getCmnt())
                .embgDtm(embgDtm)
                .trnsfDtm(trnsfDtm)
                .inputDtm(inputDtm)
                .action(yonhapWireCreateDTO.getAction())
                .mamContId(yonhapWireCreateDTO.getMam_cont_id())
                .mediaNo(yonhapWireCreateDTO.getMedia_no())
                .build();

        return yonhapWire;
    }

    public YonhapWire postBuildToEntity(YonhapWireCreateDTO yonhapWireCreateDTO) throws Exception {

        //SimpleDateFormat transFormat = new SimpleDateFormat("yyyymmddhhmmss");

        Date embgDtm = null;
        Date inputDtm = null;
        Date trnsfDtm = null;
        if(!StringUtils.isEmpty(yonhapWireCreateDTO.getEmbg_dtm())) {
            embgDtm = dateChangeHelper.stringToDateNoComma(yonhapWireCreateDTO.getEmbg_dtm());
        }
        if(!StringUtils.isEmpty(yonhapWireCreateDTO.getInput_dtm())) {
            inputDtm = dateChangeHelper.stringToDateNoComma(yonhapWireCreateDTO.getInput_dtm());
        }
        if(!StringUtils.isEmpty(yonhapWireCreateDTO.getTrnsf_dtm())) {
            trnsfDtm = dateChangeHelper.stringToDateNoComma(yonhapWireCreateDTO.getTrnsf_dtm());
        }
        int artclqnty = Integer.parseInt(yonhapWireCreateDTO.getArtclqnty());

        YonhapWire yonhapWire = YonhapWire.builder()
                .contId(yonhapWireCreateDTO.getCont_id())
                .imprt(yonhapWireCreateDTO.getImprt())
                .svcTyp(yonhapWireCreateDTO.getSvc_typ())
                .artclTitl(yonhapWireCreateDTO.getArtcl_titl())
                .artclCtt(yonhapWireCreateDTO.getArtcl_ctt())
                .agcyCd(yonhapWireCreateDTO.getAgcy_cd())
                .agcyNm(yonhapWireCreateDTO.getAgcy_nm())
                .credit(yonhapWireCreateDTO.getCredit())
                .artclqnty(artclqnty)
                .source(yonhapWireCreateDTO.getSource())
                .cmnt(yonhapWireCreateDTO.getCmnt())
                .embgDtm(embgDtm)
                .trnsfDtm(trnsfDtm)
                .inputDtm(inputDtm)
                .action(yonhapWireCreateDTO.getAction())
                .build();

        return yonhapWire;
    }
        /*Long yhWireId =null;

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyymmddhhmmss");

        Date embgDtm = null;
        Date inputDtm = null;
        Date trnsfDtm = null;
        if(!StringUtils.isEmpty(yonhapWireCreateDTO.getEmbg_dtm())) {
            embgDtm = transFormat.parse(yonhapWireCreateDTO.getEmbg_dtm());
        }
        if(!StringUtils.isEmpty(yonhapWireCreateDTO.getInput_dtm())) {
            inputDtm = transFormat.parse(yonhapWireCreateDTO.getInput_dtm());
        }
        if(!StringUtils.isEmpty(yonhapWireCreateDTO.getTrnsf_dtm())) {
            trnsfDtm = transFormat.parse(yonhapWireCreateDTO.getTrnsf_dtm());
        }
        int artclqnty = Integer.parseInt(yonhapWireCreateDTO.getArtclqnty());

        if(!StringUtils.isEmpty(yonhapWireCreateDTO.getCont_id())) {

            List<YonhapWire> yhWireList = yonhapWireRepository.findYhArtclId(yonhapWireCreateDTO.getCont_id());
            //List<YonhapWireCreateDTO> yhWireList = yonhapWireDAO.getYonhapWireContentIdList(yonhapWireCreateDTO.getContId());

            if (!ObjectUtils.isEmpty(yhWireList)) {
                yhWireId = yhWireList.get(0).getYhArtclId();
            }
            //yonhapWireCreateDTO.setYh_artcl_id(yhWireId);

            //YonhapWire yonhapWire = yonhapWireMapper.toEntity(yonhapWireCreateDTO);

            YonhapWire yonhapWire = YonhapWire.builder()
                    .yhArtclId(yhWireId)
                    .contId(yonhapWireCreateDTO.getCont_id())
                    .imprt(yonhapWireCreateDTO.getImprt())
                    .svcTyp(yonhapWireCreateDTO.getSvc_typ())
                    .artclTitl(yonhapWireCreateDTO.getArtcl_titl())
                    .artclCtt(yonhapWireCreateDTO.getArtcl_ctt())
                    .agcyCd(yonhapWireCreateDTO.getAgcy_cd())
                    .agcyNm(yonhapWireCreateDTO.getAgcy_nm())
                    .credit(yonhapWireCreateDTO.getCredit())
                    .artclqnty(artclqnty)
                    .source(yonhapWireCreateDTO.getSource())
                    .cmnt(yonhapWireCreateDTO.getCmnt())
                    .embgDtm(embgDtm)
                    .trnsfDtm(trnsfDtm)
                    .inputDtm(inputDtm)
                    .action(yonhapWireCreateDTO.getAction())
                    .build();

            yonhapWireRepository.save(yonhapWire);

            yhWireId = yonhapWire.getYhArtclId();

        }else{

            //YonhapWire yonhapWire = yonhapWireMapper.toEntity(yonhapWireCreateDTO);

            YonhapWire yonhapWire = YonhapWire.builder()
                    .contId(yonhapWireCreateDTO.getCont_id())
                    .imprt(yonhapWireCreateDTO.getImprt())
                    .svcTyp(yonhapWireCreateDTO.getSvc_typ())
                    .artclTitl(yonhapWireCreateDTO.getArtcl_titl())
                    .artclCtt(yonhapWireCreateDTO.getArtcl_ctt())
                    .agcyCd(yonhapWireCreateDTO.getAgcy_cd())
                    .agcyNm(yonhapWireCreateDTO.getAgcy_nm())
                    .credit(yonhapWireCreateDTO.getCredit())
                    .artclqnty(artclqnty)
                    .source(yonhapWireCreateDTO.getSource())
                    .cmnt(yonhapWireCreateDTO.getCmnt())
                    .embgDtm(embgDtm)
                    .trnsfDtm(trnsfDtm)
                    .inputDtm(inputDtm)
                    .action(yonhapWireCreateDTO.getAction())
                    .build();

            yonhapWireRepository.save(yonhapWire);

            yhWireId = yonhapWire.getYhArtclId();

        }

        return yhWireId;
     */  // return null;


    public YonhapWireDTO find(Long yhWireId){

        YonhapWire yonhapWire = findYonhapWire(yhWireId);

        //List<YonhapWireAttchFile> yonhapWireAttchFile = yonhapWireAttchFileRepository.findByYonhapWireFile(yhWireId);

        YonhapWireDTO yonhapWireDTO = yonhapWireMapper.toDto(yonhapWire);

       /* if (CollectionUtils.isEmpty(yonhapWireAttchFile) == false){
            List<YonhapWireAttchFileDTO> yonhapWireAttchFileDTOS = yonhapWireAttchFileMapper.toDtoList(yonhapWireAttchFile);
            yonhapWireDTO.setYonhapWireAttchFiles(yonhapWireAttchFileDTOS);
        }*/

        return yonhapWireDTO;

    }

    public YonhapWire findYonhapWire(Long yhWireId){

        Optional<YonhapWire> yonhapWire = yonhapWireRepository.findYhWire(yhWireId);

        if (yonhapWire.isPresent() == false){
            throw new ResourceNotFoundException("연합외신 기사를 찾을 수 없습니다. 외신 아이디: "+ yhWireId);
        }

        return yonhapWire.get();
    }

    public YonhapExceptionDomain createAptn(YonhapAptnCreateDTO yonhapAptnCreateDTO) throws Exception {

        Long aptnId = null;

        String contId = yonhapAptnCreateDTO.getCont_id();

        List<YonhapWire> yonhapWireList = yonhapWireRepository.findYhArtclId(contId);

        if (ObjectUtils.isEmpty(yonhapWireList) == false){

            aptnId = yonhapWireList.get(0).getWireId();

            YonhapWire yonhapWire = YonhapWire.builder()
                    .wireId(aptnId)
                    .contId(contId)
                    .artclTitl(yonhapAptnCreateDTO.getArtcl_titl())
                    .artclCtt(yonhapAptnCreateDTO.getArtcl_ctt())
                    .agcyNm("APTN")
                    .agcyCd("aptn")
                    .build();

            try {
                yonhapWireRepository.save(yonhapWire); //외신 aptn 등록
            }catch (RuntimeException e){
                return new YonhapExceptionDomain(aptnId, "5001", "yonhapAptn", e.getMessage(), "");
            }

            /*List<YonhapAttachFileCreateDTO> attachFileList = yonhapAptnCreateDTO.getUpload_files();

            if (CollectionUtils.isEmpty(attachFileList) == false){
                try {
                    yonhapService.uploadYonhapFiles(aptnId, attachFileList, "08");
                }catch (RuntimeException e){
                    return new YonhapExceptionDomain(aptnId, "5002", "yonhapAptn", e.getMessage(), "");
                }
            }*/
            
        }else {

            YonhapWire yonhapWire = YonhapWire.builder()
                    .contId(contId)
                    .artclTitl(yonhapAptnCreateDTO.getArtcl_titl())
                    .artclCtt(yonhapAptnCreateDTO.getArtcl_ctt())
                    .agcyNm("APTN")//008
                    .agcyCd("aptn")
                    .build();

            try {
                yonhapWireRepository.save(yonhapWire); //외신 aptn 등록
                aptnId = yonhapWire.getWireId();
            }catch (RuntimeException e){
                return new YonhapExceptionDomain(aptnId, "5001", "yonhapAptn", e.getMessage(), "");
            }

            /*List<YonhapAttachFileCreateDTO> attachFileList = yonhapAptnCreateDTO.getUpload_files();

            //파일등록
            if (CollectionUtils.isEmpty(attachFileList) == false){
                try {
                    yonhapService.uploadYonhapFiles(aptnId, attachFileList, "08");
                }catch (RuntimeException e){
                    return new YonhapExceptionDomain(aptnId, "5002", "yonhapAptn", e.getMessage(), "");
                }
            }*/
            
        }

        return new YonhapExceptionDomain(aptnId, "2000", "yonhapAptn", "", "");

    }

    public YonhapExceptionDomain createReuter(YonhapReuterCreateDTO yonhapReuterCreateDTO) throws JsonProcessingException, ParseException {

        Long reuterId = null;

        String contId = yonhapReuterCreateDTO.getWire_artcl_id(); //transmitId [ XML ]


        //Nam에 보낼 미디어 파일 네임
        String getCmnt = yonhapReuterCreateDTO.getVideo_nm();
        //Nam에 미디어 등록 후 id값을 받아온다.
        YonhapNamResponseDTO mamCont = mamCreateReuterMedia(getCmnt, yonhapReuterCreateDTO);

        NamResponseDTO data = mamCont.getData();
        String mamContId = "";
        Long getMamContId = null;
        if (ObjectUtils.isEmpty(data) == false){
            getMamContId = data.getId();

            mamContId = String.valueOf(getMamContId);
        }

        //에딧 넘버로 조회 하기 위해 get
        String mediaNo = yonhapReuterCreateDTO.getEditor_number();

        //에딧넘버 + namContId로 기존에 등록된 REUTER정보가 있는지 확인 있으면 UPDATE 없으면 CREATE
        List<YonhapWire> yonhapWireList = yonhapWireRepository.findReuter(mediaNo, mamContId);

        if (CollectionUtils.isEmpty(yonhapWireList) == false){

            Date trnsfDtm = dateChangeHelper.stringToDateNoComma(yonhapReuterCreateDTO.getArtcl_stnd_dtm());

            reuterId = yonhapWireList.get(0).getWireId();
            String credit = yonhapWireList.get(0).getCredit();


           /* String getCmnt = yonhapReuterCreateDTO.getVideo_nm();


            //Nam에 미디어 등록 후 id값을 받아온다.
            YonhapNamResponseDTO mamCont = mamCreateReuterMedia(getCmnt, yonhapReuterCreateDTO);*/
           /* NamResponseDTO data = mamCont.getData();
            Long mamContId = null;
            if (ObjectUtils.isEmpty(data) == false){
                mamContId = data.getId();
            }*/


            String cmnt = null;
            if (getCmnt != null && getCmnt.trim().isEmpty() == false) {
                cmnt = getCmnt.substring(0, getCmnt.lastIndexOf("."));
            }

            YonhapWire yonhapWire = YonhapWire.builder()
                    .wireId(reuterId)
                    .contId(String.valueOf(mamContId))//이게 미디어 넘버로
                    .artclTitl(yonhapReuterCreateDTO.getWire_artcl_titl())
                    .artclCtt(yonhapReuterCreateDTO.getWire_artcl_ctt())
                    .agcyNm("REUTERS")//009
                    .cmnt(cmnt)//이게 콘텐츠 아이디로?
                    .imprt(yonhapReuterCreateDTO.getImprt())
                    .mediaNo(mediaNo)
                    .source("REUTERS")
                    .svcTyp("R")
                    .agcyCd("R")
                    .inputDtm(new Date())
                    .trnsfDtm(trnsfDtm)
                    .mamContId(getMamContId)//실제 nam에서 받은 Long값 nam contentId
                    .credit(credit+","+contId)//여기에 콘텐츠 정보를 넣는다.
                    .build();


            try {
                yonhapWireRepository.save(yonhapWire); //외신 aptn 등록
            }catch (RuntimeException e){
                return new YonhapExceptionDomain(reuterId, "5001", "yonhapAptn", e.getMessage(), "");
            }

        }else {

            Date trnsfDtm = dateChangeHelper.stringToDateNoComma(yonhapReuterCreateDTO.getArtcl_stnd_dtm());

            String cmnt = null;
            if (getCmnt != null && getCmnt.trim().isEmpty() == false) {
                cmnt = getCmnt.substring(0, getCmnt.lastIndexOf("."));
            }

           /* String getCmnt = yonhapReuterCreateDTO.getVideo_nm();
            int lenth = getCmnt.length();
            //Nam에 미디어 등록 후 id값을 받아온다.
            YonhapNamResponseDTO mamCont = mamCreateReuterMedia(getCmnt, yonhapReuterCreateDTO);
            NamResponseDTO data = mamCont.getData();
            Long mamContId = null;
            if (ObjectUtils.isEmpty(data) == false){
                mamContId = data.getId();
            }*/


            YonhapWire yonhapWire = YonhapWire.builder()
                    .contId(String.valueOf(mamContId))
                    .artclTitl(yonhapReuterCreateDTO.getWire_artcl_titl())
                    .artclCtt(yonhapReuterCreateDTO.getWire_artcl_ctt())
                    .agcyNm("REUTERS")//009
                    .cmnt(cmnt)
                    .imprt(yonhapReuterCreateDTO.getImprt())
                    .mediaNo(mediaNo)
                    .source("REUTERS")
                    .svcTyp("R")
                    .agcyCd("R")
                    .inputDtm(new Date())
                    .trnsfDtm(trnsfDtm)
                    .mamContId(getMamContId)//실제 nam에서 받은 Long값 nam contentId
                    .credit(contId)//여기에 콘텐츠 정보를 넣는다.
                    .build();

            try {
                yonhapWireRepository.save(yonhapWire); //외신 aptn 등록
                reuterId = yonhapWire.getWireId();
            }catch (RuntimeException e){
                return new YonhapExceptionDomain(reuterId, "5001", "yonhapAptn", e.getMessage(), "");
            }

        }
        return new YonhapExceptionDomain(reuterId, "2000", "yonhapPhoto", "", "");
    }

    public YonhapNamResponseDTO mamCreateReuterMedia(String mediaNm, YonhapReuterCreateDTO yonhapReuterCreateDTO) throws JsonProcessingException {

        //헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        httpHeaders.add("Session_user_id", "ans");

        //Object Mapper를 통한 Json바인딩할 dmParam생성
        Map<String, Object> param = new HashMap<>();
        param.put("title", yonhapReuterCreateDTO.getWire_artcl_titl());
        param.put("content", yonhapReuterCreateDTO.getWire_artcl_ctt());
        param.put("edit_no", yonhapReuterCreateDTO.getEditor_number());
        param.put("user_id", "");
        param.put("filename", mediaNm);
        param.put("org_filename", mediaNm);

        ObjectMapper objectMapper = new ObjectMapper();
        String params = objectMapper.writeValueAsString(param);

        //httpEntity에 헤더 및 params 설정
        HttpEntity entity = new HttpEntity(params, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<YonhapNamResponseDTO> responseEntity =
                restTemplate.exchange(namUrl, HttpMethod.POST,
                        entity, YonhapNamResponseDTO.class);

        YonhapNamResponseDTO results = responseEntity.getBody();


        return results;

    }

    /*public YonhapAptnDTO formatAptn(YonhapWireDTO yonhapWireDTO){

        List<YonhapWireAttchFileDTO> yonhapWireAttchFiles = yonhapWireDTO.getYonhapWireAttchFiles();

        List<AttachFileDTO> attachFileDTOS = new ArrayList<>();

        if (CollectionUtils.isEmpty(yonhapWireAttchFiles) == false) {
            Long[] fileId = new Long[yonhapWireAttchFiles.size()];

            int i = 0;
            for (YonhapWireAttchFileDTO wireAttchFile : yonhapWireAttchFiles) {

                AttachFileDTO attachFileDTO = wireAttchFile.getAttachFile();
                fileId[i] = attachFileDTO.getFileId();
                i++;
            }

            List<AttachFile> attachFiles = attachFileRepository.findFileInfo(fileId);
            attachFileDTOS = attachFileMapper.toDtoList(attachFiles);
        }

        YonhapAptnDTO yonhapAptnDTO = YonhapAptnDTO.builder()
                .yh_artcl_id(yonhapWireDTO.getWireId())
                .cont_id(yonhapWireDTO.getContId())
                .artcl_titl(yonhapWireDTO.getArtclTitl())
                .artcl_ctt(yonhapWireDTO.getArtclCtt())
                .files(attachFileDTOS)
                .build();

        return yonhapAptnDTO;
    }*/

    public YonhapReuterDTO formatReuter(YonhapWireDTO yonhapWireDTO){

        YonhapReuterDTO yonhapReuterDTO = YonhapReuterDTO.builder()
                //wire_artcl_id(yonhapWireDTO.getWireId())
                .wire_artcl_id(yonhapWireDTO.getContId())
                .wire_artcl_titl(yonhapWireDTO.getArtclTitl())
                .wire_artcl_ctt(yonhapWireDTO.getArtclCtt())
                //.files(attachFileDTOS)
                .build();

        return yonhapReuterDTO;

    }

    public YonhapAssignSimpleDTO createWireAssign(YonhapWireAssignCreateDTO yonhapWireAssignCreateDTO, String userId){

        YonhapWireSimpleDTO yonhapWireSimpleDTO = yonhapWireAssignCreateDTO.getYonhapWire();
        Long yonhapWireId = yonhapWireSimpleDTO.getWireId();
        YonhapWire yonhapWire = findYonhapWire(yonhapWireId);

        //지정자 아이디
        String designatorId = yonhapWireAssignCreateDTO.getDesignatorId();

        userService.userFindOrFail(designatorId);//사용자 아이디 확인

        YonhapAssign yonhapAssign = YonhapAssign.builder()
                .yonhapWire(yonhapWire)
                .designatorId(designatorId)
                .assignerId(userId)
                .build();

        yonhapAssignRepository.save(yonhapAssign);

        Long assignId = yonhapAssign.getAssignId();

        YonhapAssignSimpleDTO yonhapAssignSimpleDTO = new YonhapAssignSimpleDTO();
        yonhapAssignSimpleDTO.setAssignId(assignId);

        return yonhapAssignSimpleDTO;
    }

    private BooleanBuilder getSearch(Date sdate, Date edate, String agcyCd,
                                     String agcyNm,  String source,  String svcTyp,  String searchWord, List<String> imprtList){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QYonhapWire qYonhapWire = QYonhapWire.yonhapWire;

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false){
            booleanBuilder.and(qYonhapWire.inputDtm.between(sdate, edate));
        }
        if (agcyCd != null && agcyCd.trim().isEmpty() == false){
            booleanBuilder.and(qYonhapWire.agcyCd.eq(agcyCd));
        }
        if (searchWord != null && searchWord.trim().isEmpty() == false){
            booleanBuilder.and(qYonhapWire.artclTitl.contains(searchWord));
        }
        if (ObjectUtils.isEmpty(imprtList) == false){

            booleanBuilder.and(qYonhapWire.imprt.in(imprtList));

        }
        return booleanBuilder;
    }

    public YonhapWireResponseDTO formatWire(YonhapWireDTO yonhapWireDTO){

        YonhapWireResponseDTO yonhapWireResponseDTO = YonhapWireResponseDTO.builder()
                .yh_artcl_id(yonhapWireDTO.getWireId())
                .cont_id(yonhapWireDTO.getContId())
                .action(yonhapWireDTO.getAction())
                .imprt(yonhapWireDTO.getImprt())
                .svc_typ(yonhapWireDTO.getSvcTyp())
                .artcl_titl(yonhapWireDTO.getArtclTitl())
                .artcl_ctt(yonhapWireDTO.getArtclCtt())
                .artclqnty(yonhapWireDTO.getArtclqnty())
                .agcy_cd(yonhapWireDTO.getAgcyCd())
                .agcy_nm(yonhapWireDTO.getAgcyNm())
                .source(yonhapWireDTO.getSource())
                .credit(yonhapWireDTO.getCredit())
                .input_dtm(dateChangeHelper.dateToStringNormal(yonhapWireDTO.getInputDtm()))
                .trnsf_dtm(dateChangeHelper.dateToStringNormal(yonhapWireDTO.getTrnsfDtm()))
                .build();

        return yonhapWireResponseDTO;
    }

    /*public String dateToString(Date date){

        String returnDate = "";

        if (ObjectUtils.isEmpty(date) == false) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            returnDate = simpleDateFormat.format(date);
        }

        return returnDate;
    }*/
}
