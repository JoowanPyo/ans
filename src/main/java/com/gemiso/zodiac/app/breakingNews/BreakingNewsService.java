package com.gemiso.zodiac.app.breakingNews;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gemiso.zodiac.app.breakingNews.dto.*;
import com.gemiso.zodiac.app.breakingNews.mapper.BreakingNewsCreateMapper;
import com.gemiso.zodiac.app.breakingNews.mapper.BreakingNewsMapper;
import com.gemiso.zodiac.app.breakingNews.mapper.BreakingNewsUpdateMapper;
import com.gemiso.zodiac.app.breakingNewsDetail.BreakingNewsDtl;
import com.gemiso.zodiac.app.breakingNewsDetail.BreakingNewsDtlRepository;
import com.gemiso.zodiac.app.breakingNewsDetail.dto.BreakingNewsDtlCreateDTO;
import com.gemiso.zodiac.app.breakingNewsDetail.dto.BreakingNewsDtlDTO;
import com.gemiso.zodiac.app.breakingNewsDetail.mapper.BreakingNewsDtlCreateMapper;
import com.gemiso.zodiac.app.breakingNewsFtpInfo.BreakingNewsFtpInfo;
import com.gemiso.zodiac.app.breakingNewsFtpInfo.BreakingNewsFtpInfoService;
import com.gemiso.zodiac.app.breakingNewsFtpInfo.dto.BreakingNewsFtpInfoDTO;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetCapDownloadXMLDTO;
import com.gemiso.zodiac.core.helper.CapSeparatorHelper;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import com.gemiso.zodiac.core.helper.JAXBXmlHelper;
import com.gemiso.zodiac.core.service.FTPconnectService;
import com.gemiso.zodiac.core.util.PropertyUtil;
import com.gemiso.zodiac.core.util.UploadFileBean;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BreakingNewsService {

    private final BreakingNewsRepository breakingNewsRepository;
    private final BreakingNewsDtlRepository breakingNewsDtlRepository;

    private final BreakingNewsMapper breakingNewsMapper;
    private final BreakingNewsCreateMapper breakingNewsCreateMapper;
    private final BreakingNewsUpdateMapper breakingNewsUpdateMapper;
    private final BreakingNewsDtlCreateMapper breakingNewsDtlCreateMapper;

    private final CapSeparatorHelper capSeparatorHelper;
    private final DateChangeHelper dateChangeHelper;

    private final BreakingNewsFtpInfoService breakingNewsFtpInfoService;

    //private final UserAuthService userAuthService;


    //속보뉴스 목록조회
    public List<BreakingNewsDTO> findAll(Date sdate, Date edate, String delYn){

        //속보뉴스 목록조회 조회조건 빌드
        BooleanBuilder booleanBuilder = getSearch(sdate, edate, delYn);

        //생성된 조회조건으로 리스트 조회
        List<BreakingNews> breakingNewsList =
                (List<BreakingNews>) breakingNewsRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "inputDtm"));

        //DTO리스트 변환
        List<BreakingNewsDTO> breakingNewsDTOList = breakingNewsMapper.toDtoList(breakingNewsList);

        //DTO리스트 리턴
        return breakingNewsDTOList;
    }

    //속보뉴스 상세조회
    public BreakingNewsDTO find(Long breakingNewsId){

        BreakingNews breakingNews = findBreakingNews(breakingNewsId);//속보뉴스 조회 및 존재유무 확인 [조회조건 속보뉴스 아이디]

        BreakingNewsDTO breakingNewsDTO = breakingNewsMapper.toDto(breakingNews);//조회된 속보뉴스 DTO변환

        return breakingNewsDTO;//속보뉴스 articleDTO 리턴
    }
    
    //속보뉴스 등록
    public Long create(BreakingNewsCreateDTO breakingNewsCreateDTO, String userId){
        
        //String userId = userAuthService.authUser.getUserId();//토큰에서 사용자 아이디 get
        breakingNewsCreateDTO.setInputrId(userId); //등록자 set
        
        //등록DTO 엔티티 변환
        BreakingNews breakingNews = breakingNewsCreateMapper.toEntity(breakingNewsCreateDTO);
        
        //등록
        breakingNewsRepository.save(breakingNews);

        Long breakingNewsId = breakingNews.getBreakingNewsId(); //등록하고 만들어진 속보뉴스 아이디 get

        //속보뉴스 등록으로 들어온 속보뉴스 상세 리스트 get
        List<BreakingNewsDtlCreateDTO> breakingNewsDtlCreateDTOList = breakingNewsCreateDTO.getBreakingNewsDtls();

        //속보뉴스 상세 등록
        createDetail(breakingNewsDtlCreateDTOList, breakingNewsId);

        return breakingNewsId; //속보뉴스 아이디 리턴
    }

    //속보뉴스 수정
    public void update(BreakingNewsUpdateDTO breakingNewsUpdateDTO, Long breakingNewsId, String userId){

        BreakingNews breakingNews = findBreakingNews(breakingNewsId);//속보뉴스 조회 및 존재유무 확인 [조회조건 속보뉴스 아이디]

        //String userId = userAuthService.authUser.getUserId();//토큰에서 사용자 아이디 get
        breakingNewsUpdateDTO.setUpdtrId(userId);//수정자 set

        breakingNewsUpdateMapper.updateFromDto(breakingNewsUpdateDTO, breakingNews);//기존정보에 업데이트 정보 set
        breakingNewsRepository.save(breakingNews);//수정

        //속보뉴스 수정으로 들어온 속보뉴스 상세 리스트 get
        List<BreakingNewsDtlCreateDTO> breakingNewsDtlCreateDTOList = breakingNewsUpdateDTO.getBreakingNewsDtls();
        //속보뉴스 상세 수정[기존등록된 속보뉴스 상세 삭제후 재등록]
        updateDetail(breakingNewsDtlCreateDTOList, breakingNewsId);
    }

    //속보뉴스 삭제.
    public void delete(Long breakingNewsId, String userId){

        BreakingNews breakingNews = findBreakingNews(breakingNewsId);//속보뉴스 조회 및 존재유무 확인 [조회조건 속보뉴스 아이디]

        BreakingNewsDTO breakingNewsDTO = breakingNewsMapper.toDto(breakingNews);
        //String userId = userAuthService.authUser.getUserId();//토큰에서 사용자 아이디 get
        breakingNewsDTO.setDelrId(userId);//삭제자 set
        breakingNewsDTO.setDelDtm(new Date()); //삭제일시 set
        breakingNewsDTO.setDelYn("Y");//삭제여부값 "Y" set

        breakingNewsMapper.updateFromDto(breakingNewsDTO, breakingNews);//조회된 기존정보에 수정된 삭제정보 업데이트
        
        breakingNewsRepository.save(breakingNews); //삭제정보 저장
        
    }

    //속보뉴스 상세 수정
    public void updateDetail(List<BreakingNewsDtlCreateDTO> breakingNewsDtlCreateDTOList, Long breakingNewsId){

        //기존에 등록되어 있던 속보뉴스 상세 리스트를 불러온다.
        List<BreakingNewsDtl> breakingNewsDtls = breakingNewsDtlRepository.findDetailList(breakingNewsId);

        //조회된 속보뉴스 상세 리스트를 삭제
        for (BreakingNewsDtl detailDTO : breakingNewsDtls){

            Long id = detailDTO.getId();

            breakingNewsDtlRepository.deleteById(id);
        }

        //속보뉴스 상세 재등록
        createDetail(breakingNewsDtlCreateDTOList, breakingNewsId);
    }

    //속보뉴스 상세 리스트 등록
    public void createDetail(List<BreakingNewsDtlCreateDTO> breakingNewsDtlCreateDTOList, Long breakingNewsId){

        //속보뉴스 상세에 등록할 속보뉴스 아이디 빌드
        BreakingNewsSimplerDTO breakingNewsSimplerDTO = BreakingNewsSimplerDTO.builder().breakingNewsId(breakingNewsId).build();

        for (BreakingNewsDtlCreateDTO dtlDTO :  breakingNewsDtlCreateDTOList){

            dtlDTO.setBreakingNews(breakingNewsSimplerDTO); //속보뉴스상세에 속보뉴스 아이디 set
            
            BreakingNewsDtl breakingNewsDtl = breakingNewsDtlCreateMapper.toEntity(dtlDTO);//속보뉴스 상세 등록DTO 엔티티 변환
        
            breakingNewsDtlRepository.save(breakingNewsDtl); //속보뉴스 상세 등록
        }
    }

    //속보뉴스 조회 및 존재유무 확인 [조회조건 속보뉴스 아이디]
    public BreakingNews findBreakingNews(Long breakingNewsId){

        //null방지를 위해 옵셔널 사용 조회
        Optional<BreakingNews> breakingNews = breakingNewsRepository.findBreakingNews(breakingNewsId);

        //조회된 속보뉴스가 없으면 에러
        if (breakingNews.isPresent() == false){
            throw new ResourceNotFoundException("속보뉴스를 찾을 수 없습니다. 속보뉴스 아이디 : "+breakingNewsId);
        }
        //조회된 속보뉴스 리턴
        return breakingNews.get();
    }
    
    //속보뉴스 목록조회 조회조건 빌드
    public BooleanBuilder getSearch(Date sdate, Date edate, String delYn){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QBreakingNews qBreakingNews = QBreakingNews.breakingNews;

        //조회조건이 날짜로 들어온 경우
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false){
            booleanBuilder.and(qBreakingNews.brdcDtm.between(sdate, edate));
        }

        //조회조건이 삭제 여부값으로 들어온 경우
        if (delYn != null && delYn.trim().isEmpty() ==false){
            booleanBuilder.and(qBreakingNews.delYn.eq(delYn));
        }
        else { //삭제 여부값이 안들어왔을 경우 디폴트 N
            booleanBuilder.and(qBreakingNews.delYn.eq("N"));
        }

        return booleanBuilder;
    }

    public BreakingNewsDTO send(BreakingNewsDTO breakingNewsDTO) throws UnsupportedEncodingException {


        List<BreakingNewsSendDTO> breakingNewsSendDTOS = new ArrayList<>();

        List<BreakingNewsDtlDTO> breakingNewsDtls = breakingNewsDTO.getBreakingNewsDtls();
        String lnTypCdNm = breakingNewsDTO.getLnTypCdNm();

        Integer page = 1;

        for (BreakingNewsDtlDTO breakingNewsDtlDTO : breakingNewsDtls){

            BreakingNewsSendDTO breakingNewsSendDTO = new BreakingNewsSendDTO();
            breakingNewsSendDTO.setPage(page);
            breakingNewsSendDTO.setContent(capSeparatorHelper.separator(breakingNewsDtlDTO.getCtt()));
            breakingNewsSendDTO.setTemplate(lnTypCdNm);

            breakingNewsSendDTOS.add(breakingNewsSendDTO);
            ++page;

        }

        BreakingNewsSendListDTO breakingNewsSendListDTO = new BreakingNewsSendListDTO();
        breakingNewsSendListDTO.setCg(breakingNewsSendDTOS);

        /******************* xml 파일 생성 ******************/
        JAXBContext jaxc = null;
        Marshaller marshaller = null;
        File file = null;

        try {
            jaxc = JAXBContext.newInstance(BreakingNewsSendListDTO.class);
        } catch (JAXBException e) {
            log.error("Jaxb instance 생성 에러");
        }

        Date todayDate = new Date();
        String today = dateChangeHelper.dateToStringNoTimeStraight(todayDate);
        today = today.replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]", "");

        //String firstFileNm = getValidFileName(breakingNewsDTO.getTitl());

        String fileNm = breakingNewsDTO.getTitl()+"_"+today;

        fileNm = getValidFileName(fileNm);
        fileNm = fileNm +".xml";

        //fileNm = getValidFileName(fileNm);

        //ftp서버에 한글파일을 쓸때 한글깨짐 방지
        //String tempFileName = new String(fileNm.getBytes("utf-8"),"iso_8859_1");
        String tempFileName = new String(fileNm.getBytes("euc-kr"),"euc-kr");
        //String tempFileName = new String(fileNm.getBytes("utf-8"), "UTF-16");

        file = new File("/data/storage/temp",tempFileName);


        //articleDTO TO XML 파싱
        //String xml = JAXBXmlHelper.marshal(breakingNewsSendListDTO, BreakingNewsSendListDTO.class);

        try {
            marshaller = jaxc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            marshaller.marshal( breakingNewsSendListDTO, file );
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        /******************* ftp 전송 ******************/

        ftpSend(file);

        /******************* ftp 전송 후 전송 Date 업데이트 ******************/
        breakingNewsDTO.setTrnsfDtm(new Date());

        BreakingNews breakingNews = breakingNewsMapper.toEntity(breakingNewsDTO);

        breakingNewsRepository.save(breakingNews);

        return breakingNewsDTO;

    }

    public String getValidFileName(String fileName){

        //String REGEX_PATTERN = "[(),'.%}]";
        String INVALID_WINDOWS_SPECIFIC_CHARS = "[\\\\/:*?\"<>|]";

        String newFileName = fileName.replaceAll(INVALID_WINDOWS_SPECIFIC_CHARS, "").trim();

        if (newFileName.length() == 0){
            throw new IllegalStateException("BreakingNews FileName : "+fileName + "results in a empty fileName!");
        }

        return newFileName;
    }

    public void ftpSend(File file){

        Long id = 1L;

        BreakingNewsFtpInfoDTO breakingNewsFtpInfoDTO = breakingNewsFtpInfoService.find(id);

        log.info("속보뉴스 FTP INFO : "+breakingNewsFtpInfoDTO.toString());

        String ftpIp = breakingNewsFtpInfoDTO.getFtpIp();
        Integer ftpPort = breakingNewsFtpInfoDTO.getFtpPort();
        String ftpId = breakingNewsFtpInfoDTO.getFtpId();
        String ftpPw = breakingNewsFtpInfoDTO.getFtpPw();
        String ftpPath = breakingNewsFtpInfoDTO.getFtpPath();
        String ftpType = breakingNewsFtpInfoDTO.getFtpType();

        //FTP코드 생성자
        FTPconnectService ftp = new FTPconnectService();
        FileInputStream fis = null;

        String fileNm = file.getName();

        try {
            //nam ftp 커넥트
            if ("active".equals(ftpType)) {
                ftp.connectActiveEucKr(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);

            }else if ("passive".equals(ftpType)){
                ftp.connectPassive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);
            }

            try {
                if(file.exists()) {
                    fis = new FileInputStream(file);
                    ftp.storeFile(fileNm, fis); //파일명

                    log.info("속보 뉴스 FTP1 파일명 : "+file.getName()+" fis : "+fis.toString());
                }
            } catch (FileNotFoundException e) {
                log.error("속보 뉴스 FTP file handle exception : "+ "FileNotFoundException");
                // TODO: handle exception
            }catch (IOException e) {
                log.error("속보 뉴스 FTP file handle exception : "+ "IOException");
                // TODO: handle exception
            } finally {
                if(fis != null) {
                    fis.close();
                }
            }
            //ftp.disconnect();
        } catch (NumberFormatException e) {
            log.error("속보 뉴스 FTP file NumberFormatException : "+ "NumberFormatException");
        } catch (Exception e) {
            log.error("속보 뉴스 FTP file Exception : "+ "Exception - "+e.getMessage() );
        } finally {
            if (ftp != null){
                ftp.disconnect();
            }

            if( file.exists() ){
                if(file.delete()){
                    log.info("속보 뉴스 File Delete ");
                }else{
                    log.error("속보 뉴스 file delete failed");
                }
            }

        }

    }
}
