package com.gemiso.zodiac.app.article;

import com.gemiso.zodiac.app.appAuth.AppAuth;
import com.gemiso.zodiac.app.article.dto.ArticleCreateDTO;
import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.article.dto.ArticleLockDTO;
import com.gemiso.zodiac.app.article.dto.ArticleUpdateDTO;
import com.gemiso.zodiac.app.article.mapper.ArticleCreateMapper;
import com.gemiso.zodiac.app.article.mapper.ArticleLockMapper;
import com.gemiso.zodiac.app.article.mapper.ArticleMapper;
import com.gemiso.zodiac.app.article.mapper.ArticleUpdateMapper;
import com.gemiso.zodiac.app.articleCap.ArticleCap;
import com.gemiso.zodiac.app.articleCap.ArticleCapRepository;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapDTO;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapSimpleDTO;
import com.gemiso.zodiac.app.articleCap.mapper.ArticleCapMapper;
import com.gemiso.zodiac.app.articleCap.mapper.ArticleCapSimpleMapper;
import com.gemiso.zodiac.app.articleHist.ArticleHist;
import com.gemiso.zodiac.app.articleHist.ArticleHistRepository;
import com.gemiso.zodiac.app.articleHist.mapper.ArticleHistSimpleMapper;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaSimpleDTO;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaSimpleMapper;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderSimpleDTO;
import com.gemiso.zodiac.app.articleOrder.mapper.ArticleOrderSimpleMapper;
import com.gemiso.zodiac.app.code.Code;
import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemRepository;
import com.gemiso.zodiac.app.issue.IssueRepositoy;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import com.gemiso.zodiac.app.user.QUser;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.UserGroupUser;
import com.gemiso.zodiac.app.user.UserGroupUserRepository;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import com.gemiso.zodiac.app.userGroup.UserGroup;
import com.gemiso.zodiac.app.userGroup.UserGroupAuth;
import com.gemiso.zodiac.app.userGroup.UserGroupAuthRepository;
import com.gemiso.zodiac.core.helper.PageHelper;
import com.gemiso.zodiac.core.page.PageResultDTO;
import com.gemiso.zodiac.core.service.ProcessArticleFix;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleCapRepository articleCapRepository;
    private final ArticleHistRepository articleHistRepository;
    private final CueSheetItemRepository cueSheetItemRepository;
    private final UserGroupUserRepository userGroupUserRepository;
    private final UserGroupAuthRepository userGroupAuthRepository;
    private final IssueRepositoy issueRepositoy;

    private final ArticleMapper articleMapper;
    private final ArticleCreateMapper articleCreateMapper;
    private final ArticleCapMapper articleCapMapper;
    private final ArticleCapSimpleMapper articleCapSimpleMapper;
    private final ArticleHistSimpleMapper articleHistSimpleMapper;
    private final ArticleMediaSimpleMapper articleMediaSimpleMapper;
    private final ArticleOrderSimpleMapper articleOrderSimpleMapper;
    private final ArticleUpdateMapper articleUpdateMapper;
    private final ArticleLockMapper articleLockMapper;

    private final UserAuthService userAuthService;

    @Value("${files.url-key}")
    private String fileUrl;


    public PageResultDTO<ArticleDTO, Article> findAll(Date sdate, Date edate, Date rcvDt, String rptrId, Long brdcPgmId,
                                    String artclDivCd, String artclTypCd, String searchDivCd, String searchWord,
                                                      Integer page, Integer limit, Long issuId) {

        /*page = Optional.ofNullable(page).orElse(0);
        limit = Optional.ofNullable(limit).orElse(50);

        Pageable pageable = PageRequest.of(page, limit, Sort.by("artclId","inputDtm").descending());

        BooleanBuilder booleanBuilder = getSearch(sdate, edate, rcvDt, rptrId, brdcPgmId, artclDivCd, artclTypCd, searchDivCd, searchWord);

        List<Article> confirmList = (List<Article>) articleRepository.findAll(booleanBuilder,pageable);

        List<ArticleDTO> articleDTOList = articleMapper.toDtoList(confirmList);

        return articleDTOList;*/

        PageHelper pageHelper = new PageHelper(page, limit);
        /*page = Optional.ofNullable(page).orElse(0);
        limit = Optional.ofNullable(limit).orElse(50);*/

        //Pageable pageable = PageRequest.of(page, limit, Sort.by("artclId","inputDtm").descending());
        Pageable pageable = pageHelper.getArticlePageInfo();

        //검색조건생성 [where생성]
        BooleanBuilder booleanBuilder = getSearch(sdate, edate, rcvDt, rptrId, brdcPgmId, artclDivCd, artclTypCd,
                searchDivCd, searchWord, issuId);

        //전체조회[page type]
        Page<Article> result = articleRepository.findAll(booleanBuilder,pageable);

        Function<Article, ArticleDTO> fn = (entity -> articleMapper.toDto(entity));

        return new PageResultDTO<ArticleDTO, Article>(result, fn);
    }

    // 큐시트에서 기사 목록 조회
    public List<ArticleDTO> findCue(Date sdate, Date edate, String searchWord, Long cueId) {

        //현재 큐시트에 포함된 큐시트아이템을 조회
        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemList(cueId);

        BooleanBuilder booleanBuilder = getSearchCue(sdate, edate, searchWord);

        List<Article> confirmList = (List<Article>) articleRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "inputDtm"));

        //큐시트 아이템으로 포함된 기사 조회정보에서 삭제.
        if (ObjectUtils.isEmpty(cueSheetItemList) == false) {
            for (CueSheetItem cueSheetItem : cueSheetItemList) {
                if (ObjectUtils.isEmpty(cueSheetItem.getArticle()) == false) { //기사 아이디가 있으면 조회된 기사리트와 검사하여 포함된 기사 삭제
                    Long cueArticleId = cueSheetItem.getArticle().getArtclId(); //큐시트 아이템으로 포함되어 있는 기사아이디 get
                    for (int i = confirmList.size() - 1; i >= 0; i--) {
                        Long artclId = confirmList.get(i).getArtclId();
                        if (cueArticleId.equals(artclId)) {
                            confirmList.remove(i);
                        }
                    }
                }
            }
        }
        List<ArticleDTO> articleDTOList = articleMapper.toDtoList(confirmList);

        return articleDTOList;
    }

    public ArticleDTO find(Long artclId) {

        Article article = articleFindOrFail(artclId);

        ArticleDTO articleDTO = articleMapper.toDto(article);

        //기사정보를 불러와 맵퍼스트럭트 사용시 스텍오버플로우에러[기사에 포함된 리스트에 기사정보포함되어이써 문제 발생] 때문에 따로 DTO변환.
        //List<ArticleHistSimpleDTO> articleHistDTOList = articleHistSimpleMapper.toDtoList(article.getArticleHist());//기사이력정보 DTO변환
        List<ArticleCapSimpleDTO> articleCapDTOList = articleCapSimpleMapper.toDtoList(article.getArticleCap()); //기사자막정보 DTO변환
        List<ArticleMediaSimpleDTO> articleMediaSimpleDTOList = articleMediaSimpleMapper.toDtoList(article.getArticleMedia()); //기사미디어정보 DTO변환
        List<ArticleOrderSimpleDTO> articleOrderSimpleDTOList = articleOrderSimpleMapper.toDtoList(article.getArticleOrder()); //기사의뢰정보 DTO변환


        //방송아이콘 이미지 Url 추가.
        List<ArticleCapSimpleDTO> setArticleCapDTOList = new ArrayList<>();
        if (ObjectUtils.isEmpty(articleCapDTOList) == false) {
            for (ArticleCapSimpleDTO articleCapSimpleDTO : articleCapDTOList) {

                SymbolDTO symbolDTO = new SymbolDTO();

                symbolDTO = articleCapSimpleDTO.getSymbol();

                if (ObjectUtils.isEmpty(symbolDTO) == false) {
                    String fileLoc = articleCapSimpleDTO.getSymbol().getAttachFile().getFileLoc();
                    String url = fileUrl + fileLoc;

                    symbolDTO.setUrl(url);

                    articleCapSimpleDTO.setSymbol(symbolDTO);
                    setArticleCapDTOList.add(articleCapSimpleDTO);
                }
            }
        }

        //기사이력, 자막, 미디어, 의뢰 정보 set
        //articleDTO.setArticleHistDTO(articleHistDTOList);
        articleDTO.setArticleCapDTO(setArticleCapDTOList);
        articleDTO.setArticleMediaDTO(articleMediaSimpleDTOList);
        articleDTO.setArticleOrderDTO(articleOrderSimpleDTOList);


        return articleDTO;

    }


    public Long create(ArticleCreateDTO articleCreateDTO) { // 기사등록[기사 이력, 자막]

        String userId = userAuthService.authUser.getUserId();
        articleCreateDTO.setInputrId(userId); //등록자 아이디 추가.
        articleCreateDTO.setArtclOrd(0);

        Article article = articleCreateMapper.toEntity(articleCreateDTO);
        articleRepository.save(article);

        ArticleDTO articleDTO = articleMapper.toDto(article);


        //기사 자막 create

        List<ArticleCapSimpleDTO> articleCapDTOS = articleCreateDTO.getArticleCap();

        if (!ObjectUtils.isEmpty(articleCapDTOS)) {
            for (ArticleCapSimpleDTO articleCapDTO : articleCapDTOS) {

                Article articleSimple = Article.builder().artclId(articleDTO.getArtclId()).build();

                ArticleCap articleCap = articleCapSimpleMapper.toEntity(articleCapDTO);
                articleCap.setArticle(articleSimple);
                articleCapRepository.save(articleCap);
            }
        }
        //기사 이력 create
        createArticleHist(article);

        return article.getArtclId();
    }

    public void update(ArticleUpdateDTO articleUpdateDTO, Long artclId) {

        Article article = articleFindOrFail(artclId);

        //기사 자막 Update
        articleCapUpdate(article, articleUpdateDTO);

        String userId = userAuthService.authUser.getUserId();
        articleUpdateDTO.setUpdtrId(userId);

        articleUpdateMapper.updateFromDto(articleUpdateDTO, article);

        articleRepository.save(article);

        //기사이력 등록.
        updateArticleHist(article);

    }

    public void delete(Long artclId) {

        Article article = articleFindOrFail(artclId);

        ArticleDTO articleDTO = articleMapper.toDto(article);

        //삭제정보 등록
        articleDTO.setDelDtm(new Date());
        String userId = userAuthService.authUser.getUserId();
        articleDTO.setDelrId(userId);
        articleDTO.setDelYn("Y");

        articleMapper.updateFromDto(articleDTO, article);

        articleRepository.save(article);

    }

    private void articleCapUpdate(Article article, ArticleUpdateDTO articleUpdateDTO) {


        List<ArticleCap> articleCapList = article.getArticleCap();

        if (ObjectUtils.isEmpty(articleCapList) == false) { //기존에 등록되어 있던 기사자막 삭제
            for (ArticleCap articleCap : articleCapList) {

                Long artclCapId = articleCap.getArtclCapId();

                articleCapRepository.deleteById(artclCapId);

            }
        }

        List<ArticleCapSimpleDTO> capSimpleDTOList = articleUpdateDTO.getArticleCap(); //update로 들어온 기사 등록
        if (ObjectUtils.isEmpty(capSimpleDTOList) == false) {

            for (ArticleCapSimpleDTO articleCapSimpleDTO : capSimpleDTOList) {

                Article articleSimple = Article.builder().artclId(article.getArtclId()).build();

                ArticleCap articleCap = articleCapSimpleMapper.toEntity(articleCapSimpleDTO);
                articleCap.setArticle(articleSimple);
                articleCapRepository.save(articleCap);
            }

        }

    }

    public void articleLock(Long artclId, ArticleLockDTO articleLockDTO) {

        Article article = articleFindOrFail(artclId);

        if (articleLockDTO.getLckYn().equals("Y")) {
            articleLockDTO.setLckDtm(new Date());
            // 토큰 인증된 사용자 아이디를 입력자로 등록
            String userId = userAuthService.authUser.getUserId();
            articleLockDTO.setLckr(userId);
        } else {
            article.setLckDtm(null);
            article.setLckrId(null);
        }

        articleLockMapper.updateFromDto(articleLockDTO, article);

        articleRepository.save(article);


    }

    public void articleUnlock(Long artclId, ArticleLockDTO articleLockDTO) {

        Article article = articleFindOrFail(artclId);

        if (articleLockDTO.getLckYn().equals("N")) {
            article.setLckDtm(null);
            article.setLckrId(null);
        }

        articleLockMapper.updateFromDto(articleLockDTO, article);

        articleRepository.save(article);

    }

    public Article articleFindOrFail(Long artclId) {

        Optional<Article> article = articleRepository.findArticle(artclId);

        if (!article.isPresent()) {
            throw new ResourceNotFoundException("기사 아이디가 없습니다. article Id  : " + artclId);
        }

        return article.get();

    }

    public BooleanBuilder getSearch(Date sdate, Date edate, Date rcvDt, String rptrId, Long brdcPgmId,
                                    String artclDivCd, String artclTypCd, String searchDivCd, String searchWord, Long issuId) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticle qArticle = QArticle.article;
        QUser qUser = QUser.user;

        //삭제가 되지 않은 기사 조회
        booleanBuilder.and(qArticle.delYn.eq("N"));

        //등록날짜 기준으로 조회
        if (!StringUtils.isEmpty(sdate) && !StringUtils.isEmpty(edate)) {
            booleanBuilder.and(qArticle.inputDtm.between(sdate, edate)
                    .or(qArticle.embgDtm.between(sdate, edate)
                            .or(qArticle.brdcSchdDtm.between(sdate, edate))));
        }
        //수신 일자 기준으로 조회
        if (!StringUtils.isEmpty(rcvDt)) {

            //rcvDt(수신일자)검색을 위해 +1 days
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rcvDt);
            calendar.add(Calendar.DATE, 1);
            Date rcvDtTomerrow = calendar.getTime();

            booleanBuilder.and(qArticle.inputDtm.between(rcvDt, rcvDtTomerrow));
        }
        //기자 아이디로 조회
        if (!StringUtils.isEmpty(rptrId)) {
            booleanBuilder.and(qArticle.rptrId.eq(rptrId));
        }
        //방송 프로그램 아이디로 조회
        if (!StringUtils.isEmpty(brdcPgmId)) {
            booleanBuilder.and(qArticle.brdcPgmId.eq(brdcPgmId));
        }
        //기사 구분 코드로 조회
        /*if (!StringUtils.isEmpty(artclDivCd)) {
            booleanBuilder.and(qArticle.artclDivCd.cd.eq(artclDivCd));
        }*/
        //기사 타입 코드로 조회
        if (!StringUtils.isEmpty(artclTypCd)) {
            booleanBuilder.and(qArticle.artclTypCd.eq(artclTypCd));
        }
        //검색어로 조회
        if (!StringUtils.isEmpty(searchWord)) {
            //검색구분코드 01 일때 기사 제목으로 검색
            if (searchDivCd.equals("01")) {
                booleanBuilder.and(qArticle.artclTitl.contains(searchWord));
            }
            //검색구분코드 02 일때 기자이름으로 검색
            if (searchDivCd.equals("02")) {
                booleanBuilder.and(qArticle.rptrId.eq(String.valueOf(qUser.userNm.contains(searchWord))));
            }

        }
        //기사 타입 코드로 조회
        if (!StringUtils.isEmpty(issuId)) {
            booleanBuilder.and(qArticle.issue.issuId.eq(issuId));
        }


        return booleanBuilder;
    }

    public BooleanBuilder getSearchCue(Date sdate, Date edate, String searchWord) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticle qArticle = QArticle.article;

        booleanBuilder.and(qArticle.delYn.eq("N"));

        //등록날짜 기준으로 조회
        if (!StringUtils.isEmpty(sdate) && !StringUtils.isEmpty(edate)) {
            booleanBuilder.and(qArticle.inputDtm.between(sdate, edate));
        }

        if (!StringUtils.isEmpty(searchWord)) {
            booleanBuilder.and(qArticle.artclTitl.contains(searchWord));
        }

        return booleanBuilder;
    }

    public void createArticleHist(Article article) {

        ArticleHist articleHist = ArticleHist.builder()
                .article(article)
                .chDivCd(article.getChDivCd())
                .artclTitl(article.getArtclTitl())
                .artclTitlEn(article.getArtclTitlEn())
                .artclCtt(article.getArtclCtt())
                .ancMentCtt(article.getAncMentCtt())
                .artclOrd(article.getArtclOrd())
                .orgArtclId(article.getOrgArtclId())
                .inputDtm(new Date())
                .ver(0) //수정! 버전 설정?
                .build();

        articleHistRepository.save(articleHist);
    }


    public void updateArticleHist(Article article) {

        ArticleHist articleHist = ArticleHist.builder()
                .article(article)
                .chDivCd(article.getChDivCd())
                .artclTitl(article.getArtclTitl())
                .artclTitlEn(article.getArtclTitlEn())
                .artclCtt(article.getArtclCtt())
                .ancMentCtt(article.getAncMentCtt())
                .artclOrd(article.getArtclOrd())
                .orgArtclId(article.getOrgArtclId())
                .inputDtm(new Date())
                .ver(0) //수정! 버전 설정?
                .build();

        articleHistRepository.save(articleHist);

    }


    public void vaildFixStaus(Long artclId, String apprvDivCd) {
        boolean ret = false;

        long start = System.currentTimeMillis();
        // 기사 ID를 이용하여 기자 정보를 불러온다.

        //기사 조회.
        Optional<Article> articleEntity = articleRepository.findArticle(artclId);
        if (articleEntity.isPresent() == false) {
            throw new ResourceNotFoundException(String.format("기사를 찾을수 없습니다. 기사아이디 : {%ld}", artclId));
        }

        Article article = articleEntity.get();

        // 사용자 정보
        String userId = userAuthService.authUser.getUserId();

        //현재접속자 그룹정보를 그룹이넘 리스트로 불러온다.
        List<String> fixAuthList = getAppAuth(userId);

        //ProcessArticleFix에 fix구분할 date값 set
        ProcessArticleFix paf = new ProcessArticleFix();
        paf.setArticle(article);
        paf.setApproveCode(apprvDivCd);

        //유저 Id
        if (paf.getFixStatus(userId, fixAuthList) == false) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.print(timeElapsed);

        ArticleDTO articleDTO = articleMapper.toDto(article);
        articleDTO.setApprvDivCd(apprvDivCd); //픽스 구분코드 변경.
        articleDTO.setApprvrId(userId);//픽스 승인자 변경.

        articleMapper.updateFromDto(articleDTO, article);
        articleRepository.save(article);

    }

    public void fixUpdate(Article article, String apprvDivCd) {

    }


 /*   //그룹으로 제어하지말고 권한으로 제어.
    public List<Long> getAppAuth(String userId) {

        // 사용자에 대한 그룹 정보
        List<UserGroupUser> userGroupUserList = userGroupUserRepository.findByUserId(userId);

        List<Long> appAuthList = new ArrayList<Long>();

        //조회된 유저그룹 리스트에서 유저그룹 Id를 하나씩 가져온다.
        for (UserGroupUser ugu : userGroupUserList) {
            UserGroup ug = ugu.getUserGroup(); //조회된 유저,그룹 맵핑테이블에서 유저그룹정보만 get
            Long id = ug.getUserGrpId(); //유저그룹 ID get

            if (appAuthList.contains(id) == false)
                appAuthList.add(id);
        }
        return appAuthList;
    }*/

    public List<String> getAppAuth(String userId) {

        // 사용자에 대한 그룹 정보
        List<UserGroupUser> userGroupUserList = userGroupUserRepository.findByUserId(userId);

        List<String> appAuthList = new ArrayList<>(); //리턴할 권한 List

        for (UserGroupUser userGroupUser : userGroupUserList) { //등록된 사용자 그룹에 포함한 권한을 모두 불러온다.

            Long groupId = userGroupUser.getUserGroup().getUserGrpId();

            List<UserGroupAuth> findUserGroupAuthList = userGroupAuthRepository.findByUserGrpId(groupId);

            for (UserGroupAuth userGroupAuth : findUserGroupAuthList) {

                String appAuthCD = userGroupAuth.getAppAuth().getAppAuthCd(); //코드네임 get
                if (appAuthList.contains(appAuthCD) == false) {
                    appAuthList.add(appAuthCD);
                }
            }
        }
        //권한 리스트 리턴
        return appAuthList;

    }

    // artcl_div_cd[기사구분코드가] 이슈로 들어왔을 경우 해당이슈로  작성된 기사들 출력.
/*    public PageResultDTO<ArticleDTO, Article> findAllIssue(Long issuId){

        PageHelper pageHelper = new PageHelper(null, null);
        *//*page = Optional.ofNullable(page).orElse(0);
        limit = Optional.ofNullable(limit).orElse(50);*//*

        //Pageable pageable = PageRequest.of(page, limit, Sort.by("artclId","inputDtm").descending());
        Pageable pageable = pageHelper.getArticlePageInfo();

        BooleanBuilder booleanBuilder = getSearch(null, null, null, null, null,
                null, null, null, null, issuId);

        Page<Article> result = articleRepository.findAll(booleanBuilder,pageable);

        Function<Article, ArticleDTO> fn = (entity -> articleMapper.toDto(entity));

        return new PageResultDTO<ArticleDTO, Article>(result, fn);

    }*/

}
