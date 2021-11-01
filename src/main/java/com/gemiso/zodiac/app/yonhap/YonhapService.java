package com.gemiso.zodiac.app.yonhap;

import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.app.file.AttachFileRepository;
import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.file.mapper.AttachFileMapper;
import com.gemiso.zodiac.app.yonhap.dto.YonhapAttachFileCreateDTO;
import com.gemiso.zodiac.app.yonhap.dto.YonhapAttachFileDTO;
import com.gemiso.zodiac.app.yonhap.dto.YonhapCreateDTO;
import com.gemiso.zodiac.app.yonhap.dto.YonhapDTO;
import com.gemiso.zodiac.app.yonhap.mapper.YonhapAttachFileMapper;
import com.gemiso.zodiac.app.yonhap.mapper.YonhapMapper;
import com.gemiso.zodiac.core.util.PropertyUtil;
import com.gemiso.zodiac.core.util.UploadFileBean;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
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

        yonhapDTO.setUpload_files(yonhapAttachFileDTOS);

        return yonhapDTO;
    }


    public Long create(YonhapCreateDTO yonhapCreateDTO) throws Exception {

        log.info("연합DTO 확인             ::"+yonhapCreateDTO);

        Long yonhapId = null;

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyymmddhhmmss");

        Date embgDtm = null;
        Date inputDtm = null;
        Date trnsfDtm = null;
        if(!StringUtils.isEmpty(yonhapCreateDTO.getEmbg_dtm())) {
            embgDtm = transFormat.parse(yonhapCreateDTO.getEmbg_dtm());
        }
        if(!StringUtils.isEmpty(yonhapCreateDTO.getInput_dtm())) {
            inputDtm = transFormat.parse(yonhapCreateDTO.getInput_dtm());
        }
        if(!StringUtils.isEmpty(yonhapCreateDTO.getTrnsf_dtm())) {
            trnsfDtm = transFormat.parse(yonhapCreateDTO.getTrnsf_dtm());
        }
        int artclqnty = Integer.parseInt(yonhapCreateDTO.getArtclqnty());

        //콘텐츠 아이디로
        if (!StringUtils.isEmpty(yonhapCreateDTO.getCont_id())) {

            List<Yonhap> yonhap = yonhapRepository.findByYonhap(yonhapCreateDTO.getCont_id());

            yonhapId = yonhap.get(0).getYonhapId();

            // 수정
            //yh_artcl_id = yh_vo.getYh_artcl_id();
            //yhArtclId = yonhap.getYhArtclId();

            //yonhapCreateDTO.setYh_artcl_id(yhArtclId);


			/* 지역코드 안들어왔을시, 임의로 넣어주는값 확인.
			if(StringUtils.isEmpty(yh_vo.getRegion_cd())) {
				//지역코드
				yh_vo.setRegion_cd("9999999");
				yh_vo.setRegion_nm("");
			}
			*/


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
                    .cttClassCd(yonhapCreateDTO.getCtt_class_cd())
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


            // 파일등록
            if (yonhapCreateDTO.getUpload_files() != null && yonhapCreateDTO.getUpload_files().size() > 0) {

                uploadYonhapFiles(yonhapId, yonhapCreateDTO.getUpload_files(), "07", yonhapEntity);

            }


        } else {
/*            yh_artcl_id = yonhapDAO.getYonhapId();
            yonhapCreateDTO.setYh_artcl_id(yh_artcl_id);*/


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
                    .cttClassCd(yonhapCreateDTO.getCtt_class_cd())
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

                uploadYonhapFiles(yonhapId, yonhapCreateDTO.getUpload_files(), "07", yonhapEntity);

            }

        }


        return yonhapId;

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

    public void uploadYonhapFiles(Long yh_artcl_id, List<YonhapAttachFileCreateDTO> yh_attc_file_vo_list, String divcd, Yonhap yonhap) throws Exception {


        PropertyUtil xu = new PropertyUtil();
        UploadFileBean ub = new UploadFileBean();

        try {
            //연합 첨부파일 저장 경로를 divcd값으로 불러온다.
            ub = xu.getUploadInfo("FileAttach.xml", "upload" + divcd);

            // int uploadsize = Integer.parseInt(ub.getMaxsize().substring(0, ub.getMaxsize().indexOf("MB")));
            SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.KOREA);
            SimpleDateFormat day = new SimpleDateFormat("MMdd", Locale.KOREA);

            String upDir = ub.getUpdir() + File.separator + year + File.separator + day;

            // String realpath = ub.getDest() + File.separator + upDir;

            if (yh_attc_file_vo_list != null && yh_attc_file_vo_list.size() > 0) {

                for (YonhapAttachFileCreateDTO file : yh_attc_file_vo_list) {
                    YonhapAttchFile yonhapAttchFile = yonhapAttchFileRepository.findFile(file.getFile_id());
                    yonhapAttchFileRepository.deleteById(yonhapAttchFile.getId());
                }

                for (YonhapAttachFileCreateDTO file : yh_attc_file_vo_list) {


                    //파일등록을 로컬에 안해준다?
                    //MultipartFile multipartFile = new MockMultipartFile(file.getFile_titl(), new FileInputStream(new File(upDir)));

                    // 업로드 파일 명
                    String fileName = file.getFile_titl();

                    // 파일 아이디 생성.
                    //String file_id = attachFileDAO.getFileId();

                    //확장자 파싱
                    String ext = cutExtension(fileName);

                    // 파일 리네임 [파일아이디.확장자]
                    //String rname = file_id + "." + ext;
                    // 파일 리네임 [파일아이디.확장자]

                    //String rname = yonhapAttchFile.getId() + "." + ext;

                    AttachFileDTO fb = new AttachFileDTO();

                    //fb.setFile_id(file_id);
                    /*fb.setOrg_file_nm(fileName);
                    fb.setFile_nm(rname);
                    fb.setFile_divcd(divcd);
                    fb.setFile_loc(upDir);*/

                    fb.setOrgFileNm(fileName);
                    //fb.setFileNm(rname);
                    fb.setFileDivCd(divcd);
                    fb.setFileLoc(upDir);
                    //fb.setFile_upldr_id("system");

                    AttachFile attachFile = attachFileMapper.toEntity(fb);

                    attachFileRepository.save(attachFile);

                    // 파일 업로드 경로
                    // String path = realpath + File.separator + rname;
                    //file.setFile_id(file_id);
                    file.setYh_artcl_id(yh_artcl_id);

                    //YonhapAttchFile yonhapAttchFile = yonhapAttachFileMapper.toEntity(file);

                    YonhapAttchFile yonhapAttchFile = YonhapAttchFile.builder()
                            .attachFile(attachFile)
                            .yonhap(yonhap)
                            .fileOrd(file.getFile_ord())
                            .fileTitl(file.getFile_titl())
                            .mimeType(file.getMime_type())
                            .cap(file.getCap())
                            .yhUrl(file.getYh_url())
                            .build();

                    //일단 mars FileAttach.XML에 divcd = 07, Yonhap
                    if (divcd.equals("07")) {
                        yonhapAttchFileRepository.save(yonhapAttchFile);
                    }

                    String rname = yonhapAttchFile.getId() + "." + ext;
                    attachFile.setFileNm(rname);
                    attachFileRepository.save(attachFile);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
