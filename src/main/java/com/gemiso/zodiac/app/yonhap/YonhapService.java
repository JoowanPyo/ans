package com.gemiso.zodiac.app.yonhap;

import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.app.file.AttachFileRepository;
import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.file.mapper.AttachFileMapper;
import com.gemiso.zodiac.app.yonhap.dto.*;
import com.gemiso.zodiac.app.yonhapAttchFile.YonhapAttchFileRepository;
import com.gemiso.zodiac.app.yonhapAttchFile.mapper.YonhapAttachFileMapper;
import com.gemiso.zodiac.app.yonhap.mapper.YonhapMapper;
import com.gemiso.zodiac.app.yonhapAttchFile.YonhapAttchFile;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileCreateDTO;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileDTO;
import com.gemiso.zodiac.app.yonhapPoto.YonhapPoto;
import com.gemiso.zodiac.app.yonhapPotoAttchFile.YonhapPotoAttchFile;
import com.gemiso.zodiac.app.yonhapPotoAttchFile.YonhapPotoAttchFileRepository;
import com.gemiso.zodiac.core.util.PropertyUtil;
import com.gemiso.zodiac.core.util.UploadFileBean;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class YonhapService {

    private final YonhapRepository yonhapRepository;
    private final YonhapAttchFileRepository yonhapAttchFileRepository;
    private final AttachFileRepository attachFileRepository;
    private final YonhapPotoAttchFileRepository yonhapPotoAttchFileRepository;

    private final YonhapMapper yonhapMapper;
    private final YonhapAttachFileMapper yonhapAttachFileMapper;
    private final AttachFileMapper attachFileMapper;

    public List<YonhapDTO> findAll(Date sdate, Date edate, List<String> artcl_cate_cds, List<String> region_cds, String search_word) {

        BooleanBuilder booleanBuilder = getSearch(sdate, edate, artcl_cate_cds, region_cds, search_word);

        List<Yonhap> yonhapList = (List<Yonhap>) yonhapRepository.findAll(booleanBuilder);

        List<YonhapDTO> yonhapDTOList = yonhapMapper.toDtoList(yonhapList);

        return yonhapDTOList;

    }

    public YonhapDTO find(Long yonhapId) {

        log.info("연합 Find       :::"+yonhapId);

        Yonhap yonhap = yonhapRepository.findById(yonhapId)
                .orElseThrow(() -> new ResourceNotFoundException("Yonhap not found. YonhapId : " + yonhapId));

        YonhapDTO yonhapDTO = yonhapMapper.toDto(yonhap);

        List<YonhapAttchFile> attchFile = yonhapAttchFileRepository.findId(yonhapId);
        List<YonhapAttachFileDTO> yonhapAttachFileDTOS = yonhapAttachFileMapper.toDtoList(attchFile);

        yonhapDTO.setYonhapAttchFiles(yonhapAttachFileDTOS);

        return yonhapDTO;
    }

    //String To Date
    public Date stringToDate(String str) throws ParseException {

        Date returnDate = new Date();

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyymmddhhmmss");

        if (str != null && str.trim().isEmpty() == false){
            returnDate = transFormat.parse(str);
        }

        return returnDate;
    }

    public Long create(YonhapCreateDTO yonhapCreateDTO) throws Exception {

        log.info("연합DTO 확인             ::"+yonhapCreateDTO);

        Long yonhapId = null;

        String getEmbgDtm = yonhapCreateDTO.getEmbg_dtm();
        String getInputDtm = yonhapCreateDTO.getInput_dtm();
        String getTrnsfDtm = yonhapCreateDTO.getTrnsf_dtm();

        Date embgDtm = stringToDate(getEmbgDtm);
        Date inputDtm = stringToDate(getInputDtm);
        Date trnsfDtm = stringToDate(getTrnsfDtm);
 /*       if(!StringUtils.isEmpty(yonhapCreateDTO.getEmbg_dtm())) {
            embgDtm = transFormat.parse(yonhapCreateDTO.getEmbg_dtm());
        }
        if(!StringUtils.isEmpty(yonhapCreateDTO.getInput_dtm())) {
            inputDtm = transFormat.parse(yonhapCreateDTO.getInput_dtm());
        }
        if(!StringUtils.isEmpty(yonhapCreateDTO.getTrnsf_dtm())) {
            trnsfDtm = transFormat.parse(yonhapCreateDTO.getTrnsf_dtm());
        }*/
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

            yonhapRepository.save(yonhapEntity);

            yonhapId = yonhapEntity.getYonhapId();

            // 파일등록
            if (yonhapCreateDTO.getUpload_files() != null && yonhapCreateDTO.getUpload_files().size() > 0) {

                uploadYonhapFiles(yonhapId, yonhapCreateDTO.getUpload_files(), "07");

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

            yonhapRepository.save(yonhapEntity);

            yonhapId = yonhapEntity.getYonhapId();

            // 파일등록
            if (yonhapCreateDTO.getUpload_files() != null && yonhapCreateDTO.getUpload_files().size() > 0) {

                uploadYonhapFiles(yonhapId, yonhapCreateDTO.getUpload_files(), "07");

            }

        }


        return yonhapId;

    }

    public YonhapResponseDTO formatYonhap(YonhapDTO yonhapDTO){

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
                .embg_dtm(dateToString(yonhapDTO.getEmbgDtm()))
                .input_dtm(dateToString(yonhapDTO.getInputDtm()))
                .trnsf_dtm(dateToString(yonhapDTO.getTrnsfDtm()))
                .action(yonhapDTO.getAction())
                .upload_files(yonhapDTO.getYonhapAttchFiles())
                .build();

        return yonhapResponseDTO;
    }

    public String dateToString(Date date){

        String returnDate = "";

        if (ObjectUtils.isEmpty(date) == false) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            returnDate = simpleDateFormat.format(date);
        }
        return returnDate;
    }

    public BooleanBuilder getSearch(Date sdate, Date edate, List<String> artcl_cate_cds, List<String> region_cds, String search_word) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QYonhap qYonhap = QYonhap.yonhap;

        if (!StringUtils.isEmpty(sdate) && !StringUtils.isEmpty(edate)) {
            booleanBuilder.and(qYonhap.inputDtm.between(sdate, edate));
        }
        if (!ObjectUtils.isEmpty(artcl_cate_cds)) {
            for (int i = 0; i < artcl_cate_cds.size(); i++) {
                String artcleCateCd = artcl_cate_cds.get(i);

                if (i == 0) {
                    booleanBuilder.and(qYonhap.artclCateCd.eq(artcleCateCd));
                } else {
                    booleanBuilder.or(qYonhap.artclCateCd.eq(artcleCateCd));
                }
            }
        }
        if (!ObjectUtils.isEmpty(region_cds)) {
            for (int i = 0; i < region_cds.size(); i++) {
                String regionCd = region_cds.get(i);

                if (i == 0) {
                    booleanBuilder.and(qYonhap.regionCd.eq(regionCd));
                } else {
                    booleanBuilder.or(qYonhap.regionCd.eq(regionCd));
                }
            }
        }
        if (!StringUtils.isEmpty(search_word)) {
            booleanBuilder.and(qYonhap.artclTitl.contains(search_word));
        }

        return booleanBuilder;
    }

    public void uploadYonhapFiles(Long yh_artcl_id, List<YonhapAttachFileCreateDTO> yh_attc_file_vo_list, String divcd) throws Exception {


        PropertyUtil xu = new PropertyUtil();
        UploadFileBean ub = new UploadFileBean();

        //연합 첨부파일 저장 경로를 divcd값으로 불러온다.
        ub = xu.getUploadInfo("FileAttach.xml", "upload" + divcd);

        // int uploadsize = Integer.parseInt(ub.getMaxsize().substring(0, ub.getMaxsize().indexOf("MB")));
        SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.KOREA);
        SimpleDateFormat day = new SimpleDateFormat("MMdd", Locale.KOREA);

        String upDir = ub.getUpdir() + File.separator + year + File.separator + day;

        // String realpath = ub.getDest() + File.separator + upDir;

        if (yh_attc_file_vo_list != null && yh_attc_file_vo_list.size() > 0) {

            for (YonhapAttachFileCreateDTO file : yh_attc_file_vo_list) {
                YonhapAttchFile yonhapAttchFile = yonhapAttchFileRepository.findFile(yh_artcl_id);
                yonhapAttchFileRepository.deleteById(yonhapAttchFile.getId());
            }

            for (YonhapAttachFileCreateDTO file : yh_attc_file_vo_list) {


                //파일등록을 로컬에 안해준다?
                //MultipartFile multipartFile = new MockMultipartFile(file.getFile_titl(), new FileInputStream(new File(upDir)));

                int fileSize = file.getFile_size();

                // 업로드 파일 명
                String fileName = file.getFile_nm();
                // 파일 아이디 생성.
                //String file_id = attachFileDAO.getFileId();
                //확장자 파싱
                String ext = cutExtension(fileName);

                // 파일 리네임 [파일아이디.확장자]
                //String rname = file_id + "." + ext;
                // 파일 리네임 [파일아이디.확장자]

                //String rname = yonhapAttchFile.getId() + "." + ext;

                AttachFileDTO fb = new AttachFileDTO();

                //fb.setFile_id(file_id); 아이디 jpa자동생성
                fb.setOrgFileNm(fileName);
                //fb.setFile_nm(rname); 아이디생성 아직 안됫으므로 인설트이후 다시 업데이트
                fb.setFileDivCd(divcd);
                //fb.setFile_ext(utils.cutExtension(fileName)); 확장자 따로 저장 안함
                fb.setFileSize(fileSize);
                fb.setFileLoc(upDir);
                fb.setInputrId("system");

                AttachFile attachFile = attachFileMapper.toEntity(fb);

                attachFileRepository.save(attachFile);

                Long fileId = attachFile.getFileId();

                String rname = fileId + "." + ext; //파일 네임 재생성 아이디+확장자 ex) 123.jpg

                fb.setFileNm(rname);
                
                attachFileMapper.updateFromDto(fb, attachFile);
                
                attachFileRepository.save(attachFile);// fileNm재등록 업데이트

                file.setFile_id(fileId);
                file.setYh_artcl_id(yh_artcl_id);

                if (divcd.equals("07")) {
                    postYonhapFile(file);

                } else if (divcd.equals("06")) {
                    postYonhapPhotoFile(file);

                } /*else if (divcd.equals("01")) {
                    yonhapDAO.postYonhapPressDataFile(file);

                }*/


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
    //연합포토파일 등록
    public void postYonhapPhotoFile(YonhapAttachFileCreateDTO file){

        Long yonhapPotoId = file.getYh_artcl_id();
        Long fileId = file.getFile_id();

        AttachFile attachFile = AttachFile.builder().fileId(fileId).build();
        YonhapPoto yonhapPoto = YonhapPoto.builder().yonhapArtclId(yonhapPotoId).build();

        YonhapPotoAttchFile yonhapPotoAttchFile = YonhapPotoAttchFile.builder()
                .yonhapPoto(yonhapPoto)
                .attachFile(attachFile)
                .fileOrd(file.getFile_ord())
                .fileTypCd(file.getFile_typ_cd())
                .mimeTyp(file.getMime_typ())
                .yonhapUrl(file.getYh_url())
                .expl(file.getExpl())
                .build();

        yonhapPotoAttchFileRepository.save(yonhapPotoAttchFile);

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

}
