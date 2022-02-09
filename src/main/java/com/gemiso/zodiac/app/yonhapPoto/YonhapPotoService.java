package com.gemiso.zodiac.app.yonhapPoto;

import com.gemiso.zodiac.app.yonhap.YonhapService;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileCreateDTO;
import com.gemiso.zodiac.app.yonhapPoto.dto.YonhapPotoCreateDTO;
import com.gemiso.zodiac.app.yonhapPoto.dto.YonhapPotoDTO;
import com.gemiso.zodiac.app.yonhapPoto.mapper.YonhapPotoCreateMapper;
import com.gemiso.zodiac.app.yonhapPoto.mapper.YonhapPotoMapper;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class YonhapPotoService {

    private final YonhapPotoRepository yonhapPotoRepository;

    private final YonhapPotoMapper yonhapPotoMapper;
    private final YonhapPotoCreateMapper yonhapPotoCreateMapper;

    private final YonhapService yonhapService;

    //목록조회
    public List<YonhapPotoDTO> findAll(Date sdate, Date edate, String searchWord) {

        //검색조건 빌들
        BooleanBuilder booleanBuilder = getSearch(sdate, edate, searchWord);

        List<YonhapPoto> yonhapPotoList =
                (List<YonhapPoto>) yonhapPotoRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "inputDtm"));

        List<YonhapPotoDTO> yonhapPotoDTOList = yonhapPotoMapper.toDtoList(yonhapPotoList);

        return yonhapPotoDTOList;
    }

    //연합포토 등록
    public Long create(YonhapPotoCreateDTO yonhapPotoCreateDTO) throws Exception {

        log.info("연합DTO 확인             ::" + yonhapPotoCreateDTO);

        Long yonhapId = null;

        String getEmbgDtm = yonhapPotoCreateDTO.getEmbg_dtm();
        String getInputDtm = yonhapPotoCreateDTO.getInput_dtm();
        String getTrnsfDtm = yonhapPotoCreateDTO.getTrnsf_dtm();

        Date embgDtm = stringToDate(getEmbgDtm);
        Date inputDtm = stringToDate(getInputDtm);
        Date trnsfDtm = stringToDate(getTrnsfDtm);

/*        String contId = yonhapPotoCreateDTO.getCont_id();

        List<YonhapPoto> yonhapPotoList = yonhapPotoRepository.findByYonhapPost(contId);

        if (CollectionUtils.isEmpty(yonhapPotoList) == false){

            yonhapId = yonhapPotoList.get(0).getYonhapArtclId();*/

        YonhapPoto yonhapPoto = YonhapPoto.builder()
                .contId(yonhapPotoCreateDTO.getCont_id())
                .imprt(yonhapPotoCreateDTO.getImprt())
                .svcTyp(yonhapPotoCreateDTO.getSvc_typ())
                .artclTitl(yonhapPotoCreateDTO.getArtcl_titl())
                .artclSmltitl(yonhapPotoCreateDTO.getArtcl_smltitl())
                .artclCtt(yonhapPotoCreateDTO.getArtcl_ctt())
                .artclCateCd(yonhapPotoCreateDTO.getArtcl_cate_cd())
                .regionCd(yonhapPotoCreateDTO.getRegion_cd())
                .regionNm(yonhapPotoCreateDTO.getRegion_nm())
                .cttClassCd(yonhapPotoCreateDTO.getCtt_class_cd())
                .cttClassAddCd(yonhapPotoCreateDTO.getCtt_class_add_cd())
                .yonhapPhotoDivCd(yonhapPotoCreateDTO.getYh_photo_div_cd())
                .yonhapPublNo(yonhapPotoCreateDTO.getYh_publ_no())
                .credit(yonhapPotoCreateDTO.getCredit())
                .cmnt(yonhapPotoCreateDTO.getCmnt())
                .inputDtm(inputDtm)
                .trnsfDtm(trnsfDtm)
                .embgDtm(embgDtm)
                .action(yonhapPotoCreateDTO.getAction())
                .build();

        yonhapPotoRepository.save(yonhapPoto);

        Long yonhapPotoId = yonhapPoto.getYonhapArtclId();

        List<YonhapAttachFileCreateDTO> uploadFiles = yonhapPotoCreateDTO.getUpload_files();

        if (CollectionUtils.isEmpty(uploadFiles) == false){

            yonhapService.uploadYonhapFiles(yonhapPotoId, uploadFiles, "06");

        }

        return yonhapPotoId;
    }

    //String To Date
    public Date stringToDate(String str) throws ParseException {

        Date returnDate = new Date();

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyymmddhhmmss");

        if (str != null && str.trim().isEmpty() == false) {
            returnDate = transFormat.parse(str);
        }

        return returnDate;
    }

    //검색조건 빌들
    public BooleanBuilder getSearch(Date sdate, Date edate, String searchWord) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QYonhapPoto qYonhapPoto = QYonhapPoto.yonhapPoto;

        //날짜검색조건
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            booleanBuilder.and(qYonhapPoto.inputDtm.between(sdate, edate));
        }

        //기사제목검색
        if (searchWord != null && searchWord.trim().isEmpty() == false) {
            booleanBuilder.and(qYonhapPoto.artclTitl.contains(searchWord));
        }

        return booleanBuilder;
    }
}
