package com.gemiso.zodiac.app.yonhapWire;

import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.app.file.AttachFileRepository;
import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.file.mapper.AttachFileMapper;
import com.gemiso.zodiac.app.yonhap.YonhapService;
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
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class YonhapWireService {

    private final YonhapWireRepository yonhapWireRepository;
    //private final AttachFileRepository attachFileRepository;
    //private final YonhapWireAttchFileRepository yonhapWireAttchFileRepository;

    private final YonhapWireMapper yonhapWireMapper;
    //private final AttachFileMapper attachFileMapper;
    //private final YonhapWireAttchFileMapper yonhapWireAttchFileMapper;


    private final DateChangeHelper dateChangeHelper;


    public List<YonhapWireDTO> findAll(Date sdate, Date edate, String agcyCd,
                                             String searchWord, List<String> imprtList){

        BooleanBuilder booleanBuilder = getSearch(sdate, edate, agcyCd, searchWord, imprtList);

        List<YonhapWire> yonhapWireList = (List<YonhapWire>) yonhapWireRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "inputDtm"));

        List<YonhapWireDTO> yonhapWireDTOList = yonhapWireMapper.toDtoList(yonhapWireList);

        return yonhapWireDTOList;

    }

    public Long create(YonhapWireCreateDTO yonhapWireCreateDTO) throws Exception {

        Long yhWireId = null;

        //새로 들어온 연합에 contId로 기존에 데이터가 있는지 조회.
        String contId = yonhapWireCreateDTO.getCont_id();
        List<YonhapWire> yonhapWireList = yonhapWireRepository.findYhArtclId(contId);

        //기존연합이 있을경우 updqte
        if (CollectionUtils.isEmpty(yonhapWireList) == false) {

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

    public YonhapExceptionDomain createReuter(YonhapReuterCreateDTO yonhapReuterCreateDTO){

        Long reuterId = null;

        String contId = yonhapReuterCreateDTO.getWire_artcl_id();

        List<YonhapWire> yonhapWireList = yonhapWireRepository.findYhArtclId(contId);

        if (CollectionUtils.isEmpty(yonhapWireList) == false){

            reuterId = yonhapWireList.get(0).getWireId();

            YonhapWire yonhapWire = YonhapWire.builder()
                    .wireId(reuterId)
                    .contId(contId)
                    .artclTitl(yonhapReuterCreateDTO.getWire_artcl_titl())
                    .artclCtt(yonhapReuterCreateDTO.getWire_artcl_ctt())
                    .agcyNm("REUTERS")//009
                    .agcyCd("reuters")
                    .build();

            try {
                yonhapWireRepository.save(yonhapWire); //외신 aptn 등록
            }catch (RuntimeException e){
                return new YonhapExceptionDomain(reuterId, "5001", "yonhapAptn", e.getMessage(), "");
            }

        }else {

            YonhapWire yonhapWire = YonhapWire.builder()
                    .contId(contId)
                    .artclTitl(yonhapReuterCreateDTO.getWire_artcl_titl())
                    .artclCtt(yonhapReuterCreateDTO.getWire_artcl_ctt())
                    .agcyNm("REUTER")//009
                    .agcyCd("reuter")
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

    private BooleanBuilder getSearch(Date sdate, Date edate, String agcyCd,
                                     String searchWord, List<String> imprtList){

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
