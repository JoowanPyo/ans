package com.gemiso.zodiac.app.yonhap;

import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.app.file.AttachFileRepository;
import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.file.dto.StatusCodeFileDTO;
import com.gemiso.zodiac.app.file.mapper.AttachFileMapper;
import com.gemiso.zodiac.app.yonhap.dto.YonhapCreateDTO;
import com.gemiso.zodiac.app.yonhap.dto.YonhapDTO;
import com.gemiso.zodiac.app.yonhap.dto.YonhapFileResponseDTO;
import com.gemiso.zodiac.app.yonhap.dto.YonhapResponseDTO;
import com.gemiso.zodiac.app.yonhap.mapper.YonhapMapper;
import com.gemiso.zodiac.app.yonhapAttchFile.YonhapAttchFile;
import com.gemiso.zodiac.app.yonhapAttchFile.YonhapAttchFileRepository;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileCreateDTO;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileDTO;
import com.gemiso.zodiac.app.yonhapAttchFile.mapper.YonhapAttachFileMapper;
import com.gemiso.zodiac.app.yonhapPhoto.dto.YonhapExceptionDomain;
import com.gemiso.zodiac.app.yonhapPotoAttchFile.YonhapPhotoAttchFileRepository;
import com.gemiso.zodiac.app.yonhapWireAttchFile.YonhapWireAttchFileRepository;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import com.gemiso.zodiac.core.util.PropertyUtil;
import com.gemiso.zodiac.core.util.UploadFileBean;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class YonhapService {

    private final YonhapRepository yonhapRepository;
    private final YonhapAttchFileRepository yonhapAttchFileRepository;
    private final AttachFileRepository attachFileRepository;
    //private final YonhapPhotoAttchFileRepository yonhapPhotoAttchFileRepository;
    //private final YonhapWireAttchFileRepository yonhapWireAttchFileRepository;

    private final YonhapMapper yonhapMapper;
    private final AttachFileMapper attachFileMapper;
    //private final YonhapAttachFileMapper yonhapAttachFileMapper;

    private final DateChangeHelper dateChangeHelper;

    public List<YonhapDTO> findAll(Date sdate, Date edate, List<String> artcl_cate_cds, List<String> region_cds,
                                   String search_word, String svcTyp) {

        BooleanBuilder booleanBuilder = getSearch(sdate, edate, artcl_cate_cds, region_cds, search_word, svcTyp);

        List<Yonhap> yonhapList = (List<Yonhap>) yonhapRepository.findAll(booleanBuilder);

        List<YonhapDTO> yonhapDTOList = yonhapMapper.toDtoList(yonhapList);

        return yonhapDTOList;

    }

    public YonhapDTO find(Long yonhapId) {

        log.info("연합 Find       :::"+yonhapId);

        Yonhap yonhap = yonhapRepository.findById(yonhapId)
                .orElseThrow(() -> new ResourceNotFoundException("Yonhap not found. YonhapId : " + yonhapId));

        YonhapDTO yonhapDTO = yonhapMapper.toDto(yonhap);

       /* List<YonhapAttchFile> attchFile = yonhapAttchFileRepository.findId(yonhapId);
        List<YonhapAttachFileDTO> yonhapAttachFileDTOS = yonhapAttachFileMapper.toDtoList(attchFile);

        yonhapDTO.setYonhapAttchFiles(yonhapAttachFileDTOS);*/

        return yonhapDTO;
    }

    public YonhapExceptionDomain create(YonhapCreateDTO yonhapCreateDTO) throws Exception {

        log.info("연합DTO 확인             ::"+yonhapCreateDTO);

        Long yonhapId = null;

        String getEmbgDtm = yonhapCreateDTO.getEmbg_dtm();
        String getInputDtm = yonhapCreateDTO.getInput_dtm();
        String getTrnsfDtm = yonhapCreateDTO.getTrnsf_dtm();

        //String데이터 타입을 Date( yyyymmddhhmmss )타입으로 변환
        Date embgDtm = dateChangeHelper.stringToDateNoComma(getEmbgDtm);
        Date inputDtm = dateChangeHelper.stringToDateNoComma(getInputDtm);
        Date trnsfDtm = dateChangeHelper.stringToDateNoComma(getTrnsfDtm);

        int artclqnty = Integer.parseInt(yonhapCreateDTO.getArtclqnty());

        String contId = yonhapCreateDTO.getCont_id();

        List<Yonhap> yonhap = yonhapRepository.findByYonhap(contId);

        //콘텐츠 아이디로
        if (CollectionUtils.isEmpty(yonhap) == false) {

                yonhapId = yonhap.get(0).getYonhapId();

                Yonhap yonhapEntity = Yonhap.builder()
                        .yonhapId(yonhapId)
                        .contId(yonhapCreateDTO.getCont_id())
                        .imprt(yonhapCreateDTO.getImprt())
                        .svcTyp(yonhapCreateDTO.getSvc_typ())
                        .artclTitl(yonhapCreateDTO.getArtcl_titl())
                        .artclSmltitl(yonhapCreateDTO.getArtcl_smltitl())
                        .artclCtt(yonhapCreateDTO.getArtcl_ctt())
                        .credit(yonhapCreateDTO.getCredit())
                        .source(yonhapCreateDTO.getSource())
                        .artclCateCd(yonhapCreateDTO.getArtcl_cate_nm())
                        .regionNm(yonhapCreateDTO.getRegion_nm())
                        .cttClassCd(yonhapCreateDTO.getCtt_class_nm())
                        .cttClassAddCd(yonhapCreateDTO.getCtt_class_add_cd())
                        .issuCd(yonhapCreateDTO.getIssu_cd())
                        .stockCd(yonhapCreateDTO.getStock_cd())
                        .artclqnty(artclqnty)
                        .cmnt(yonhapCreateDTO.getCmnt())
                        .relContId(yonhapCreateDTO.getRel_cont_id())
                        .refContInfo(yonhapCreateDTO.getRef_cont_info())
                        .embgDtm(embgDtm)
                        .inputDtm(inputDtm)
                        .trnsfDtm(trnsfDtm)
                        .action(yonhapCreateDTO.getAction())
                        .build();


            log.info("연합 UPDATE ENTITY       :::"+yonhapEntity);

            try {
                yonhapRepository.save(yonhapEntity);
                yonhapId = yonhapEntity.getYonhapId();
            }catch (RuntimeException e){
                return new YonhapExceptionDomain(yonhapId, "5001", "yonhap", e.getMessage(), "");
            }
            // 파일등록
            if (yonhapCreateDTO.getUpload_files() != null && yonhapCreateDTO.getUpload_files().size() > 0) {
                try {
                    uploadYonhapFiles(yonhapId, yonhapCreateDTO.getUpload_files(), "07");
                }catch (RuntimeException e){
                    return new YonhapExceptionDomain(yonhapId, "5002", "yonhap", e.getMessage(), "");
                }
            }


        } else {

            Yonhap yonhapEntity = Yonhap.builder()
                    .contId(yonhapCreateDTO.getCont_id())
                    .imprt(yonhapCreateDTO.getImprt())
                    .svcTyp(yonhapCreateDTO.getSvc_typ())
                    .artclTitl(yonhapCreateDTO.getArtcl_titl())
                    .artclSmltitl(yonhapCreateDTO.getArtcl_smltitl())
                    .artclCtt(yonhapCreateDTO.getArtcl_ctt())
                    .credit(yonhapCreateDTO.getCredit())
                    .source(yonhapCreateDTO.getSource())
                    .artclCateCd(yonhapCreateDTO.getArtcl_cate_nm())
                    .regionNm(yonhapCreateDTO.getRegion_nm())
                    .cttClassCd(yonhapCreateDTO.getCtt_class_nm())
                    .cttClassAddCd(yonhapCreateDTO.getCtt_class_add_cd())
                    .issuCd(yonhapCreateDTO.getIssu_cd())
                    .stockCd(yonhapCreateDTO.getStock_cd())
                    .artclqnty(artclqnty)
                    .cmnt(yonhapCreateDTO.getCmnt())
                    .relContId(yonhapCreateDTO.getRel_cont_id())
                    .refContInfo(yonhapCreateDTO.getRef_cont_info())
                    .embgDtm(embgDtm)
                    .inputDtm(inputDtm)
                    .trnsfDtm(trnsfDtm)
                    .action(yonhapCreateDTO.getAction())
                    .build();

            log.info("연합 Create ENTITY       :::"+yonhapEntity);

            try {
                yonhapRepository.save(yonhapEntity);
                yonhapId = yonhapEntity.getYonhapId();
            }catch (RuntimeException e){
                return new YonhapExceptionDomain(yonhapId, "5001", "yonhap", e.getMessage(), "");
            }

            // 파일등록
            if (yonhapCreateDTO.getUpload_files() != null && yonhapCreateDTO.getUpload_files().size() > 0) {
                try {
                    uploadYonhapFiles(yonhapId, yonhapCreateDTO.getUpload_files(), "07");
                }catch (RuntimeException e){
                    return new YonhapExceptionDomain(yonhapId, "5002", "yonhap", e.getMessage(), "");
                }
            }

        }


        return new YonhapExceptionDomain(yonhapId, "2000", "yonhap", "", "");

    }

    public YonhapResponseDTO formatYonhap(YonhapDTO yonhapDTO){

        List<YonhapAttachFileDTO> yonhapAttchFiles = yonhapDTO.getYonhapAttchFiles();

        List<AttachFileDTO> attachFileDTOS = new ArrayList<>();

        if (CollectionUtils.isEmpty(yonhapAttchFiles) == false) {
            Long[] fileId = new Long[yonhapAttchFiles.size()];

            int i = 0;
            for (YonhapAttachFileDTO yonhapFileDTO : yonhapAttchFiles) {

                AttachFileDTO attachFileDTO = yonhapFileDTO.getAttachFile();
                fileId[i] = attachFileDTO.getFileId();

                i++;
            }

            List<AttachFile> attachFiles = attachFileRepository.findFileInfo(fileId);
            attachFileDTOS = attachFileMapper.toDtoList(attachFiles);
        }

        YonhapResponseDTO yonhapResponseDTO = YonhapResponseDTO.builder()
                .yh_artcl_id(yonhapDTO.getYonhapId())
                .cont_id(yonhapDTO.getContId())
                .imprt(yonhapDTO.getImprt())
                .svc_typ(yonhapDTO.getSvcTyp())
                .artcl_titl(yonhapDTO.getArtclTitl())
                .artcl_smltitl(yonhapDTO.getArtclSmltitl())
                .artcl_ctt(yonhapDTO.getArtclCtt())
                .credit(yonhapDTO.getCredit())
                .source(yonhapDTO.getSource())
                .artcl_cate_cd(yonhapDTO.getArtclCateCd())
                .artcl_cate_nm(yonhapDTO.getArtclCateNm())
                .region_cd(yonhapDTO.getRegionCd())
                .region_nm(yonhapDTO.getRegionNm())
                .ctt_class_cd(yonhapDTO.getCttClassCd())
                .ctt_class_nm(yonhapDTO.getCttClassNm())
                .ctt_class_add_cd(yonhapDTO.getCttClassAddCd())
                .issu_cd(yonhapDTO.getIssuCd())
                .stock_cd(yonhapDTO.getStockCd())
                .artclqnty(yonhapDTO.getArtclqnty())
                .cmnt(yonhapDTO.getCmnt())
                .rel_cont_id(yonhapDTO.getRelContId())
                .ref_cont_info(yonhapDTO.getRefContInfo())
                .embg_dtm(dateChangeHelper.dateToStringNormal(yonhapDTO.getEmbgDtm()))//Date(yyyy-MM-dd HH:mm:ss)형식의 입력일시를 String으로 변환
                .input_dtm(dateChangeHelper.dateToStringNormal(yonhapDTO.getInputDtm()))//Date(yyyy-MM-dd HH:mm:ss)형식의 입력일시를 String으로 변환
                .trnsf_dtm(dateChangeHelper.dateToStringNormal(yonhapDTO.getTrnsfDtm()))//Date(yyyy-MM-dd HH:mm:ss)형식의 입력일시를 String으로 변환
                .action(yonhapDTO.getAction())
                .files(attachFileDTOS)
                .build();

        return yonhapResponseDTO;
    }

    public YonhapFileResponseDTO fileCreate(MultipartFile file, String fileDivCd){

        int code = 200;

        //AttachFileDTO attachFileDTO = new AttachFileDTO();

        String ext = "";
        String msg = "";
        Long fileId = null;
        String upDir = "";
        String rname = "";
        String realpath = "";
        String orgFileNm = "";

        try {
            PropertyUtil xu = new PropertyUtil();
            UploadFileBean ub = new UploadFileBean();

            ub = xu.getUploadInfo("FileAttach.xml", fileDivCd);
            System.out.println("11111111111111111");
            int uploadsize = Integer.parseInt(ub.getMaxsize().substring(0, ub.getMaxsize().indexOf("MB")));


            SimpleDateFormat fromat1 = new SimpleDateFormat("yyyy");
            SimpleDateFormat fromat2 = new SimpleDateFormat("MMdd");

            Date time = new Date();

            String year = fromat1.format(time);
            String day = fromat2.format(time);

            //업로드 디렉토리 path
            upDir = ub.getUpdir() +"/"+ year + "/" + day;
            realpath = ub.getDest() + upDir;
            log.info("file size : " + uploadsize+ ", Dest : " + ub.getDest());
            //디렉토리 생성
            if (isMakeDir(realpath)) {
                log.info("Directory create: " + realpath);
            }else {
                throw new IOException();
            }

            rname = file.getOriginalFilename();

            log.info("original name: " + rname);

            //파일 아이디 생성(날짜+""+시퀀스)
            //file_id = attachFileDAO.getFileId();

            //DB 등록
            AttachFileDTO fb = new AttachFileDTO();

            //원본파일 아이디 저장
            orgFileNm = file.getOriginalFilename();

            //fb.setFileId(file_id);
            fb.setOrgFileNm(orgFileNm);
            //fb.setFileNm(rname);
            fb.setFileDivCd(fileDivCd);
            fb.setFileSize((int) file.getSize());
            fb.setFileLoc(upDir);
            // 토큰 인증된 사용자 아이디를 입력자로 등록
            //String userId = userAuthService.authUser.getUserId();
            fb.setInputrId("Yonhap");
            fb.setFileUpldDtm(new Date());
            fb.setInputDtm(new Date());
            //로그인 아이디로 바꿔야 함?
            //	fb.setFile_upldr_id("system");

            msg = "/store/"+upDir+"/"+rname;
            log.info("msg: "+msg);

            //DB에 insert
            AttachFile attachFileEntity = attachFileMapper.toEntity(fb);
            attachFileRepository.save(attachFileEntity);

            fileId = attachFileEntity.getFileId();

            //DB에 insert
            //attachFileDAO.insertStorageFile(fb);

            if (log.isDebugEnabled()) {
                log.debug("attach file insert ok: " + fileId);
            }

            //오리지널 파일네임 여부
            if (ub.getRname_yn().equals("N") == false) {
                //확장자 파싱
                ext = cutExtension(rname);

                rname = fileId + "." + ext;
            }


            byte[] bytes = file.getBytes();

            BufferedOutputStream buffStream = null;
            try {

                //파일을 버퍼링을 이용하여 저장할 경로
                // YYYYMMDD+FI+seq 요런식으로 들어감...
                buffStream = new BufferedOutputStream(new FileOutputStream(new File(realpath + File.separator + rname)));

                //파일 복사
                buffStream.write(bytes);
            }
            catch (IOException e)
            {
                //System.out.println("BufferedReader 파일복사 중 에러 발생");
                log.error(e.getMessage());
            }
            finally {
                try {
                    if (buffStream != null) {
                        buffStream.close();
                    }
                }catch (IOException e){
                    log.error(e.getMessage());
                }

            }
            /*buffStream.close();*/
            msg += "Uploaded (" + file.getOriginalFilename() + ")";
            code = 200;


            AttachFile attachFile = attachFileRepository.findById(fileId)
                    .orElseThrow(() -> new ResourceNotFoundException("File not found. FileId :"));

            AttachFileDTO attachFileDTO = attachFileMapper.toDto(attachFile);

            attachFileDTO.setFileNm(rname);
            AttachFile aveAttachFile = attachFileMapper.toEntity(attachFileDTO);
            attachFileRepository.save(aveAttachFile);


            log.info("attach file start : " + fileId);


            HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

            //return attachFileDTO;

        } catch (IOException e) {

            log.error("IOException Occured "+e.getMessage());
        }
        //return new StatusCodeFileDTO(codeDTO, msg, file_id, org_file_nm);
        return new YonhapFileResponseDTO(fileId);
    }

    public boolean isMakeDir(String sdir)
    {
        File dir = new File(sdir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                if (log.isDebugEnabled()) {
                    log.info("isMakeDir Fail: " + sdir);
                }
            }
            else if (log.isDebugEnabled()) {
                log.info("isMakeDir success: " + sdir);
            }
        }

        return true;
    }

    public BooleanBuilder getSearch(Date sdate, Date edate, List<String> artcl_cate_cds, List<String> region_cds,
                                    String search_word, String svcTyp) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QYonhap qYonhap = QYonhap.yonhap;

        //날짜 조회조건이 들어온 경우
        if (ObjectUtils.isEmpty(sdate) ==false && ObjectUtils.isEmpty(edate) == false) {
            booleanBuilder.and(qYonhap.inputDtm.between(sdate, edate));
        }
        //분류코드
        if (CollectionUtils.isEmpty(artcl_cate_cds) == false) {

            booleanBuilder.and(qYonhap.artclCateCd.in(artcl_cate_cds));
        }
        //통신사코드
        if (CollectionUtils.isEmpty(region_cds) == false) {
            booleanBuilder.and(qYonhap.regionCd.in(region_cds));
        }
        //검색어
        //if (search_word != null & search_word.trim().isEmpty() == false) {
        //    booleanBuilder.and(qYonhap.artclTitl.contains(search_word));
        //}
        //서비스 유형 ( 국문 AKRO, 영문 AENO )
        if (svcTyp != null && svcTyp.trim().isEmpty() == false){
            booleanBuilder.and(qYonhap.svcTyp.eq(svcTyp));
        }

        return booleanBuilder;
    }

    public void uploadYonhapFiles(Long yh_artcl_id, List<YonhapAttachFileCreateDTO> yh_attc_file_vo_list, String divcd) throws Exception {




    }

    public void deleteYonhapFile(Long id){

        List<YonhapAttchFile> yonhapAttchFileList = yonhapAttchFileRepository.findFile(id);

        if (CollectionUtils.isEmpty(yonhapAttchFileList) == false) {
            for (YonhapAttchFile yonhapAttchFile : yonhapAttchFileList) {

                yonhapAttchFileRepository.deleteById(yonhapAttchFile.getId());

            }
        }
    }

    //연합파일 등록
    public void postYonhapFile(YonhapAttachFileCreateDTO file){

        Long yhArtclId = file.getYh_artcl_id();
        Long fileId = file.getFile_id();

        Yonhap yonhap = Yonhap.builder().yonhapId(yhArtclId).build();
        AttachFile attachFile = AttachFile.builder().fileId(fileId).build();

        YonhapAttchFile yonhapAttchFile = YonhapAttchFile.builder()
                .yonhap(yonhap)
                .attachFile(attachFile)
                .fileOrd(file.getFile_ord())
                .fileTitl(file.getFile_titl())
                .mimeType(file.getMime_typ())
                .cap(file.getCap())
                .yhUrl(file.getYh_url())
                .build();

        yonhapAttchFileRepository.save(yonhapAttchFile);

    }

    //파일네임 확장자 파싱
    public static String cutExtension(String s) {
        String returnValue = null;
        if (s != null) {
            int index = s.lastIndexOf('.');
            if (index != -1) {
                returnValue = s.substring(index + 1);
            }
        }
        return returnValue;
    }

    /*public void deleteYonhapPhotoFile(Long id){

        List<YonhapPhotoAttchFile> yonhapPhotoAttchFiles = yonhapPhotoAttchFileRepository.findYonhapPhoto(id);

        for (YonhapPhotoAttchFile yonhapPhotoAttchFile : yonhapPhotoAttchFiles){
            Long yonhapPhotoid = yonhapPhotoAttchFile.getId();

            yonhapPhotoAttchFileRepository.deleteById(yonhapPhotoid);
        }
    }

    public void deleteYonhapAptnFile(Long id){

        List<YonhapWireAttchFile> yonhapWireAttchFiles = yonhapWireAttchFileRepository.findByYonhapWireFile(id);

        for (YonhapWireAttchFile yonhapWireAttchFile : yonhapWireAttchFiles){
            Long yonhapWireId = yonhapWireAttchFile.getId();

            yonhapWireAttchFileRepository.deleteById(yonhapWireId);
        }

    }

    //aptn파일 등록
    public void postYonhapAptnFile(YonhapAttachFileCreateDTO file){

        Long yhWireId = file.getYh_artcl_id();
        Long fileId = file.getFile_id();

        YonhapWire yonhapWire = YonhapWire.builder().wireId(yhWireId).build();
        AttachFile attachFile = AttachFile.builder().fileId(fileId).build();

        YonhapWireAttchFile yonhapWireAttchFile = YonhapWireAttchFile.builder()
                .yonhapWire(yonhapWire)
                .attachFile(attachFile)
                .fileOrd(file.getFile_ord())
                .fileTitl(file.getFile_titl())
                .mimeType(file.getMime_typ())
                .cap(file.getCap())
                .yhUrl(file.getYh_url())
                .build();

        yonhapWireAttchFileRepository.save(yonhapWireAttchFile);

    }*/

    /*//연합포토파일 등록
    public void postYonhapPhotoFile(YonhapAttachFileCreateDTO file){

        Long yonhapPotoId = file.getYh_artcl_id();
        Long fileId = file.getFile_id();

        AttachFile attachFile = AttachFile.builder().fileId(fileId).build();
        YonhapPhoto yonhapPhoto = YonhapPhoto.builder().yonhapArtclId(yonhapPotoId).build();

        YonhapPhotoAttchFile yonhapPhotoAttchFile = YonhapPhotoAttchFile.builder()
                .yonhapPhoto(yonhapPhoto)
                .attachFile(attachFile)
                .fileOrd(file.getFile_ord())
                .fileTypCd(file.getFile_typ_cd())
                .mimeTyp(file.getMime_typ())
                .yonhapUrl(file.getYh_url())
                .expl(file.getExpl())
                .build();

        yonhapPhotoAttchFileRepository.save(yonhapPhotoAttchFile);

    }*/

}
