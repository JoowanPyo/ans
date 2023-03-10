package com.gemiso.zodiac.app.scrollNews;

import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsCreateDTO;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsDTO;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsSimpleDTO;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsUpdateDTO;
import com.gemiso.zodiac.app.scrollNews.mapper.ScrollNewsCreateMapper;
import com.gemiso.zodiac.app.scrollNews.mapper.ScrollNewsMapper;
import com.gemiso.zodiac.app.scrollNews.mapper.ScrollNewsUpdateMapper;
import com.gemiso.zodiac.app.scrollNewsDetail.ScrollNewsDetail;
import com.gemiso.zodiac.app.scrollNewsDetail.ScrollNewsDetailRepository;
import com.gemiso.zodiac.app.scrollNewsDetail.dto.ScrollNewsDetailCreateDTO;
import com.gemiso.zodiac.app.scrollNewsDetail.dto.ScrollNewsDetailCttJsonDTO;
import com.gemiso.zodiac.app.scrollNewsDetail.mapper.ScrollNewsDetailCreateMapper;
import com.gemiso.zodiac.app.scrollNewsFtpInfo.ScrollNewsFtpInfo;
import com.gemiso.zodiac.app.scrollNewsFtpInfo.ScrollNewsFtpInfoService;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import com.gemiso.zodiac.core.helper.MarshallingJsonHelper;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.service.FTPconnectService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScrollNewsService {

    private final ScrollNewsRepository scrollNewsRepository;
    private final ScrollNewsDetailRepository scrollNewsDetailRepository;

    private final ScrollNewsMapper scrollNewsMapper;
    private final ScrollNewsCreateMapper scrollNewsCreateMapper;
    private final ScrollNewsUpdateMapper scrollNewsUpdateMapper;
    private final ScrollNewsDetailCreateMapper scrollNewsDetailCreateMapper;

    //private final UserAuthService userAuthService;
    private final ScrollNewsFtpInfoService scrollNewsFtpInfoService;

    private final DateChangeHelper dateChangeHelper;
    private final MarshallingJsonHelper marshallingJsonHelper;


    //FTP?????? ?????????
    FTPconnectService ftp = new FTPconnectService();
    FileInputStream fis = null;

    //????????? ?????? ????????????
    @Transactional
    public List<ScrollNewsDTO> findAll(Date sdate, Date edate, String delYn) {

        //????????? ?????? ???????????? ??????
        BooleanBuilder booleanBuilder = getSearch(sdate, edate, delYn);

        //????????? ?????????????????? ????????? ?????? ????????? ?????? ??????
        List<ScrollNews> scrollNews =
                (List<ScrollNews>) scrollNewsRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "inputDtm"));

        //??????????????? ????????? ????????? DTO??????
        List<ScrollNewsDTO> scrollNewsDTOList = scrollNewsMapper.toDtoList(scrollNews);

        //articleDTO ????????? ??????
        return scrollNewsDTOList;
    }

    //????????? ?????? ????????????
    @Transactional
    public ScrollNewsDTO find(Long scrlNewsId) {

        ScrollNews scrollNews = findScrollNews(scrlNewsId); //??????????????? ???????????? ?????? ??? ???????????? ??????

        ScrollNewsDTO scrollNewsDTO = scrollNewsMapper.toDto(scrollNews);

        return scrollNewsDTO;

    }

    public void send(ScrollNewsDTO scrollNewsDTO) {

        String fileNm = scrollNewsDTO.getFileNm();
        String scrlFrmCd = scrollNewsDTO.getScrlFrmCd();
        Long scrlNewsId = scrollNewsDTO.getScrlNewsId();
        List<ScrollNewsDetail> scrollNewsDetailList = scrollNewsDetailRepository.findDetailsList(scrlNewsId);

        String makeFileNm = fileNm + ".txt";

        try {

            //String url= System.getProperty("user.dir");

            //File makeForder = new File(System.getProperty("user.dir") + File.separator + "storage" + File.separator + "send_text");
            File makeForder = new File("data/storage/send_text");

            if (makeForder.exists() == false) {

                try {

                    //Path directoryPath = Paths.get(System.getProperty("user.dir") + File.separator + "storage" + File.separator + "send_text");
                    Path directoryPath = Paths.get("/data/storage/send_text");
                    Files.createDirectories(directoryPath);//?????? ???????????????.
                    //System.out.println("????????? ?????????????????????.");
                } catch (FileAlreadyExistsException e) {
                    log.error("????????? ?????? send ???????????? ?????? ?????? : "+"FileAlreadyExistsException");
                    //e.getStackTrace();
                } catch (NoSuchFileException e) {
                    log.error("????????? ?????? send ???????????? ?????? ?????? : "+"NoSuchFileException");
                    //e.getStackTrace();
                }catch (IOException e) {
                    log.error("????????? ?????? send ???????????? ?????? ?????? : "+"IOException");
                    //e.getStackTrace();
                }
            }

            File file = null;
            /* FIX */
            if( makeFileNm != null && !"".equals( makeFileNm ) )
            {
                /* ????????????(directory traversal) ????????? ?????? */
                makeFileNm = makeFileNm.replaceAll("/", ""); // "/" ?????????
                makeFileNm = makeFileNm.replaceAll("\\\\", ""); // "\" ?????????
                makeFileNm = makeFileNm.replaceAll("\\.\\.", ""); // ".." ?????????

                file = new File("/data/storage/send_text/"+ makeFileNm );
            }

            // 1. ?????? ?????? ??????
            //File file = new File("/data/storage/send_text/"+ makeFileNm);
            //File file = new File(System.getProperty("user.dir")+File.separator+"storage"+File.separator+"send_text"+File.separator+fileNm+".txt");

            // 2. ?????? ???????????? ?????? ??? ??????
            if (file.exists() == false) {
                file.createNewFile();
            }

            //Writer ??????
            FileWriter fw = new FileWriter(file);
            PrintWriter writer = new PrintWriter(fw);

            try {


                if ("scroll_typ_scroll".equals(scrlFrmCd)) {

                    //????????? ??????
                    writer.write("{#SC}\n");
                    writer.println();

                    for (ScrollNewsDetail entity : scrollNewsDetailList) {

                        String cttJson = entity.getCttJson();
                        String title = entity.getTitl();

                        writer.println("*sc " + title);

                        JSONParser parser = new JSONParser();
                        Object obj = parser.parse(cttJson);
                        JSONArray jsonObj = (JSONArray) obj;

                        for (int i = 0; i < jsonObj.size(); i++) {
                            JSONObject object = (JSONObject) jsonObj.get(i);
                            String line = (String) object.get("line");
                            writer.println("=" + line);
                        }
                        writer.println();
                    }


                } else if ("scroll_typ_standup".equals(scrlFrmCd)) {

                    //????????? ??????
                    writer.write("{#RU}\n");
                    writer.println();

                    for (ScrollNewsDetail entity : scrollNewsDetailList) {

                        String cttJson = entity.getCttJson();
                        String title = entity.getTitl();

                        writer.println("*sc " + title);

                        JSONParser parser = new JSONParser();
                        Object obj = parser.parse(cttJson);
                        JSONArray jsonObj = (JSONArray) obj;

                        for (int i = 0; i < jsonObj.size(); i++) {
                            JSONObject object = (JSONObject) jsonObj.get(i);
                            String line = (String) object.get("line");
                            writer.println("=" + line);
                        }

                        writer.println();
                    }


                } else {
                    log.error(" ????????? ?????? ????????? ?????? ???????????? . scrlFrmCd - " + scrlFrmCd);
                }

                writer.print("####");
                //writer.close();

            }catch (IndexOutOfBoundsException e){

                log.error("????????? ?????? ????????? ?????? : "+"IndexOutOfBoundsException");
            } finally {
                writer.close();
                fw.close();
            }


        } catch (IOException e) {

            log.error(" ????????? ?????? ?????? : massage - " + "IOException");

        }catch (ParseException e) {

            log.error(" ????????? ?????? ?????? : massage - " + "ParseException");

        }

        sendTextFile(makeFileNm);

    }

    //FTP?????? [ ???????????? ??? 5??? ]
    public void sendTextFile(String makeFileNm) {

        //FTP?????? [ 1 ]
        sendFile1(makeFileNm);

        //FTP?????? [ 2 ]
        sendFile2(makeFileNm);

        //FTP?????? [ 3 ]
        sendFile3(makeFileNm);

        //FTP?????? [ 4 ]
        sendFile4(makeFileNm);

        //FTP?????? [ 5 ]
        sendFile5(makeFileNm);


    }
    //FTP?????? [ ???????????? 5 ]
    public void sendFile5(String makeFileNm) {


        Long id = 5L;

        ScrollNewsFtpInfo scrollNewsFtpInfo = scrollNewsFtpInfoService.findScrollFtpInfo(id);


        String ftpIp = scrollNewsFtpInfo.getFtpIp();
        Integer ftpPort = scrollNewsFtpInfo.getFtpPort();
        String ftpId = scrollNewsFtpInfo.getFtpId();
        String ftpPw = scrollNewsFtpInfo.getFtpPw();
        String ftpPath = scrollNewsFtpInfo.getFtpPath();
        String ftpType = scrollNewsFtpInfo.getFtpType();

        try {
            //nam ftp ?????????
            if ("active".equals(ftpType)) {
                ftp.connectActive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);

            }else if ("passive".equals(ftpType)){
                ftp.connectPassive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);
            }

            File file = null;
            /* FIX */
            if( makeFileNm != null && !"".equals( makeFileNm ) )
            {
                /* ????????????(directory traversal) ????????? ?????? */
                makeFileNm = makeFileNm.replaceAll("/", ""); // "/" ?????????
                makeFileNm = makeFileNm.replaceAll("\\\\", ""); // "\" ?????????
                makeFileNm = makeFileNm.replaceAll("\\.\\.", ""); // ".." ?????????

                file = new File("/data/storage/send_text/" + makeFileNm );
            }
            //File file = new File(System.getProperty("user.dir") + File.separator + "storage" + File.separator + "send_text" + File.separator +makeFileNm);

            try {
                if(file.exists()) {
                    fis = new FileInputStream(file);
                    ftp.storeFile(makeFileNm, fis); //?????????

                    log.info("FTP5 ????????? : "+makeFileNm+" fis : "+fis.toString());
                }
            } catch (FileNotFoundException e) {
                log.error("FTP5 file handle exception : "+ "FileNotFoundException");
                // TODO: handle exception
            }catch (IOException e) {
                log.error("FTP5 file handle exception : "+ "IOException");
                // TODO: handle exception
            } finally {
                if(fis != null) {
                    fis.close();
                }
            }
            ftp.disconnect();
        } catch (NumberFormatException e) {
            log.error("FTP5 file NumberFormatException : "+ "NumberFormatException");
        } catch (IOException e) {
            log.error("FTP5 file Exception : "+ "IOException");
        } catch (Exception e) {
            log.error("FTP5 file Exception : "+ "Exception");
        }
    }

    //FTP?????? [ ???????????? 4 ]
    public void sendFile4(String makeFileNm) {


        Long id = 4L;

        ScrollNewsFtpInfo scrollNewsFtpInfo = scrollNewsFtpInfoService.findScrollFtpInfo(id);


        String ftpIp = scrollNewsFtpInfo.getFtpIp();
        Integer ftpPort = scrollNewsFtpInfo.getFtpPort();
        String ftpId = scrollNewsFtpInfo.getFtpId();
        String ftpPw = scrollNewsFtpInfo.getFtpPw();
        String ftpPath = scrollNewsFtpInfo.getFtpPath();
        String ftpType = scrollNewsFtpInfo.getFtpType();

        try {
            //nam ftp ?????????
            if ("active".equals(ftpType)) {
                ftp.connectActive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);

            }else if ("passive".equals(ftpType)){
                ftp.connectPassive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);
            }

            File file = null;
            /* FIX */
            if( makeFileNm != null && !"".equals( makeFileNm ) )
            {
                /* ????????????(directory traversal) ????????? ?????? */
                makeFileNm = makeFileNm.replaceAll("/", ""); // "/" ?????????
                makeFileNm = makeFileNm.replaceAll("\\\\", ""); // "\" ?????????
                makeFileNm = makeFileNm.replaceAll("\\.\\.", ""); // ".." ?????????

                file = new File("/data/storage/send_text/" + makeFileNm );
            }
            //File file = new File(System.getProperty("user.dir") + File.separator + "storage" + File.separator + "send_text" + File.separator +makeFileNm);

            try {
                if(file.exists()) {
                    fis = new FileInputStream(file);
                    ftp.storeFile(makeFileNm, fis); //?????????

                    log.info("FTP4 ????????? : "+makeFileNm+" fis : "+fis.toString());
                }
            } catch (FileNotFoundException e) {
                log.error("FTP4 file handle exception : "+ "FileNotFoundException");
                // TODO: handle exception
            }catch (IOException e) {
                log.error("FTP4 file handle exception : "+ "IOException");
                // TODO: handle exception
            }finally {

                if(fis != null) {
                    fis.close();
                }
            }
            ftp.disconnect();
        } catch (NumberFormatException e) {
            log.error("FTP4 file NumberFormatException : "+ "NumberFormatException");
        } catch (Exception e) {
            log.error("FTP4 file Exception : "+ "Exception");
        }
    }

    //FTP?????? [ ???????????? 3 ]
    public void sendFile3(String makeFileNm) {


        Long id = 3L;

        ScrollNewsFtpInfo scrollNewsFtpInfo = scrollNewsFtpInfoService.findScrollFtpInfo(id);


        String ftpIp = scrollNewsFtpInfo.getFtpIp();
        Integer ftpPort = scrollNewsFtpInfo.getFtpPort();
        String ftpId = scrollNewsFtpInfo.getFtpId();
        String ftpPw = scrollNewsFtpInfo.getFtpPw();
        String ftpPath = scrollNewsFtpInfo.getFtpPath();
        String ftpType = scrollNewsFtpInfo.getFtpType();

        try {
            //nam ftp ?????????
            if ("active".equals(ftpType)) {
                ftp.connectActive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);

            }else if ("passive".equals(ftpType)){
                ftp.connectPassive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);
            }

            File file = null;
            /* FIX */
            if( makeFileNm != null && !"".equals( makeFileNm ) )
            {
                /* ????????????(directory traversal) ????????? ?????? */
                makeFileNm = makeFileNm.replaceAll("/", ""); // "/" ?????????
                makeFileNm = makeFileNm.replaceAll("\\\\", ""); // "\" ?????????
                makeFileNm = makeFileNm.replaceAll("\\.\\.", ""); // ".." ?????????

                file = new File("/data/storage/send_text/" + makeFileNm );
            }
            //File file = new File(System.getProperty("user.dir") + File.separator + "storage" + File.separator + "send_text" + File.separator +makeFileNm);

            try {

                if(file.exists()) {
                    fis = new FileInputStream(file);
                    ftp.storeFile(makeFileNm, fis); //?????????

                    log.info("FTP3 ????????? : "+makeFileNm+" fis : "+fis.toString());
                }
            } catch (FileNotFoundException e) {
                log.error("FTP3 file handle exception : "+ "FileNotFoundException");
                // TODO: handle exception
            }catch (IOException e) {
                log.error("FTP3 file handle exception : "+ "IOException");
                // TODO: handle exception
            } finally {
                if(fis != null) {
                    fis.close();
                }
            }
            ftp.disconnect();
        } catch (NumberFormatException e) {
            log.error("FTP3 file NumberFormatException : "+ "NumberFormatException");
        } catch (Exception e) {
            log.error("FTP3 file Exception : "+ "Exception");
        }
    }

    //FTP?????? [ ???????????? 2 ]
    public void sendFile2(String makeFileNm) {

        Long id = 2L;

        ScrollNewsFtpInfo scrollNewsFtpInfo = scrollNewsFtpInfoService.findScrollFtpInfo(id);


        String ftpIp = scrollNewsFtpInfo.getFtpIp();
        Integer ftpPort = scrollNewsFtpInfo.getFtpPort();
        String ftpId = scrollNewsFtpInfo.getFtpId();
        String ftpPw = scrollNewsFtpInfo.getFtpPw();
        String ftpPath = scrollNewsFtpInfo.getFtpPath();
        String ftpType = scrollNewsFtpInfo.getFtpType();

        try {
            //nam ftp ?????????
            if ("active".equals(ftpType)) {
                ftp.connectActive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);

            }else if ("passive".equals(ftpType)){
                ftp.connectPassive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);
            }

            File file = null;
            /* FIX */
            if( makeFileNm != null && !"".equals( makeFileNm ) )
            {
                /* ????????????(directory traversal) ????????? ?????? */
                makeFileNm = makeFileNm.replaceAll("/", ""); // "/" ?????????
                makeFileNm = makeFileNm.replaceAll("\\\\", ""); // "\" ?????????
                makeFileNm = makeFileNm.replaceAll("\\.\\.", ""); // ".." ?????????

                file = new File("/data/storage/send_text/" + makeFileNm );
            }
            //File file = new File(System.getProperty("user.dir") + File.separator + "storage" + File.separator + "send_text" + File.separator +makeFileNm);

            try {

                if(file.exists()) {
                    fis = new FileInputStream(file);
                    ftp.storeFile(makeFileNm, fis); //?????????

                    log.info("FTP2 ????????? : "+makeFileNm+" fis : "+fis.toString());
                }
            }catch (FileNotFoundException e) {
                log.error("FTP2 file handle exception : "+ "FileNotFoundException");
                // TODO: handle exception
            }catch (IOException e) {
                log.error("FTP2 file handle exception : "+ "IOException");
                // TODO: handle exception
            } finally {
                if(fis != null) {
                    fis.close();
                }
            }
            ftp.disconnect();
        } catch (NumberFormatException e) {
            log.error("FTP2 file NumberFormatException : "+ "NumberFormatException");
        } catch (Exception e) {
            log.error("FTP2 file Exception : "+ "Exception");
        }
    }

    //FTP?????? [ ???????????? 1 ]
    public void sendFile1(String makeFileNm) {

        Long id = 1L;

        ScrollNewsFtpInfo scrollNewsFtpInfo = scrollNewsFtpInfoService.findScrollFtpInfo(id);


        String ftpIp = scrollNewsFtpInfo.getFtpIp();
        Integer ftpPort = scrollNewsFtpInfo.getFtpPort();
        String ftpId = scrollNewsFtpInfo.getFtpId();
        String ftpPw = scrollNewsFtpInfo.getFtpPw();
        String ftpPath = scrollNewsFtpInfo.getFtpPath();
        String ftpType = scrollNewsFtpInfo.getFtpType();

        try {
            //nam ftp ?????????
            if ("active".equals(ftpType)) {
                ftp.connectActive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);

            }else if ("passive".equals(ftpType)){
                ftp.connectPassive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);
            }

            File file = null;
            /* FIX */
            if( makeFileNm != null && !"".equals( makeFileNm ) )
            {
                /* ????????????(directory traversal) ????????? ?????? */
                makeFileNm = makeFileNm.replaceAll("/", ""); // "/" ?????????
                makeFileNm = makeFileNm.replaceAll("\\\\", ""); // "\" ?????????
                makeFileNm = makeFileNm.replaceAll("\\.\\.", ""); // ".." ?????????

                file = new File("/data/storage/send_text/" + makeFileNm );
            }

            //File file = new File("/data/storage/send_text/" +makeFileNm);

            try {
                if(file.exists()) {
                    fis = new FileInputStream(file);
                    ftp.storeFile(makeFileNm, fis); //?????????

                    log.info("FTP1 ????????? : "+makeFileNm+" fis : "+fis.toString());
                }
            } catch (FileNotFoundException e) {
                log.error("FTP1 file handle exception : "+ "FileNotFoundException");
                // TODO: handle exception
            }catch (IOException e) {
                log.error("FTP1 file handle exception : "+ "IOException");
                // TODO: handle exception
            } finally {
                if(fis != null) {
                    fis.close();
                }
            }
            ftp.disconnect();
        } catch (NumberFormatException e) {
            log.error("FTP1 file NumberFormatException : "+ "NumberFormatException");
        } catch (Exception e) {
            log.error("FTP1 file Exception : "+ "Exception");
        }
    }

    //????????? ?????? ??????
    @Transactional
    public Long create(ScrollNewsCreateDTO scrollNewsCreateDTO, String userId) throws Exception {

        //String userId = userAuthService.authUser.getUserId(); //?????? ????????? ????????? get
        scrollNewsCreateDTO.setInputrId(userId); //????????? set

        String fileNm = scrollNewsCreateDTO.getFileNm();
        String disasterFileNm = null;
        //???????????? ???????????? ?????? ????????? + 00 ?????? ????????? ??????
        String scrlDivCd = scrollNewsCreateDTO.getScrlDivCd();
        if ("scroll_disaster".equals(scrlDivCd)){

            Integer count = 0;

            Date nowDate = scrollNewsCreateDTO.getBrdcDtm();

            String stringDate = dateChangeHelper.dateToStringNoTime(nowDate);
            Date dateDate = dateChangeHelper.StringToDateNoTime(stringDate);

            SearchDate searchDate = new SearchDate(dateDate, dateDate);

            Date ddd = searchDate.getStartDate();
            Date eee = searchDate.getEndDate();

            Optional<Integer> scrollNewsCount = scrollNewsRepository.findScrollNewsCount(searchDate.getStartDate(), searchDate.getEndDate(), scrlDivCd);

            if (scrollNewsCount.isPresent() == false){
                count = 0;
            }else {
                count = scrollNewsCount.get();
            }

            String stringNumber = Integer.toString(count);

            if (stringNumber.length() > 1){
                disasterFileNm = fileNm+stringNumber;
            }else {
                disasterFileNm = fileNm+"0"+stringNumber;
            }

            scrollNewsCreateDTO.setFileNm(disasterFileNm);
        }

        ScrollNews scrollNews = scrollNewsCreateMapper.toEntity(scrollNewsCreateDTO);//??????DTO ????????? ??????.
        scrollNewsRepository.save(scrollNews);// ??????

        Long scrlNewsId = scrollNews.getScrlNewsId(); //????????? ?????? ????????? set && ???????????? ????????? ?????? ?????????
        //????????? ?????? ?????? ?????? ????????? get
        List<ScrollNewsDetailCreateDTO> scrollNewsDetailCreateDTO = scrollNewsCreateDTO.getScrollNewsDetails();

        createDetail(scrollNewsDetailCreateDTO, scrlNewsId); //????????? ?????? ?????? ??????.

        return scrlNewsId;
    }

    @Transactional
    public void update(ScrollNewsUpdateDTO scrollNewsUpdateDTO, Long scrlNewsId, String userId) throws Exception {

        ScrollNews scrollNews = findScrollNews(scrlNewsId); //??????????????? ???????????? ?????? ??? ???????????? ??????

        //String userId = userAuthService.authUser.getUserId();//?????? ????????? ????????? get
        scrollNewsUpdateDTO.setUpdtrId(userId); //????????? ??????

        scrollNewsUpdateMapper.updateFromDto(scrollNewsUpdateDTO, scrollNews);//??????????????? ?????? ????????????

        scrollNewsRepository.save(scrollNews); //??????

        //????????? ?????? ?????? ?????? ????????? get
        List<ScrollNewsDetailCreateDTO> scrollNewsDetailCreateDTO = scrollNewsUpdateDTO.getScrollNewsDetail();

        //????????? ?????? ?????? ??????.
        updateDetail(scrollNewsDetailCreateDTO, scrlNewsId);
    }

    //????????? ?????? ??????
    @Transactional
    public void delete(Long scrlNewsId, String userId) {

        ScrollNews scrollNews = findScrollNews(scrlNewsId); //??????????????? ???????????? ?????? ??? ???????????? ??????

        //String userId = userAuthService.authUser.getUserId();//?????? ????????? ????????? get

        ScrollNewsDTO scrollNewsDTO = scrollNewsMapper.toDto(scrollNews);
        scrollNewsDTO.setDelDtm(new Date()); //?????? ?????? set
        scrollNewsDTO.setDelrId(userId); // ????????? set
        scrollNewsDTO.setDelYn("Y"); //?????? ????????? Y

        scrollNewsMapper.updateFromDto(scrollNewsDTO, scrollNews);
        scrollNewsRepository.save(scrollNews); //????????? ?????? ??????

    }

    //????????? ?????? ?????? ????????????
    @Transactional
    public void updateDetail(List<ScrollNewsDetailCreateDTO> scrollNewsDetailCreateDTO, Long scrlNewsId) throws Exception {

        //??????????????? ?????? List??????[????????? ???????????? ?????? ??????????????? ?????? ?????? ??? ?????????]
        List<ScrollNewsDetail> scrollNewsDetails = scrollNewsDetailRepository.findDetailsList(scrlNewsId);

        //????????? ??????????????? ?????? ????????? ??????
        for (ScrollNewsDetail scrollNewsDetail : scrollNewsDetails) {
            Long id = scrollNewsDetail.getId();
            scrollNewsDetailRepository.deleteById(id);
        }

        createDetail(scrollNewsDetailCreateDTO, scrlNewsId); //??????????????? ?????? ?????????

    }

    //????????? ?????? ?????? ????????? ??????
    @Transactional
    public void createDetail(List<ScrollNewsDetailCreateDTO> scrollNewsDetailCreateDTO, Long scrlNewsId) throws Exception {

        //????????? ?????? ????????? ????????? ????????? ?????? ????????? ??????
        ScrollNewsSimpleDTO scrollNewsSimpleDTO = ScrollNewsSimpleDTO.builder().scrlNewsId(scrlNewsId).build();

        //????????? ?????? ?????? ??????DTO ????????? ??????
        for (ScrollNewsDetailCreateDTO dto : scrollNewsDetailCreateDTO) {

            //?????? Json???????????? ??????
            List<ScrollNewsDetailCttJsonDTO> ctts = dto.getCttJsons();
            String returnCtt = "";
            if (CollectionUtils.isEmpty(ctts) == false) {
                returnCtt = marshallingJsonHelper.MarshallingJson(ctts);
            }

            //Rundown.setCttJson(returnCtt);//?????? Json???????????? ??????
            dto.setScrollNews(scrollNewsSimpleDTO);//????????? ?????? ????????? set
            ScrollNewsDetail scrollNewsDetail = scrollNewsDetailCreateMapper.toEntity(dto);//????????? ?????? ??????DTO ????????? ??????
            scrollNewsDetail.setCttJson(returnCtt);
            scrollNewsDetailRepository.save(scrollNewsDetail);//????????? ?????? ?????? ??????

        }

    }

    //?????? Json??????
    //?????? ???????????? ????????? Json??????
 /*   public String entityToJson(String ctt) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        String jsonInString = mapper.writeValueAsString(ctt);

        return jsonInString;
    }*/


    //??????????????? ???????????? ?????? ??? ???????????? ??????
    @Transactional
    public ScrollNews findScrollNews(Long scrlNewsId) {

        //null????????? ?????? ???????????? ????????? ?????? ??????[?????? ?????? ????????? ?????? ????????? && ???????????? N]
        Optional<ScrollNews> scrollNews = scrollNewsRepository.findScrollNews(scrlNewsId);

        if (scrollNews.isPresent() == false) { //????????? ????????? ????????? ????????? ????????????.
            throw new ResourceNotFoundException("????????? ????????? ?????? ??? ????????????. ????????? ?????? ????????? : " + scrlNewsId);
        }

        return scrollNews.get(); //????????? ????????? ?????? ??????
    }

    //????????? ?????? ???????????? ?????? ??????
    @Transactional
    public BooleanBuilder getSearch(Date sdate, Date edate, String delYn) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QScrollNews qScrollNews = QScrollNews.scrollNews;

        //??????????????? ????????? ????????? ??????
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            booleanBuilder.and(qScrollNews.brdcDtm.between(sdate, edate));
        }

        //??????????????? ?????? ??????????????? ????????? ??????
        if (delYn != null && delYn.trim().isEmpty() == false) {
            booleanBuilder.and(qScrollNews.delYn.eq(delYn));
        } else { //?????? ???????????? ??????????????? ?????? ????????? N
            booleanBuilder.and(qScrollNews.delYn.eq("N"));
        }

        return booleanBuilder;
    }

}
