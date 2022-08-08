package com.gemiso.zodiac.app.dictionary;

import com.gemiso.zodiac.app.dictionary.dto.DictionaryCreateDTO;
import com.gemiso.zodiac.app.dictionary.dto.DictionaryDTO;
import com.gemiso.zodiac.app.dictionary.dto.DictionaryUpdateDTO;
import com.gemiso.zodiac.app.dictionary.mapper.DictionaryCreateMapper;
import com.gemiso.zodiac.app.dictionary.mapper.DictionaryMapper;
import com.gemiso.zodiac.app.dictionary.mapper.DictionaryUpdateMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    private final DictionaryMapper dictionaryMapper;
    private final DictionaryCreateMapper dictionaryCreateMapper;
    private final DictionaryUpdateMapper dictionaryUpdateMapper;

    //private final UserAuthService userAuthService;


    public List<DictionaryDTO> findAll(Date sdate, Date edate, String searchWord) throws Exception { //목록조회

        //검색조건 add
        BooleanBuilder booleanBuilder = getSearch(sdate, edate, searchWord);

        //전체조회.
        List<Dictionary> dictionaryList =
                (List<Dictionary>) dictionaryRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "inputDtm"));

        //searchWord 조건이 있으면 조건에 맞는 엔티티만 articleDTO List로 변환후 리턴
        //searchWord 조건이 없으면 검색된 엔티티 articleDTO List로 변환후 리턴
        List<DictionaryDTO> dictionaryDTOList = dictionaryMapper.toDtoList(dictionaryList);

        return dictionaryDTOList;

    }

    public DictionaryDTO find(Long id){ //  상세조회

        Dictionary dictionary = findDictionary(id);

        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        return dictionaryDTO;
    }

    public Long create(DictionaryCreateDTO dictionaryCreateDTO, String userId){ //등록

        //등록하는 사용자 아이디 set
        //String userId = userAuthService.authUser.getUserId();
        dictionaryCreateDTO.setInputrId(userId);

        Dictionary dictionary = dictionaryCreateMapper.toEntity(dictionaryCreateDTO);

        dictionaryRepository.save(dictionary);

        Long id = dictionary.getId(); //생성된 아이디 리턴.
        return id;

    }

    public void update(DictionaryUpdateDTO dictionaryUpdateDTO, Long id, String userId){ //수정

        Dictionary dictionary = findDictionary(id);

        //수정자 정보 set
        //String userId = userAuthService.authUser.getUserId();
        dictionaryUpdateDTO.setUpdtrId(userId);

        dictionaryUpdateMapper.updateFromDto(dictionaryUpdateDTO, dictionary);

        dictionaryRepository.save(dictionary);


    }

    public void delete(Long dicId, String userId){ //삭제.

        Dictionary dictionary = findDictionary(dicId);

        DictionaryDTO dictionaryDTO = dictionaryMapper.toDto(dictionary);

        //삭제정보 set.
        //String userId = userAuthService.authUser.getUserId();
        dictionaryDTO.setDelrId(userId);
        dictionaryDTO.setDelDtm(new Date());
        dictionaryDTO.setDelYn("Y");

        dictionaryMapper.updateFromDto(dictionaryDTO, dictionary);

        dictionaryRepository.save(dictionary);

    }

    public BooleanBuilder getSearch(Date sdate, Date edate, String searchWord){ //목록조회 쿼리 조건 추가.

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QDictionary qDictionary = QDictionary.dictionary;

        booleanBuilder.and(qDictionary.delYn.eq("N"));

        //등록날짜 기준으로 조회
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            booleanBuilder.and(qDictionary.inputDtm.between(sdate, edate));
        }

        return booleanBuilder;

    }

    public Dictionary findDictionary(Long dicId){ // 단어사전 아이디 값으로 엔티티 find

        Optional<Dictionary> dictionary = dictionaryRepository.findByDicId(dicId);

        if (dictionary.isPresent() == false){
            throw new ResourceNotFoundException("해당 아이디로 단어사전 정보를 찾을 수 없습니다. 단어사전 아이디 : "+dicId);
        }

        return dictionary.get();

    }

    /*public UserSimpleDTO entityToDtoUser(MisUser user){ //사용자 정보 DTO변환

        UserSimpleDTO userSimpleDTO = null;

        //null일 경우 NullPointerException이 나기때문에 예외처리.
        if (ObjectUtils.isEmpty(user)==false) {
            String userId = user.getUserId();
            String userNm = user.getUserNm();

            userSimpleDTO = UserSimpleDTO.builder().userId(userId).userNm(userNm).build();

        }
        return userSimpleDTO;
    }*/

    /*public Map<String, String> jsonToDto(JSONObject json) throws Exception { //Json To Map [wordKo, wordEn]

        Map<String, String> map = null;
        //json.toJSONString()이 null이경우 error가 나기때문에 예외처리.
        if (ObjectUtils.isEmpty(json) == false) {
            map = new ObjectMapper().readValue(json.toJSONString(), Map.class);
        }

        return map;
    }*/
    
