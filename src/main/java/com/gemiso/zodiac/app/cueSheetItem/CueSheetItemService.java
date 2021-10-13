package com.gemiso.zodiac.app.cueSheetItem;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.ArticleRepository;
import com.gemiso.zodiac.app.article.mapper.ArticleMapper;
import com.gemiso.zodiac.app.articleCap.ArticleCap;
import com.gemiso.zodiac.app.articleCap.ArticleCapRepository;
import com.gemiso.zodiac.app.articleMedia.ArticleMedia;
import com.gemiso.zodiac.app.articleMedia.ArticleMediaRepository;
import com.gemiso.zodiac.app.cueSheetItem.dto.*;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemCreateMapper;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemMapper;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemSymbolMapper;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemUpdateMapper;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.UserGroupUserRepository;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import com.gemiso.zodiac.app.userGroup.UserGroupAuthRepository;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CueSheetItemService {

    private final CueSheetItemRepository cueSheetItemRepository;
    private final ArticleRepository articleRepository;
    private final ArticleCapRepository articleCapRepository;
    private final ArticleMediaRepository articleMediaRepository;
    private final UserGroupUserRepository userGroupUserRepository;
    private final UserGroupAuthRepository userGroupAuthRepository;
    private final CueSheetItemSymbolRepository cueSheetItemSymbolRepository;
    //private final CueSheetRepository cueSheetRepository;

    private final CueSheetItemMapper cueSheetItemMapper;
    private final CueSheetItemCreateMapper cueSheetItemCreateMapper;
    private final CueSheetItemUpdateMapper cueSheetItemUpdateMapper;
    private final CueSheetItemSymbolMapper cueSheetItemSymbolMapper;
    private final ArticleMapper articleMapper;

    private final UserAuthService userAuthService;

    public List<CueSheetItemDTO> findAll(Long artclId, Long cueId){

        BooleanBuilder booleanBuilder = getSearch(artclId, cueId);

        List<CueSheetItem> cueSheetItemList = (List<CueSheetItem>) cueSheetItemRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "cueItemOrd"));

        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemMapper.toDtoList(cueSheetItemList);

        cueSheetItemDTOList = setSymbol(cueSheetItemDTOList); //방송아이콘 맵핑 테이블 추가.

        return cueSheetItemDTOList;
    }

    public List<CueSheetItemDTO> setSymbol(List<CueSheetItemDTO> cueSheetItemDTOList){

        for (CueSheetItemDTO cueSheetItemDTO : cueSheetItemDTOList){ //조회된 아이템에 List

            Long cueItemId = cueSheetItemDTO.getCueItemId(); //아이템 아이디 get

            //아이템 아이디로 방송아이콘 맵핑테이블 조회
            List<CueSheetItemSymbol> cueSheetItemSymbolList = cueSheetItemSymbolRepository.findSymbol(cueItemId);

            if (ObjectUtils.isEmpty(cueSheetItemSymbolList) == false){

                List<CueSheetItemSymbolDTO> cueSheetItemSymbolDTO = cueSheetItemSymbolMapper.toDtoList(cueSheetItemSymbolList);

                cueSheetItemDTO.setCueSheetItemSymbolDTO(cueSheetItemSymbolDTO); //아이템에 set방송아이콘List

            }
        }

        return cueSheetItemDTOList;

    }

    public CueSheetItemDTO find(Long cueItemId){

        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItem);

        return cueSheetItemDTO;

    }

    public Long create(CueSheetItemCreateDTO cueSheetItemCreateDTO, Long cueId){


        //큐시트 아이디 등록
        cueSheetItemCreateDTO.setCueId(cueId);
        //토큰 사용자 Id(현재 로그인된 사용자 ID)
        String userId = userAuthService.authUser.getUserId();
        //입력자 등록[ 현재 Entity에 입력자가 타입이 유저이기 때문에 빌드시켜서 set]
        UserSimpleDTO userSimpleDTO = UserSimpleDTO.builder().userId(userId).build();
        cueSheetItemCreateDTO.setInputr(userSimpleDTO);

        CueSheetItem cueSheetItem = cueSheetItemCreateMapper.toEntity(cueSheetItemCreateDTO);
        cueSheetItemRepository.save(cueSheetItem); //큐시트아이템 등록

        //큐시트 아이템 순번 재등록
        Long cueItemId = cueSheetItem.getCueItemId();
        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemList(cueId);

        for (int i = 0; i < cueSheetItemList.size(); i++){
            if (cueItemId.equals(cueSheetItemList.get(i).getCueItemId())){
                cueSheetItemList.remove(i);//신규 등록된 큐시트 아이템 리스트에서 삭제
            }
        }

        cueSheetItemList.add(cueSheetItemCreateDTO.getCueItemOrd(), cueSheetItem); //신규등록하려는 큐시트 아이템 원하는 순번에 리스트 추가

        //조회된 큐시트 아이템 Ord 업데이트
        int index = 0;
        for (CueSheetItem cueSheetItems : cueSheetItemList){

            CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItems);
            cueSheetItemDTO.setCueItemOrd(index);
            CueSheetItem setCueSheetItem = cueSheetItemMapper.toEntity(cueSheetItemDTO);
            cueSheetItemRepository.save(setCueSheetItem);
            index++;
        }

        return cueSheetItem.getCueItemId();
    }

    public void update(CueSheetItemUpdateDTO cueSheetItemUpdateDTO, Long cueId, Long cueItemId){

        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        cueSheetItemUpdateDTO.setCueId(cueId);
        cueSheetItemUpdateDTO.setCueItemId(cueItemId);
        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        UserSimpleDTO userSimpleDTO = UserSimpleDTO.builder().userId(userId).build();
        cueSheetItemUpdateDTO.setUpdtr(userSimpleDTO);

        cueSheetItemUpdateMapper.updateFromDto(cueSheetItemUpdateDTO, cueSheetItem);

        cueSheetItemRepository.save(cueSheetItem);
    }

    public void delete(Long cueId, Long cueItemId){

        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItem);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        UserSimpleDTO userSimpleDTO = UserSimpleDTO.builder().userId(userId).build();
        cueSheetItemDTO.setDelr(userSimpleDTO);
        cueSheetItemDTO.setDelYn("Y");
        cueSheetItemDTO.setDelDtm(new Date());

        cueSheetItemMapper.updateFromDto(cueSheetItemDTO, cueSheetItem);

        cueSheetItemRepository.save(cueSheetItem);
    }

    public void ordUpdate( Long cueId, Long cueItemId, int cueItemOrd){


        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        CueSheetItemDTO updateCueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItem);
        updateCueSheetItemDTO.setCueItemOrd(cueItemOrd);

        cueSheetItemMapper.updateFromDto(updateCueSheetItemDTO, cueSheetItem);
        cueSheetItemRepository.save(cueSheetItem); //큐시트아이템 등록

        //큐시트 아이템 순번 재등록
        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemList(cueId);

        for (int i =  cueSheetItemList.size() - 1; i >= 0 ; i--){
            if (!cueItemId.equals(cueSheetItemList.get(i).getCueItemId())){
                continue;
            }
            cueSheetItemList.remove(i);//신규 등록된 큐시트 아이템 리스트에서 삭제
        }



        cueSheetItemList.add(cueItemOrd, cueSheetItem); //신규등록하려는 큐시트 아이템 원하는 순번에 리스트 추가

        //조회된 큐시트 아이템 Ord 업데이트
        int index = 0;
        for (CueSheetItem cueSheetItems : cueSheetItemList){

            CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItems);
            cueSheetItemDTO.setCueItemOrd(index);
            CueSheetItem setCueSheetItem = cueSheetItemMapper.toEntity(cueSheetItemDTO);
            cueSheetItemRepository.save(setCueSheetItem);
            index++;
        }

    }

    public CueSheetItem cueItemFindOrFail(Long cueItemId){

        Optional<CueSheetItem> cueSheetItem = cueSheetItemRepository.findByCueItem(cueItemId);

        if (cueSheetItem.isPresent() == false){
            throw new ResourceNotFoundException("CueSheetItem not found. CueSheet Item Id : " + cueItemId);
        }

        return cueSheetItem.get();

    }

    public BooleanBuilder getSearch(Long artclId, Long cueId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        //dsl q쿼리 생성
        QCueSheetItem qCueSheetItem = QCueSheetItem.cueSheetItem;

        booleanBuilder.and(qCueSheetItem.delYn.eq("N"));
        //쿼리 where 조건 추가.
        if (StringUtils.isEmpty(artclId) == false){
            booleanBuilder.and(qCueSheetItem.article.artclId.eq(artclId));
        }
        //쿼리 where 조건 추가.
        if (StringUtils.isEmpty(cueId) == false){
            booleanBuilder.and(qCueSheetItem.cueId.eq(cueId));
        }


        return booleanBuilder;
    }

    public BooleanBuilder getSearchOrd(Long cueId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        //dsl q쿼리 생성
        QCueSheetItem qCueSheetItem = QCueSheetItem.cueSheetItem;

        booleanBuilder.and(qCueSheetItem.delYn.eq("N"));
        //쿼리 where 조건 추가.
        if (StringUtils.isEmpty(cueId) == false){
            booleanBuilder.and(qCueSheetItem.cueId.eq(cueId));
        }

        return booleanBuilder;
    }

    public void createCueItem(Long cueId, Long artclId, int cueItemOrd){

        //기사 복사[복사된 기사 Id get]
        Article copyArtcl = copyArticle(artclId);

        //토큰 사용자 Id(현재 로그인된 사용자 ID)
        String userId = userAuthService.authUser.getUserId();
        //입력자 등록[ 현재 Entity에 입력자가 타입이 유저이기 때문에 빌드시켜서 set]
        User user = User.builder().userId(userId).build();

        //큐시트아이템create 빌드
        CueSheetItem cueSheetItem = CueSheetItem.builder()
                .cueId(cueId)
                .cueItemOrd(cueItemOrd)
                .inputr(user)
                .article(copyArtcl)
                .build();


        //빌드된 큐시트아이템 등록
        cueSheetItemRepository.save(cueSheetItem);

        Long cueItemId = cueSheetItem.getCueItemId();

        //큐시트아이템 순서 update
        ordUpdateDrop(cueSheetItem, cueId, cueItemOrd);


    }

    public void createCueItemList(List<CueSheetItemCreateListDTO> cueSheetItemCreateListDTO, Long cueId){

        for (CueSheetItemCreateListDTO createListDTO : cueSheetItemCreateListDTO){
            Long artclId = createListDTO.getArtclId();
            int cueItemOrd = createListDTO.getCueItemOrd();

            //기사 복사[복사된 기사 Id get]
            Article copyArtcl = copyArticle(artclId);

            //토큰 사용자 Id(현재 로그인된 사용자 ID)
            String userId = userAuthService.authUser.getUserId();
            //입력자 등록[ 현재 Entity에 입력자가 타입이 유저이기 때문에 빌드시켜서 set]
            User user = User.builder().userId(userId).build();

            //큐시트아이템create 빌드
            CueSheetItem cueSheetItem = CueSheetItem.builder()
                    .cueId(cueId)
                    .cueItemOrd(cueItemOrd)
                    .inputr(user)
                    .article(copyArtcl)
                    .build();


            //빌드된 큐시트아이템 등록
            cueSheetItemRepository.save(cueSheetItem);

            Long cueItemId = cueSheetItem.getCueItemId();

            //큐시트아이템 순서 update
            ordUpdateDrop(cueSheetItem, cueId, cueItemOrd);
        }

    }
    
    public void ordUpdateDrop(CueSheetItem cueSheetItem, Long cueId, int cueItemOrd){

        // 쿼리문 만들기
        BooleanBuilder booleanBuilder = getSearchOrd(cueId);

        //큐시트 아이템 순번 재등록
        List<CueSheetItem> cueSheetItemList = (List<CueSheetItem>) cueSheetItemRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "cueItemOrd"));
        if(cueSheetItemList== null)
            return;

        //ClearCueSheetList(cueSheetItemList);

        Long cueSheetItemId = cueSheetItem.getCueItemId();
        for (int i =  cueSheetItemList.size() - 1; i >= 0 ; i--){ //out of index때문에 역순으로 검사.
            if (!cueSheetItemId.equals(cueSheetItemList.get(i).getCueItemId())){
                continue;
            }
            cueSheetItemList.remove(i);//신규 등록된 큐시트 아이템 리스트에서 삭제
        }

        cueSheetItemList.add(cueItemOrd, cueSheetItem); //신규등록하려는 큐시트 아이템 원하는 순번에 리스트 추가

        //조회된 큐시트 아이템 Ord 업데이트

        int index = 1;
        for (CueSheetItem cueSheetItems : cueSheetItemList){
            CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItems);
            cueSheetItemDTO.setCueItemOrd(index);
            CueSheetItem setCueSheetItem = cueSheetItemMapper.toEntity(cueSheetItemDTO);
            cueSheetItemRepository.save(setCueSheetItem);

            index++;
        }
    }

    public Article copyArticle(Long artclId){

        /*********************/
        /* 기사를 저장하는 부분 */

        //기사 아이디로 기사 검색
        Optional<Article> getArticle = articleRepository.findArticle(artclId);
        //조회된 기사 빈값 검사.

        if (getArticle.isPresent() == false){
            throw new ResourceNotFoundException(String.format("기사아이디에 해당하는 기사가 없습니다. {%ld}", artclId));
        }
        //조회된 기사 get
        Article article = getArticle.get();

        //기사 시퀀스[오리지널 기사 0 = 그밑으로 복사된 기사 + 1 증가]
        int artclOrd = article.getArtclOrd() + 1;

        Article articleEntity = getArticleEntity(article, artclOrd);
        articleRepository.save(articleEntity);

        /*********************/
        /* 자막을 저장하는 부분 */

        //기사 아이디로 기사자막 조회
        List<ArticleCap> articleCap = articleCapRepository.findArticleCap(artclId);
        for( int i  = 0 ; i < articleCap.size() ; i++ )
        {
            ArticleCap getArticleCap =  articleCap.get(i);

            ArticleCap articleCapEntity = getArticleCapEntity(getArticleCap, articleEntity);

            articleCapRepository.save(articleCapEntity);
        }

        /*********************/
        /* 미디어 정보 저장 하는 부분 */

        //기사 아이디로 기사 미디어 조회
        List<ArticleMedia> articleMediaList = articleMediaRepository.findArticleMediaList(artclId);

        //조회된 기사 미디어가 있으면 복사된 기사 아이디로 기사미디어 신규 저장.
        if (ObjectUtils.isEmpty(articleMediaList) ==false){

            for (ArticleMedia articleMedia : articleMediaList){

                ArticleMedia articleMediaEntity = getArticleMediaEntity(articleMedia, articleEntity);


                articleMediaRepository.save(articleMediaEntity);
            }
        }


        return articleEntity;

    }

    // 기사 저장하기 위한 엔터티 만들기 2021-10-27
    private Article getArticleEntity(Article article, int artclOrd) {
        return Article.builder()
                .chDivCd(article.getChDivCd())
                .artclKindCd(article.getArtclKindCd())
                .artclFrmCd(article.getArtclFrmCd())
                .artclDivCd(article.getArtclDivCd())
                .artclFldCd(article.getArtclFldCd())
                .apprvDivCd(article.getApprvDivCd())
                .prdDivCd(article.getPrdDivCd())
                .artclTypCd(article.getArtclTypCd())
                .artclTypDtlCd(article.getArtclTypDtlCd())
                .artclCateCd(article.getArtclCateCd())
                .artclTitl(article.getArtclTitl())
                .artclTitlEn(article.getArtclTitlEn())
                .artclCtt(article.getArtclCtt())
                .ancMentCtt(article.getAncMentCtt())
                .rptrNm(article.getRptrNm())
                .userGrpId(article.getUserGrpId())
                .artclReqdSecDivYn(article.getArtclReqdSecDivYn())
                .artclReqdSec(article.getArtclReqdSec())
                .lckYn(article.getLckYn())
                .lckDtm(article.getLckDtm())
                .apprvDtm(article.getApprvDtm())
                .artclOrd(artclOrd)//기사 시퀀스 +1
                .brdcCnt(article.getBrdcCnt())
                .orgArtclId(article.getOrgArtclId())//원본기사 아이디set
                .urgYn(article.getUrgYn())
                .frnotiYn(article.getFrnotiYn())
                .embgYn(article.getEmbgYn())
                .embgDtm(article.getEmbgDtm())
                .inputrNm(article.getInputrNm())
                .delYn(article.getDelYn())
                .notiYn(article.getNotiYn())
                .regAppTyp(article.getRegAppTyp())
                .brdcPgmId(article.getBrdcPgmId())
                .brdcSchdDtm(article.getBrdcSchdDtm())
                .inputr(article.getInputr())
                .updtr(article.getUpdtr())
                .delr(article.getDelr())
                .apprvr(article.getApprvr())
                .lckr(article.getLckr())
                .rptr(article.getRptr())
                .artclCttTime(article.getArtclCttTime())
                .ancMentCttTime(article.getAncMentCttTime())
                .artclExtTime(article.getArtclExtTime())
                .videoTime(article.getVideoTime())
                .deptCd(article.getDeptCd())
                .deviceCd(article.getDeviceCd())
                .parentArtlcId(article.getArtclId())//복사한 기사 아이디 set
                .issue(article.getIssue())
                .build();
    }

    public ArticleCap getArticleCapEntity(ArticleCap getArticleCap, Article articleEntity){
        return ArticleCap.builder()
                .capDivCd(getArticleCap.getCapDivCd())
                .lnNo(getArticleCap.getLnNo())
                .capCtt(getArticleCap.getCapCtt())
                .capRmk(getArticleCap.getCapRmk())
                .article(articleEntity)
                .capTemplate(getArticleCap.getCapTemplate())
                .symbol(getArticleCap.getSymbol())
                .build();
    }

    public ArticleMedia getArticleMediaEntity(ArticleMedia articleMedia, Article articleEntity){

        return ArticleMedia.builder()
                .mediaTypCd(articleMedia.getMediaTypCd())
                .mediaOrd(articleMedia.getMediaOrd())
                .contId(articleMedia.getContId())
                .trnsfFileNm(articleMedia.getTrnsfFileNm())
                .mediaDurtn(articleMedia.getMediaDurtn())
                .mediaMtchDtm(articleMedia.getMediaMtchDtm())
                .trnsfStCd(articleMedia.getTrnsfStCd())
                .assnStCd(articleMedia.getAssnStCd())
                .videoEdtrNm(articleMedia.getVideoEdtrNm())
                .delYn(articleMedia.getDelYn())
                .delDtm(articleMedia.getDelDtm())
                .inputr(articleMedia.getInputr())
                .updtr(articleMedia.getUpdtr())
                .delr(articleMedia.getDelr())
                .videoEdtrId(articleMedia.getVideoEdtrId())
                .article(articleEntity)
                .build();
    }




}
