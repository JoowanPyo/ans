package com.gemiso.zodiac.app.yonhap;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.app.file.AttachFileRepository;
import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.file.dto.StatusCodeFileDTO;
import com.gemiso.zodiac.app.file.mapper.AttachFileMapper;
import com.gemiso.zodiac.app.user.UserService;
import com.gemiso.zodiac.app.yonhap.dto.*;
import com.gemiso.zodiac.app.yonhap.mapper.YonhapMapper;
import com.gemiso.zodiac.app.yonhapAssign.YonhapAssign;
import com.gemiso.zodiac.app.yonhapAssign.YonhapAssignRepository;
import com.gemiso.zodiac.app.yonhapAssign.dto.YonhapAssignSimpleDTO;
import com.gemiso.zodiac.app.yonhapAttchFile.YonhapAttchFile;
import com.gemiso.zodiac.app.yonhapAttchFile.YonhapAttchFileRepository;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileCreateDTO;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileDTO;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileSimpleDTO;
import com.gemiso.zodiac.app.yonhapAttchFile.mapper.YonhapAttachFileMapper;
import com.gemiso.zodiac.app.yonhapPhoto.dto.YonhapExceptionDomain;
import com.gemiso.zodiac.app.yonhapPotoAttchFile.YonhapPhotoAttchFileRepository;
import com.gemiso.zodiac.app.yonhapWireAttchFile.YonhapWireAttchFileRepository;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import com.gemiso.zodiac.core.helper.PageHelper;
import com.gemiso.zodiac.core.page.PageResultDTO;
import com.gemiso.zodiac.core.util.PropertyUtil;
import com.gemiso.zodiac.core.util.UploadFileBean;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class YonhapService {

    private final YonhapRepository yonhapRepository;
    private final YonhapAttchFileRepository yonhapAttchFileRepository;
    private final AttachFileRepository attachFileRepository;
    private final YonhapAssignRepository yonhapAssignRepository;
    //private final YonhapPhotoAttchFileRepository yonhapPhotoAttchFileRepository;
    //private final YonhapWireAttchFileRepository yonhapWireAttchFileRepository;

    private final YonhapMapper yonhapMapper;
    private final AttachFileMapper attachFileMapper;
    //private final YonhapAttachFileMapper yonhapAttachFileMapper;

    private final DateChangeHelper dateChangeHelper;
    private final UserService userService;

    public PageResultDTO<YonhapDTO, Yonhap> findAll(Date sdate, Date edate, String artclCateCd, /*List<String> region_cds,*/
                                   String search_word, String svcTyp, Integer page, Integer limit) {

        //????????? ?????? page, limit null?????? page = 1 limit = 50 ????????? ??????
        PageHelper pageHelper = new PageHelper(page, limit);
        Pageable pageable = pageHelper.getYonhapPage();

        BooleanBuilder booleanBuilder = getSearch(sdate, edate, artclCateCd/*, region_cds*/, search_word, svcTyp);

        Page<Yonhap> yonhapList = yonhapRepository.findByYonhapList(sdate, edate, artclCateCd, /*region_cds,*/
                search_word, svcTyp, pageable);

        Function<Yonhap, YonhapDTO> fn = (entity -> yonhapMapper.toDto(entity));


        return new PageResultDTO<YonhapDTO, Yonhap>(yonhapList, fn);

    }

    public YonhapDTO find(Long yonhapId) {

        log.info("?????? Find       :::"+yonhapId);

        Optional<Yonhap> yonhapEntity = yonhapRepository.findByYonhapId(yonhapId);

        if (yonhapEntity.isPresent() == false){
            throw new ResourceNotFoundException("??????????????? ?????? ??? ????????????. ???????????? ????????? : "+yonhapId);
        }

        Yonhap yonhap = yonhapEntity.get();

        //List<YonhapAttchFile> yonhapAttchFile = yonhapAttchFileRepository.findYonhapFile(yonhapId);

        YonhapDTO yonhapDTO = yonhapMapper.toDto(yonhap);

       /* List<YonhapAttchFile> attchFile = yonhapAttchFileRepository.findId(yonhapId);
        List<YonhapAttachFileDTO> yonhapAttachFileDTOS = yonhapAttachFileMapper.toDtoList(attchFile);

        yonhapDTO.setYonhapAttchFiles(yonhapAttachFileDTOS);*/

        return yonhapDTO;
    }

    public YonhapExceptionDomain create(YonhapCreateDTO yonhapCreateDTO) throws Exception {

        log.info("??????DTO ??????             ::"+yonhapCreateDTO);

        Long yonhapId = null;

        String getEmbgDtm = yonhapCreateDTO.getEmbg_dtm();
        String getInputDtm = yonhapCreateDTO.getInput_dtm();
        String getTrnsfDtm = yonhapCreateDTO.getTrnsf_dtm();

        //String????????? ????????? Date( yyyymmddhhmmss )???????????? ??????
        Date embgDtm = dateChangeHelper.stringToDateNoComma(getEmbgDtm);
        Date inputDtm = dateChangeHelper.stringToDateNoComma(getInputDtm);
        Date trnsfDtm = dateChangeHelper.stringToDateNoComma(getTrnsfDtm);

        /*String artclCateCd = yonhapCreateDTO.getArtcl_cate_cd()*/

        int artclqnty = Integer.parseInt(yonhapCreateDTO.getArtclqnty());

        String contId = yonhapCreateDTO.getCont_id();

        List<Yonhap> yonhap = yonhapRepository.findYonhap(contId);

        //????????? ????????????
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
                        .artclCateCd(yonhapCreateDTO.getArtcl_cate_cd())
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
                        //.inputDtm(inputDtm)
                        .trnsfDtm(trnsfDtm)
                        .action(yonhapCreateDTO.getAction())
                        .build();


            log.info("?????? UPDATE ENTITY       :::"+yonhapEntity);

            try {
                yonhapRepository.save(yonhapEntity);
                yonhapId = yonhapEntity.getYonhapId();
            }catch (RuntimeException e){
                return new YonhapExceptionDomain(yonhapId, "5001", "yonhap", "RuntimeException", "");
            }
            // ????????????
            if (yonhapCreateDTO.getUpload_files() != null && yonhapCreateDTO.getUpload_files().size() > 0) {
                try {
                    uploadYonhapFiles(yonhapId, yonhapCreateDTO.getUpload_files());
                }catch (RuntimeException e){
                    return new YonhapExceptionDomain(yonhapId, "5002", "yonhap", "RuntimeException", "");
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
                    .artclCateCd(yonhapCreateDTO.getArtcl_cate_cd())
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

            log.info("?????? Create ENTITY       :::"+yonhapEntity);

            try {
                yonhapRepository.save(yonhapEntity);
                yonhapId = yonhapEntity.getYonhapId();
            }catch (RuntimeException e){
                return new YonhapExceptionDomain(yonhapId, "5001", "yonhap", "RuntimeException", "");
            }

            // ????????????
            if (yonhapCreateDTO.getUpload_files() != null && yonhapCreateDTO.getUpload_files().size() > 0) {
                try {
                    uploadYonhapFiles(yonhapId, yonhapCreateDTO.getUpload_files());
                }catch (RuntimeException e){
                    return new YonhapExceptionDomain(yonhapId, "5002", "yonhap", "RuntimeException", "");
                }
            }

        }


        return new YonhapExceptionDomain(yonhapId, "2000", "yonhap", "", "");

    }

    public YonhapResponseDTO formatYonhap(YonhapDTO yonhapDTO){

        List<YonhapAttachFileSimpleDTO> yonhapAttchFiles = yonhapDTO.getYonhapAttchFiles();

        List<AttachFileDTO> attachFileDTOS = new ArrayList<>();

        if (CollectionUtils.isEmpty(yonhapAttchFiles) == false) {
            Long[] fileId = new Long[yonhapAttchFiles.size()];

            int i = 0;
            for (YonhapAttachFileSimpleDTO yonhapFileDTO : yonhapAttchFiles) {

                AttachFileDTO attachFileDTO = yonhapFileDTO.getAttachFile();
                fileId[i] = attachFileDTO.getFileId();

                i++;
            }

            List<AttachFile> attachFiles = attachFileRepository.findFileInfo(fileId);
            attachFileDTOS = attachFileMapper.toDtoList(attachFiles);

            for (AttachFileDTO attachFileDTO : attachFileDTOS){
                attachFileDTO.setFileLoc("");
                //attachFileDTO.setFileNm();
            }
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
                .embg_dtm(dateChangeHelper.dateToStringNormal(yonhapDTO.getEmbgDtm()))//Date(yyyy-MM-dd HH:mm:ss)????????? ??????????????? String?????? ??????
                .input_dtm(dateChangeHelper.dateToStringNormal(yonhapDTO.getInputDtm()))//Date(yyyy-MM-dd HH:mm:ss)????????? ??????????????? String?????? ??????
                .trnsf_dtm(dateChangeHelper.dateToStringNormal(yonhapDTO.getTrnsfDtm()))//Date(yyyy-MM-dd HH:mm:ss)????????? ??????????????? String?????? ??????
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
            int uploadsize = Integer.parseInt(ub.getMaxsize().substring(0, ub.getMaxsize().indexOf("MB")));


            SimpleDateFormat fromat1 = new SimpleDateFormat("yyyy");
            SimpleDateFormat fromat2 = new SimpleDateFormat("MMdd");

            Date time = new Date();

            String year = fromat1.format(time);
            String day = fromat2.format(time);

            //????????? ???????????? path
            upDir = ub.getUpdir() +"/"+ year + "/" + day;
            realpath = ub.getDest() + upDir;
            log.info("file size : " + uploadsize+ ", Dest : " + ub.getDest());
            //???????????? ??????
            if (isMakeDir(realpath)) {
                log.info("Directory create: " + realpath);
            }else {
                throw new IOException();
            }

            rname = file.getOriginalFilename();

            log.info("original name: " + rname);

            //?????? ????????? ??????(??????+""+?????????)
            //file_id = attachFileDAO.getFileId();

            //DB ??????
            AttachFileDTO fb = new AttachFileDTO();

            //???????????? ????????? ??????
            orgFileNm = file.getOriginalFilename();

            //fb.setFileId(file_id);
            fb.setOrgFileNm(orgFileNm);
            //fb.setFileNm(rname);
            fb.setFileDivCd(fileDivCd);
            fb.setFileSize((int) file.getSize());
            fb.setFileLoc(upDir);
            // ?????? ????????? ????????? ???????????? ???????????? ??????
            //String userId = userAuthService.authUser.getUserId();
            fb.setInputrId("Yonhap");
            fb.setFileUpldDtm(new Date());
            fb.setInputDtm(new Date());
            //????????? ???????????? ????????? ????
            //	fb.setFile_upldr_id("system");

            //DB??? insert
            AttachFile attachFileEntity = attachFileMapper.toEntity(fb);
            attachFileRepository.save(attachFileEntity);

            fileId = attachFileEntity.getFileId();

            //DB??? insert
            //attachFileDAO.insertStorageFile(fb);

            if (log.isDebugEnabled()) {
                log.debug("attach file insert ok: " + fileId);
            }

            //???????????? ???????????? ??????
           /* if (ub.getRname_yn().equals("N") == false) {
                //????????? ??????
                ext = cutExtension(rname);

                rname = fileId + "." + ext;
            }*/

            File uploadFile = null;

            if (rname != null && rname.trim().isEmpty() == false){

                if( rname.endsWith(".doc") || rname.endsWith(".hwp") || rname.endsWith(".pdf") || rname.endsWith(".xls") ||
                        rname.endsWith(".png") || rname.endsWith(".svg") || rname.endsWith(".txt") || rname.endsWith(".zip") ||
                        rname.endsWith(".jpg") || rname.endsWith(".xml")){

                    //String fileName = file.getOriginalFilename();
                    //???????????? ???????????? ??????
                    if (ub.getRname_yn().equals("N") == false) {
                        //????????? ??????
                        ext = cutExtension(rname);

                        rname = fileId + "." + ext;
                    }
                    uploadFile = new File(realpath + File.separator + rname);
                }
            }

            byte[] bytes = file.getBytes();

            BufferedOutputStream buffStream = null;
            FileOutputStream fileOutputStream = new FileOutputStream(uploadFile);
            //FileOutputStream fileOutputStream = new FileOutputStream(uploadFile);

            try {

                //????????? ???????????? ???????????? ????????? ??????
                // YYYYMMDD+FI+seq ??????????????? ?????????...
                buffStream = new BufferedOutputStream(fileOutputStream);

                //?????? ??????
                buffStream.write(bytes);
            }
            catch (IOException e)
            {
                //System.out.println("BufferedReader ???????????? ??? ?????? ??????");
                log.error("???????????? IOException");
            }
            finally {
                try {
                    if (buffStream != null) {
                        buffStream.close();
                    }
                    if (fileOutputStream != null){
                        fileOutputStream.close();
                    }
                }catch (IOException e){
                    log.error("???????????? buffStream.close() - IOException");
                }

            }

            AttachFile attachFile = attachFileRepository.findById(fileId)
                    .orElseThrow(() -> new ResourceNotFoundException("File not found. FileId :"));

            AttachFileDTO attachFileDTO = attachFileMapper.toDto(attachFile);

            attachFileDTO.setFileNm(rname);
            AttachFile aveAttachFile = attachFileMapper.toEntity(attachFileDTO);
            attachFileRepository.save(aveAttachFile);

            msg = "/store/"+upDir+"/"+rname;
            log.info("msg: "+msg);
            /*buffStream.close();*/
            msg += " - Uploaded (" + file.getOriginalFilename() + ")";
            code = 200;

            log.info("attach file start : " + fileId);
            
            //HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

            //return attachFileDTO;

        } catch (IOException e) {

            log.error("IOException Occured "+"IOException");
        }
        //return new StatusCodeFileDTO(codeDTO, msg, file_id, org_file_nm);
        return new YonhapFileResponseDTO(fileId);
    }

    public YonhapAssignSimpleDTO createAssign(YonhapAssignCreateDTO yonhapAssignCreateDTO, String userId){

        YonhapSimpleDTO yonhapSimpleDTO = yonhapAssignCreateDTO.getYonhap();
        Long yonhapId = yonhapSimpleDTO.getYonhapId();

        Yonhap yonhap = yonhapRepository.findById(yonhapId)
                .orElseThrow(() -> new ResourceNotFoundException("?????????????????? ?????? ??? ????????????. Yonhap Id : " + yonhapId));

        Yonhap yonhapEntity = Yonhap.builder().yonhapId(yonhapId).build();

        //????????? ?????????
        String designatorId = yonhapAssignCreateDTO.getDesignatorId();

        userService.userFindOrFail(designatorId);//????????? ????????? ??????

        YonhapAssign yonhapAssign = YonhapAssign.builder()
                .yonhap(yonhapEntity)
                .designatorId(designatorId)
                .assignerId(userId)
                .build();

        yonhapAssignRepository.save(yonhapAssign);

        Long assignId = yonhapAssign.getAssignId();

        YonhapAssignSimpleDTO yonhapAssignSimpleDTO = new YonhapAssignSimpleDTO();
        yonhapAssignSimpleDTO.setAssignId(assignId);

        return yonhapAssignSimpleDTO;

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

    public BooleanBuilder getSearch(Date sdate, Date edate, String artclCateCd, /*List<String> region_cds,*/
                                    String searchWord, String svcTyp) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QYonhap qYonhap = QYonhap.yonhap;

        //?????? ??????????????? ????????? ??????
        if (ObjectUtils.isEmpty(sdate) ==false && ObjectUtils.isEmpty(edate) == false) {
            booleanBuilder.and(qYonhap.inputDtm.between(sdate, edate));
        }
        //????????????
        if (artclCateCd != null && artclCateCd.trim().isEmpty() == false) {

            booleanBuilder.and(qYonhap.artclCateCd.eq(artclCateCd));
        }
        //???????????????
        /*if (CollectionUtils.isEmpty(region_cds) == false) {
            booleanBuilder.and(qYonhap.regionCd.in(region_cds));
        }*/
        //?????????
        //if (search_word != null & search_word.trim().isEmpty() == false) {
        //    booleanBuilder.and(qYonhap.artclTitl.contains(search_word));
        //}
        //????????? ?????? ( ?????? AKRO, ?????? AENO )
        if (svcTyp != null && svcTyp.trim().isEmpty() == false){
            booleanBuilder.and(qYonhap.svcTyp.eq(svcTyp));
        }

        if (searchWord != null && searchWord.trim().isEmpty() == false){
            booleanBuilder.and(qYonhap.artclTitl.contains(searchWord));
        }

        return booleanBuilder;
    }

    public void uploadYonhapFiles(Long yonhapId, List<YonhapAttachFileCreateDTO> yh_attc_file_vo_list) throws Exception {


        for (YonhapAttachFileCreateDTO createDTO : yh_attc_file_vo_list) {


            Long fileId = createDTO.getUpdate_file_id();

            Yonhap yonhap = Yonhap.builder().yonhapId(yonhapId).build();


            AttachFile attachFile = AttachFile.builder().fileId(fileId).build();

            YonhapAttchFile yonhapAttchFile = YonhapAttchFile.builder()
                    .yonhap(yonhap)
                    .attachFile(attachFile)
                    .fileOrd(createDTO.getFile_ord())
                    .fileTitl(createDTO.getFile_titl())
                    .mimeType(createDTO.getMime_typ())
                    .cap(createDTO.getCap())
                    .yhUrl(createDTO.getYh_url())
                    .build();

            yonhapAttchFileRepository.save(yonhapAttchFile);
        }


    }

    public void deleteYonhapFile(Long id){

        List<YonhapAttchFile> yonhapAttchFileList = yonhapAttchFileRepository.findFile(id);

        if (CollectionUtils.isEmpty(yonhapAttchFileList) == false) {
            for (YonhapAttchFile yonhapAttchFile : yonhapAttchFileList) {

                yonhapAttchFileRepository.deleteById(yonhapAttchFile.getId());

            }
        }
    }

    //???????????? ??????
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

    //???????????? ????????? ??????
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

    //aptn?????? ??????
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

    /*//?????????????????? ??????
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
