package com.gemiso.zodiac.app.yonhapPhoto;

import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.app.file.AttachFileRepository;
import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.file.mapper.AttachFileMapper;
import com.gemiso.zodiac.app.yonhap.YonhapService;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileCreateDTO;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileDTO;
import com.gemiso.zodiac.app.yonhapPhoto.dto.YonhapExceptionDomain;
import com.gemiso.zodiac.app.yonhapPhoto.dto.YonhapPhotoCreateDTO;
import com.gemiso.zodiac.app.yonhapPhoto.dto.YonhapPhotoDTO;
import com.gemiso.zodiac.app.yonhapPhoto.dto.YonhapPhotoDomainDTO;
import com.gemiso.zodiac.app.yonhapPhoto.mapper.YonhapPhotoCreateMapper;
import com.gemiso.zodiac.app.yonhapPhoto.mapper.YonhapPhotoMapper;
import com.gemiso.zodiac.app.yonhapPotoAttchFile.dto.YonhapPhotoAttchFileDTO;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class YonhapPhotoService {

    private final YonhapPhotoRepository yonhapPhotoRepository;
    private final AttachFileRepository attachFileRepository;

    private final YonhapPhotoMapper yonhapPhotoMapper;
    private final AttachFileMapper attachFileMapper;
    //private final YonhapPhotoCreateMapper yonhapPhotoCreateMapper;

    //private final YonhapService yonhapService;

    private final DateChangeHelper dateChangeHelper;

    //연합포토 상세조회
    public List<YonhapPhotoDTO> findAll(Date sdate, Date edate, String searchWord) {

        //검색조건 빌들
        BooleanBuilder booleanBuilder = getSearch(sdate, edate, searchWord);

        List<YonhapPhoto> yonhapPhotoList =
                (List<YonhapPhoto>) yonhapPhotoRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "inputDtm"));

        List<YonhapPhotoDTO> yonhapPhotoDTOList = yonhapPhotoMapper.toDtoList(yonhapPhotoList);

        return yonhapPhotoDTOList;
    }

    //연합포토 상세조회
    public YonhapPhotoDTO find(Long yonhapPhotoId){

        YonhapPhoto yonhapPhoto = findYonhapPhoto(yonhapPhotoId);

        YonhapPhotoDTO YonhapPhotoDTO = yonhapPhotoMapper.toDto(yonhapPhoto);

        return YonhapPhotoDTO;

    }

    //연합포토 등록
    public YonhapExceptionDomain create(YonhapPhotoCreateDTO yonhapPhotoCreateDTO) throws Exception {

        log.info("연합DTO 확인             ::" + yonhapPhotoCreateDTO);

        Long yonhapPhotoId = null;

        String getEmbgDtm = yonhapPhotoCreateDTO.getEmbg_dtm();
        String getInputDtm = yonhapPhotoCreateDTO.getInput_dtm();
        String getTrnsfDtm = yonhapPhotoCreateDTO.getTrnsf_dtm();

        //String데이터 타입을 Date( yyyymmddhhmmss )타입으로 변환
        Date embgDtm = dateChangeHelper.stringToDateNoComma(getEmbgDtm);
        Date inputDtm = dateChangeHelper.stringToDateNoComma(getInputDtm);
        Date trnsfDtm = dateChangeHelper.stringToDateNoComma(getTrnsfDtm);

/*        String contId = YonhapPhotoCreateDTO.getCont_id();

        List<YonhapPhoto> yonhapPotoList = YonhapPhotoRepository.findByYonhapPost(contId);

        if (CollectionUtils.isEmpty(yonhapPotoList) == false){

            yonhapId = yonhapPotoList.get(0).getYonhapArtclId();*/

        YonhapPhoto yonhapPhoto = YonhapPhoto.builder()
                .contId(yonhapPhotoCreateDTO.getCont_id())
                .imprt(yonhapPhotoCreateDTO.getImprt())
                .svcTyp(yonhapPhotoCreateDTO.getSvc_typ())
                .artclTitl(yonhapPhotoCreateDTO.getArtcl_titl())
                .artclSmltitl(yonhapPhotoCreateDTO.getArtcl_smltitl())
                .artclCtt(yonhapPhotoCreateDTO.getArtcl_ctt())
                .artclCateCd(yonhapPhotoCreateDTO.getArtcl_cate_cd())
                .regionCd(yonhapPhotoCreateDTO.getRegion_cd())
                .regionNm(yonhapPhotoCreateDTO.getRegion_nm())
                .cttClassCd(yonhapPhotoCreateDTO.getCtt_class_cd())
                .cttClassAddCd(yonhapPhotoCreateDTO.getCtt_class_add_cd())
                .yonhapPhotoDivCd(yonhapPhotoCreateDTO.getYh_photo_div_cd())
                .yonhapPublNo(yonhapPhotoCreateDTO.getYh_publ_no())
                .credit(yonhapPhotoCreateDTO.getCredit())
                .cmnt(yonhapPhotoCreateDTO.getCmnt())
                .inputDtm(inputDtm)
                .trnsfDtm(trnsfDtm)
                .embgDtm(embgDtm)
                .action(yonhapPhotoCreateDTO.getAction())
                .build();

        try {
            yonhapPhotoRepository.save(yonhapPhoto);
            yonhapPhotoId = yonhapPhoto.getYonhapArtclId();
        }catch (RuntimeException e){
            return new YonhapExceptionDomain(yonhapPhotoId, "5001", "yonhapPhoto", "RuntimeException", "");
        }

        /*//기사에 포함된 첨부파일get
        List<YonhapAttachFileCreateDTO> uploadFiles = yonhapPhotoCreateDTO.getUpload_files();

        //기사저장후 기사첨부파일이 있으면 첨부파일 정보 등록
        if (CollectionUtils.isEmpty(uploadFiles) == false){
            try {
                yonhapService.uploadYonhapFiles(yonhapPhotoId, uploadFiles, "06");
            }catch (RuntimeException e){
                return new YonhapExceptionDomain(yonhapPhotoId, "5002", "yonhapPhoto", e.getMessage(), "");
            }


        }*/

        return new YonhapExceptionDomain(yonhapPhotoId, "2000", "yonhapPhoto", "", "");
    }

    //연합포토 response객체 빌드
    public YonhapPhotoDomainDTO formatYonhapPhoto(YonhapPhotoDTO yonhapPhotoDTO){

        List<YonhapPhotoAttchFileDTO> yonhapPhotoAttchFiles = yonhapPhotoDTO.getYonhapPhotoAttchFiles();

        List<AttachFileDTO> attachFileDTOS = new ArrayList<>();

        if (CollectionUtils.isEmpty(yonhapPhotoAttchFiles) ==false) {
            Long[] fileId = new Long[yonhapPhotoAttchFiles.size()];

            int i = 0;
            for (YonhapPhotoAttchFileDTO photoFileDTO : yonhapPhotoAttchFiles) {

                AttachFileDTO attachFileDTO = photoFileDTO.getAttachFile();
                fileId[i] = attachFileDTO.getFileId();

                i++;
            }

            List<AttachFile> attachFiles = attachFileRepository.findFileInfo(fileId);
            attachFileDTOS = attachFileMapper.toDtoList(attachFiles);
        }

        YonhapPhotoDomainDTO yonhapPhotoDomainDTO = YonhapPhotoDomainDTO.builder()
                .yh_artcl_id(yonhapPhotoDTO.getYonhapArtclId())
                .cont_id(yonhapPhotoDTO.getContId())
                .action(yonhapPhotoDTO.getAction())
                .imprt(yonhapPhotoDTO.getImprt())
                .svc_typ(yonhapPhotoDTO.getSvcTyp())
                .artcl_titl(yonhapPhotoDTO.getArtclTitl())
                .artcl_smltitl(yonhapPhotoDTO.getArtclSmltitl())
                .artcl_ctt(yonhapPhotoDTO.getArtclCtt())
                .artcl_cate_cd(yonhapPhotoDTO.getArtclCateCd())
                .artcl_cate_nm(yonhapPhotoDTO.getArtclCateNm())
                .region_cd(yonhapPhotoDTO.getRegionCd())
                .region_nm(yonhapPhotoDTO.getRegionNm())
                .ctt_class_cd(yonhapPhotoDTO.getCttClassCd())
                .ctt_class_nm(yonhapPhotoDTO.getCttClassNm())
                .ctt_class_add_cd(yonhapPhotoDTO.getCttClassAddCd())
                .ctt_class_add_nm(yonhapPhotoDTO.getCttClassAddNm())
                .yh_photo_div_cd(yonhapPhotoDTO.getYonhapPhotoDivCd())
                .yh_photo_div_nm(yonhapPhotoDTO.getYonhapPhotoDivNm())
                .yh_publ_no(yonhapPhotoDTO.getYonhapPublNo())
                .source(yonhapPhotoDTO.getSrc())
                .credit(yonhapPhotoDTO.getCredit())
                .input_dtm(dateChangeHelper.dateToStringNormal(yonhapPhotoDTO.getInputDtm()))//Date(yyyy-MM-dd HH:mm:ss)형식의 입력일시를 String으로 변환
                .trnsf_dtm(dateChangeHelper.dateToStringNormal(yonhapPhotoDTO.getTrnsfDtm()))//Date(yyyy-MM-dd HH:mm:ss)형식의 입력일시를 String으로 변환
                .embg_dtm(dateChangeHelper.dateToStringNormal(yonhapPhotoDTO.getEmbgDtm()))//Date(yyyy-MM-dd HH:mm:ss)형식의 입력일시를 String으로 변환
                .cmnt(yonhapPhotoDTO.getCmnt())
                .files(attachFileDTOS)
                //.upload_files(YonhapPhotoDTO.getUpload_files())
                .build();

        return yonhapPhotoDomainDTO;
    }

    //검색조건 빌들
    public BooleanBuilder getSearch(Date sdate, Date edate, String searchWord) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QYonhapPhoto qYonhapPhoto = QYonhapPhoto.yonhapPhoto;

        //날짜검색조건
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            booleanBuilder.and(qYonhapPhoto.inputDtm.between(sdate, edate));
        }

        //기사제목검색
        if (searchWord != null && searchWord.trim().isEmpty() == false) {
            booleanBuilder.and(qYonhapPhoto.artclTitl.contains(searchWord));
        }

        return booleanBuilder;
    }

    //연합포토 조회 [상세조회], 존재유무 확인
    public YonhapPhoto findYonhapPhoto(Long YonhapPhoto){

        Optional<YonhapPhoto> yonhapPhoto = yonhapPhotoRepository.findById(YonhapPhoto);

        if (yonhapPhoto.isPresent() == false){
            throw new ResourceNotFoundException("연합포토 정보를 찾을 수 없습니다. 연합포토 아이디 : "+ YonhapPhoto);
        }

        return yonhapPhoto.get();
    }
}
