package com.gemiso.zodiac.app.yonhapWire;

import com.gemiso.zodiac.app.yonhapWire.dto.YonhapWireCreateDTO;
import com.gemiso.zodiac.app.yonhapWire.dto.YonhapWireDTO;
import com.gemiso.zodiac.app.yonhapWire.mapper.YonhapCreateMapper;
import com.gemiso.zodiac.app.yonhapWire.mapper.YonhapWireMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class YonhapWireService {

    private final YonhapWireRepository yonhapWireRepository;

    private final YonhapWireMapper yonhapWireMapper;
    private final YonhapCreateMapper yonhapCreateMapper;

    public List<YonhapWireDTO> findAll(Date sdate, Date edate, String agcyCd,
                                             String searchWord, List<String> imprtList){

        BooleanBuilder booleanBuilder = getSearch(sdate, edate, agcyCd, searchWord, imprtList);

        List<YonhapWire> yonhapWireList = (List<YonhapWire>) yonhapWireRepository.findAll(booleanBuilder);

        List<YonhapWireDTO> yonhapWireDTOList = yonhapWireMapper.toDtoList(yonhapWireList);

        return yonhapWireDTOList;

    }

    public Long create(YonhapWireCreateDTO yonhapWireCreateDTO) throws Exception {

        Long yhWireId = null;

        //새로 들어온 연합에 contId로 기존에 데이터가 있는지 조회.
        String contId = yonhapWireCreateDTO.getCont_id();
        List<YonhapWire> yonhapWireList = yonhapWireRepository.findYhArtclId(contId);

        //기존연합이 있을경우 updqte
        if (ObjectUtils.isEmpty(yonhapWireList)) {
            Long OrgYhArtclId = yonhapWireList.get(0).getYhArtclId();
            yonhapWireCreateDTO.setYh_artcl_id(OrgYhArtclId);

            YonhapWire yonhapWire = updateBuildToEntity(yonhapWireCreateDTO);

            yonhapWireRepository.save(yonhapWire);

            yhWireId = yonhapWire.getYhArtclId();

        } else { //기존연합이 없는경우 post

            YonhapWire yonhapWire = postBuildToEntity(yonhapWireCreateDTO);

            yonhapWireRepository.save(yonhapWire);

            yhWireId = yonhapWire.getYhArtclId();
        }

        return yhWireId;
    }

    public YonhapWire updateBuildToEntity(YonhapWireCreateDTO yonhapWireCreateDTO) throws Exception {

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

        YonhapWire yonhapWire = YonhapWire.builder()
                .yhArtclId(yonhapWireCreateDTO.getYh_artcl_id())
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

        YonhapWire yonhapWire = yonhapWireRepository.findById(yhWireId)
                .orElseThrow(() -> new ResourceNotFoundException("YonhapWireId not found. YonhapWireId : " + yhWireId));

        YonhapWireDTO yonhapWireDTO = yonhapWireMapper.toDto(yonhapWire);

        return yonhapWireDTO;

    }

    private BooleanBuilder getSearch(Date sdate, Date edate, String agcyCd,
                                     String searchWord, List<String> imprtList){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QYonhapWire qYonhapWire = QYonhapWire.yonhapWire;

        if (!StringUtils.isEmpty(sdate) && !StringUtils.isEmpty(edate)){
            booleanBuilder.and(qYonhapWire.inputDtm.between(sdate, edate));
        }
        if (!StringUtils.isEmpty(agcyCd)){
            booleanBuilder.and(qYonhapWire.agcyCd.eq(agcyCd));
        }
        if (!StringUtils.isEmpty(searchWord)){
            booleanBuilder.and(qYonhapWire.artclTitl.contains(searchWord));
        }
        if (!ObjectUtils.isEmpty(imprtList)){

            for (int i =0; i < imprtList.size(); i++){
                String imprt = imprtList.get(i);

                if (i == 0){
                    booleanBuilder.and(qYonhapWire.imprt.eq(imprt));
                }else {
                    booleanBuilder.or(qYonhapWire.imprt.eq(imprt));
                }
            }
        }
        return booleanBuilder;
    }
}
