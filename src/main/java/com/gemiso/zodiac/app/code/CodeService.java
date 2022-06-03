package com.gemiso.zodiac.app.code;

import com.gemiso.zodiac.app.code.dto.CodeCreateDTO;
import com.gemiso.zodiac.app.code.dto.CodeDTO;
import com.gemiso.zodiac.app.code.dto.CodeOrdUpdateDTO;
import com.gemiso.zodiac.app.code.dto.CodeUpdateDTO;
import com.gemiso.zodiac.app.code.mapper.CodeCreateMapper;
import com.gemiso.zodiac.app.code.mapper.CodeMapper;
import com.gemiso.zodiac.app.code.mapper.CodeUpdateMapper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CodeService {

    private final CodeRepository codeRepository;

    private final CodeMapper codeMapper;
    private final CodeCreateMapper codeCreateMapper;
    private final CodeUpdateMapper codeUpdateMapper;

    private final UserAuthService userAuthService;


    public List<CodeDTO> findAll(String searchWord, String useYn, List<String> hrnkCdIds) {

        BooleanBuilder booleanBuilder = getSearch(searchWord, useYn, hrnkCdIds);

        List<Code> codeList = (List<Code>) codeRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "cdOrd"));

        List<CodeDTO> codeDTOList = codeMapper.toDtoList(codeList);

        return codeDTOList;

    }

    public List<CodeDTO> findArticleType(String artclTypCd) {

        //artclTypCd = "article_type";

        List<Code> codeList = codeRepository.findArticleTypeCD(artclTypCd);


        //ConcurrentModificationException 에러가 나기때문에 for문을 두번돌려서 모든 기사유형코드 add
        String[] listArr = new String[codeList.size()];
        int i = 0;
        for (Code code : codeList) { //상위코드값 담는다[하위값 조회하기 위해]
            String id = code.getCd();
            listArr[i] = id;
            i++;
        }
        //in쿠리로 하위코드 리스트 조회.
        List<Code> underCodeList = codeRepository.findUnderArticleTypeCD(listArr);

        codeList.addAll(underCodeList);//조회된 하위 코드 리스트를 상위코드 리스트에 포함 하여 리턴.

        List<CodeDTO> codeDTOList = codeMapper.toDtoList(codeList);

        return codeDTOList;
    }

    public CodeDTO find(Long cdId) {

        Code codeEntity = codeFindOrFail(cdId);

        CodeDTO codeDTO = codeMapper.toDto(codeEntity);

        return codeDTO;

    }

    public Long create(CodeCreateDTO codeCreateDTO) {

        String hrnkCd = codeCreateDTO.getHrnkCdId(); //상위코드값 get

        if (ObjectUtils.isEmpty(hrnkCd)) { //상위코드 일 경우
            Optional<Integer> cdOrd = codeRepository.findHrnkOrd(); // 순서번호 최대값 가져오기 null값 방지를 위해 Optional사용

            if (cdOrd.isPresent() == false) { //최초 코드일 경우 기본값(0) set
                codeCreateDTO.setCdOrd(0);
            } else {
                codeCreateDTO.setCdOrd(cdOrd.get() + 1); //조회된 MAX Ord값 +1 set
            }

        } else { //하위 코드일 경우

            Optional<Integer> cdOrd = codeRepository.findOrd(hrnkCd); // 순서번호 최대값 가져오기

            if (cdOrd.isPresent() == false) {
                codeCreateDTO.setCdOrd(0); //최초 하위코드일 경우 기본값(0) set
            } else {
                codeCreateDTO.setCdOrd(cdOrd.get() + 1); //조회된 MAX Ord값 +1 set
            }

        }

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        codeCreateDTO.setInputrId(userId);

        Code codeEntity = codeCreateMapper.toEntity(codeCreateDTO);
        codeRepository.save(codeEntity);

        Long cdId = codeEntity.getCdId();

        return cdId;

    }

    public void update(CodeUpdateDTO codeUpdateDTO, Long cdId) {

        Code code = codeFindOrFail(cdId);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        codeUpdateDTO.setCdId(cdId);
        codeUpdateDTO.setUpdtrId(userId);

        codeUpdateMapper.updateFromDto(codeUpdateDTO, code);
        codeRepository.save(code);

    }

    public void delete(Long cdId) {

        Code code = codeFindOrFail(cdId);

        CodeDTO codeDTO = codeMapper.toDto(code);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        codeDTO.setDelrId(userId);
        codeDTO.setDelYn("Y");
        codeDTO.setDelDtm(new Date());

        codeMapper.updateFromDto(codeDTO, code);

        codeRepository.save(code);
    }

    public void updateOrd(CodeOrdUpdateDTO codeOrdUpdateDTO, Long cdId){

        Code code = codeFindOrFail(cdId);

        Integer cdOrd = codeOrdUpdateDTO.getCdOrd();
        String hrnkCdId = codeOrdUpdateDTO.getHrnkCdId();

        CodeDTO codeDTO = codeMapper.toDto(code);
        codeDTO.setCdOrd(cdOrd);
        codeMapper.updateFromDto(codeDTO, code);
        codeRepository.save(code);

        List<Code> codeList = codeRepository.findCodeList(hrnkCdId);

        for (int i = codeList.size()-1; i >= 0; i-- ){

            Long newCdId = codeList.get(i).getCdId();

            if (newCdId.equals(cdId)){
                codeList.remove(i);
            }
        }

        codeList.add(cdOrd, code);

        int index = 0;
        for (Code codeEntity : codeList){

            CodeDTO updateCodeDTO = codeMapper.toDto(codeEntity);
            updateCodeDTO.setCdOrd(index);
            Code updateCode = codeMapper.toEntity(updateCodeDTO);
            codeRepository.save(updateCode);
            index++;
        }

    }

    public Code codeFindOrFail(Long cdId) {

        /*return codeRepository.findById(cdId)
                .orElseThrow(() -> new ResourceNotFoundException("CodeId not found. CodeId : " + cdId));*/

        Optional<Code> code = codeRepository.findByCodeId(cdId);

        if (!code.isPresent()) {
            throw new ResourceNotFoundException("CodeId not found. CodeId : " + cdId);
        }
        return code.get();

    }

    private BooleanBuilder getSearch(String searchWord, String useYn, List<String> hrnkCdIds) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCode qCode = QCode.code;

        booleanBuilder.and(qCode.delYn.eq("N"));
        if (searchWord != null && searchWord.trim().isEmpty() == false) {
            booleanBuilder.and(qCode.cdNm.contains(searchWord));
        }
        if (useYn != null && useYn.trim().isEmpty() == false) {
            booleanBuilder.and(qCode.useYn.eq(useYn));
        }
        if (CollectionUtils.isEmpty(hrnkCdIds) == false) {

            booleanBuilder.and(qCode.hrnkCdId.in(hrnkCdIds)); //이걸 한개는 and 나머지는 or로 처리 해야한다.

        }

        return booleanBuilder;
    }
}