//////////////////////////////////////////////////Json 빌드 작업
    /*public Dictionary toEntity(DictionaryCreateDTO dictionaryCreateDTO){ // 등록 articleDTO To Entity

        Dictionary dictionary = Dictionary.builder()
                .expl(dictionaryCreateDTO.getExpl())
                .wordKo(dtoToJson(dictionaryCreateDTO.getWordKo()))//map으로 들어온 파라미터를 json으로 변환
                .wordEn(dtoToJson(dictionaryCreateDTO.getWordEn()))//map으로 들어온 파라미터를 json으로 변환
                .inputr(dtoToUser(dictionaryCreateDTO.getInputr()))
                .build();

        return dictionary;

    }*/

    /*public JSONObject dtoToJson(Map<String, String> word){ //map으로 들어온 파라미터를 json으로 변환

        JSONObject json =  new JSONObject(word);

        return json;
    }*/

   /* public MisUser dtoToUser(UserSimpleDTO userSimpleDTO){//dto로 들어온 유저아이디를 Entity로 변환

        String userId = userSimpleDTO.getUserId();

        MisUser user = MisUser.builder().userId(userId).build();

        return user;

    }*/


    //목록조회된 엔티티 리스트 To articleDTO List 변환
    //searchWord[검색어]있으면 검색조건에 해당하는 엔티티만 변환후 List에 추가 후 리턴.
    /*public List<DictionaryDTO> toEntityList(List<Dictionary> dictionaryList, String searchWord) throws Exception {

        List<DictionaryDTO> dictionaryDTOList = new ArrayList<>();

        if (searchWord == null || "".equals(searchWord)) //검색조건이 안들어와 왔을경우 entity to takerCueFindAllDTO 변환후 List 리턴
        {
            for (Dictionary dictionary : dictionaryList) {
                DictionaryDTO dictionaryDTO = DictionaryDTO.builder()
                        .id(dictionary.getId())
                        .expl(dictionary.getExpl())
                        .wordKo(jsonToDto(dictionary.getWordKo())) //Json타입을 Map으로 변환
                        .wordEn(jsonToDto(dictionary.getWordEn()))//Json타입을 Map으로 변환
                        .delDtm(dictionary.getDelDtm())
                        .inputr(entityToDtoUser(dictionary.getInputr())) // User정보를 DTO변환
                        .updtr(entityToDtoUser(dictionary.getUpdtr()))// User정보를 DTO변환
                        .delr(entityToDtoUser(dictionary.getDelr()))// User정보를 DTO변환
                        .delYn(dictionary.getDelYn())
                        .build();

                dictionaryDTOList.add(dictionaryDTO);
            }
        }
        else //검색어로 조건이 들어왔을 경우 Json타입에 Like조건이 안되므로 for문으로 검사.
        {
            for (Dictionary dictionary : dictionaryList) {
                DictionaryDTO dictionaryDTO = DictionaryDTO.builder()
                        .id(dictionary.getId())
                        .expl(dictionary.getExpl())
                        .wordKo(jsonToDto(dictionary.getWordKo()))//Json타입을 Map으로 변환
                        .wordEn(jsonToDto(dictionary.getWordEn()))//Json타입을 Map으로 변환
                        .delDtm(dictionary.getDelDtm())
                        .inputr(entityToDtoUser(dictionary.getInputr()))// User정보를 DTO변환
                        .updtr(entityToDtoUser(dictionary.getUpdtr()))// User정보를 DTO변환
                        .delr(entityToDtoUser(dictionary.getDelr()))// User정보를 DTO변환
                        .delYn(dictionary.getDelYn())
                        .build();

                //wordKo,wordEn Json타입을 Map으로변환 후 map.containKey or Value 조건으로 검색조건 검사후 같은것만 리스트에 저장.
                Map<String, String> wordKo =  dictionaryDTO.getWordKo();
                Map<String, String> wordEn  = dictionaryDTO.getWordEn();

                if (CollectionUtils.isEmpty(wordKo) == false){
                    if (wordKo.containsKey(searchWord) || wordKo.containsValue(searchWord)){
                        dictionaryDTOList.add(dictionaryDTO);
                        continue;
                    }
                }
                if (CollectionUtils.isEmpty(wordEn) == false){
                    if (wordEn.containsKey(searchWord) || wordEn.containsValue(searchWord)){
                        dictionaryDTOList.add(dictionaryDTO);
                        continue;
                    }
                }

            }

        }

        return dictionaryDTOList;
    }*/
}
