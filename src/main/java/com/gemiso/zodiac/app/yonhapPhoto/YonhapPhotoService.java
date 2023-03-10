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

    //???????????? ????????????
    public List<YonhapPhotoDTO> findAll(Date sdate, Date edate, String searchWord) {

        //???????????? ??????
        BooleanBuilder booleanBuilder = getSearch(sdate, edate, searchWord);

        List<YonhapPhoto> yonhapPhotoList =
                (List<YonhapPhoto>) yonhapPhotoRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "inputDtm"));

        List<YonhapPhotoDTO> yonhapPhotoDTOList = yonhapPhotoMapper.toDtoList(yonhapPhotoList);

        return yonhapPhotoDTOList;
    }

    //???????????? ????????????
    public YonhapPhotoDTO find(Long yonhapPhotoId){

        YonhapPhoto yonhapPhoto = findYonhapPhoto(yonhapPhotoId);

        YonhapPhotoDTO YonhapPhotoDTO = yonhapPhotoMapper.toDto(yonhapPhoto);

        return YonhapPhotoDTO;

    }

    //???????????? ??????
    public YonhapExceptionDomain create(YonhapPhotoCreateDTO yonhapPhotoCreateDTO) throws Exception {

        log.info("??????DTO ??????             ::" + yonhapPhotoCreateDTO);

        Long yonhapPhotoId = null;

        String getEmbgDtm = yonhapPhotoCreateDTO.getEmbg_dtm();
        String getInputDtm = yonhapPhotoCreateDTO.getInput_dtm();
        String getTrnsfDtm = yonhapPhotoCreateDTO.getTrnsf_dtm();

        //String????????? ????????? Date( yyyymmddhhmmss )???????????? ??????
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

        /*//????????? ????????? ????????????get
        List<YonhapAttachFileCreateDTO> uploadFiles = yonhapPhotoCreateDTO.getUpload_files();

        //??????????????? ????????????????????? ????????? ???????????? ?????? ??????
        if (CollectionUtils.isEmpty(uploadFiles) == false){
            try {
                yonhapService.uploadYonhapFiles(yonhapPhotoId, uploadFiles, "06");
            }catch (RuntimeException e){
                return new YonhapExceptionDomain(yonhapPhotoId, "5002", "yonhapPhoto", e.getMessage(), "");
            }


        }*/

        return new YonhapExceptionDomain(yonhapPhotoId, "2000", "yonhapPhoto", "", "");
    }

    //???????????? response?????? ??????
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
                .input_dtm(dateChangeHelper.dateToStringNormal(yonhapPhotoDTO.getInputDtm()))//Date(yyyy-MM-dd HH:mm:ss)????????? ??????????????? String?????? ??????
                .trnsf_dtm(dateChangeHelper.dateToStringNormal(yonhapPhotoDTO.getTrnsfDtm()))//Date(yyyy-MM-dd HH:mm:ss)????????? ??????????????? String?????? ??????
                .embg_dtm(dateChangeHelper.dateToStringNormal(yonhapPhotoDTO.getEmbgDtm()))//Date(yyyy-MM-dd HH:mm:ss)????????? ??????????????? String?????? ??????
                .cmnt(yonhapPhotoDTO.getCmnt())
                .files(attachFileDTOS)
                //.upload_files(YonhapPhotoDTO.getUpload_files())
                .build();

        return yonhapPhotoDomainDTO;
    }

    //???????????? ??????
    public BooleanBuilder getSearch(Date sdate, Date edate, String searchWord) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QYonhapPhoto qYonhapPhoto = QYonhapPhoto.yonhapPhoto;

        //??????????????????
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            booleanBuilder.and(qYonhapPhoto.inputDtm.between(sdate, edate));
        }

        //??????????????????
        if (searchWord != null && searchWord.trim().isEmpty() == false) {
            booleanBuilder.and(qYonhapPhoto.artclTitl.contains(searchWord));
        }

        return booleanBuilder;
    }

    //???????????? ?????? [????????????], ???????????? ??????
    public YonhapPhoto findYonhapPhoto(Long YonhapPhoto){

        Optional<YonhapPhoto> yonhapPhoto = yonhapPhotoRepository.findById(YonhapPhoto);

        if (yonhapPhoto.isPresent() == false){
            throw new ResourceNotFoundException("???????????? ????????? ?????? ??? ????????????. ???????????? ????????? : "+ YonhapPhoto);
        }

        return yonhapPhoto.get();
    }
}
