package com.gemiso.zodiac.app.cueSheet;

import com.gemiso.zodiac.app.cueSheet.dto.CueSheetCreateDTO;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetDTO;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetUpdateDTO;
import com.gemiso.zodiac.app.cueSheet.mapper.CueSheetCreateMapper;
import com.gemiso.zodiac.app.cueSheet.mapper.CueSheetMapper;
import com.gemiso.zodiac.app.cueSheet.mapper.CueSheetUpdateMapper;
import com.gemiso.zodiac.app.userGroup.QUserGroup;
import com.gemiso.zodiac.app.userGroup.UserGroup;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CueSheetService {

    private final CueSheetRepository cueSheetRepository;

    private final CueSheetMapper cueSheetMapper;
    private final CueSheetCreateMapper cueSheetCreateMapper;
    private final CueSheetUpdateMapper cueSheetUpdateMapper;

    private final UserAuthService userAuthService;



    public List<CueSheetDTO> findAll(Date sdate, Date edate, Long brdcPgmId, String brdcPgmNm, String searchWord){

        BooleanBuilder booleanBuilder = getSearch( sdate,  edate,  brdcPgmId,  brdcPgmNm,  searchWord);

        List<CueSheet> cueSheets = (List<CueSheet>) cueSheetRepository.findAll(booleanBuilder);

        List<CueSheetDTO> cueSheetDTOList = cueSheetMapper.toDtoList(cueSheets);

        return cueSheetDTOList;

    }

    public CueSheetDTO find(Long cueId){

        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        CueSheetDTO cueSheetDTO = cueSheetMapper.toDto(cueSheet);

        return cueSheetDTO;

    }

    public Long create(CueSheetCreateDTO cueSheetCreateDTO){

        String userId = userAuthService.authUser.getUserId();
        cueSheetCreateDTO.setInputrId(userId);

        CueSheet cueSheet = cueSheetCreateMapper.toEntity(cueSheetCreateDTO);

        cueSheetRepository.save(cueSheet);

        return cueSheet.getCueId();
    }

    public void update(CueSheetUpdateDTO cueSheetUpdateDTO, Long cueId){

        //수정! 잠금여부? 잠금사용자, 잠금일시 도 수정할 때 정보를 넣어주나?

        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        cueSheetUpdateDTO.setCueId(cueId);

        cueSheetUpdateMapper.updateFromDto(cueSheetUpdateDTO, cueSheet);

        cueSheetRepository.save(cueSheet);


        //수정! 버전정보 안들어가나요?

    }

    public void delete(Long cueId){

        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        CueSheetDTO cueSheetDTO = cueSheetMapper.toDto(cueSheet);

        String userId = userAuthService.authUser.getUserId();
        cueSheetDTO.setDelDtm(new Date());
        cueSheetDTO.setDelrId(userId);
        cueSheetDTO.setDelYn("Y");

        cueSheetMapper.updateFromDto(cueSheetDTO, cueSheet);

        cueSheetRepository.save(cueSheet);

    }

    public CueSheet cueSheetFindOrFail(Long cueId){

        Optional<CueSheet> cueSheet = cueSheetRepository.findByCue(cueId);

        if (!cueSheet.isPresent()){
            throw new ResourceNotFoundException("CueSheetId not found. cueSheetId : " + cueId);
        }

        return cueSheet.get();

    }

    private BooleanBuilder getSearch(Date sdate, Date edate, Long brdcPgmId, String brdcPgmNm, String searchWord) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCueSheet qCueSheet = QCueSheet.cueSheet;

        booleanBuilder.and(qCueSheet.delYn.eq("N"));

        if (!StringUtils.isEmpty(sdate) && !StringUtils.isEmpty(edate)){
            booleanBuilder.and(qCueSheet.inputDtm.between(sdate, edate));
        }
        if(!StringUtils.isEmpty(brdcPgmId)){
            booleanBuilder.and(qCueSheet.program.brdcPgmId.eq(brdcPgmId));
        }
        if(!StringUtils.isEmpty(brdcPgmNm)){
            booleanBuilder.and(qCueSheet.brdcPgmNm.contains(brdcPgmNm));
        }
        if(!StringUtils.isEmpty(searchWord)){
            booleanBuilder.and(qCueSheet.brdcPgmNm.contains(searchWord).or(qCueSheet.pd1Id.contains(searchWord))
                    .or(qCueSheet.pd2Id.contains(searchWord)));
        }

        return booleanBuilder;
    }
}
