package com.gemiso.zodiac.app.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class ElasticSearchArticleRepositoryImpl implements ElasticSearchArticleCustom {

    private final ElasticsearchOperations elasticsearchOperations;
    private final RestClientBuilder restClientBuilder;
    private final RestHighLevelClient restHighLevelClient;



    @Autowired
    public ElasticSearchArticleRepositoryImpl(ElasticsearchOperations elasticsearchOperations,
                                              RestClientBuilder restClientBuilder,
                                              @Qualifier("elasticsearchClient") RestHighLevelClient restHighLevelClient) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.restClientBuilder = restClientBuilder;
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public Page<ElasticSearchArticle> findByElasticSearchArticleList(Date sdate,
                                                                     Date edate,
                                                                     Date rcvDt,
                                                                     String rptrId,
                                                                     String inputrId,
                                                                     String brdcPgmId,
                                                                     String artclDivCd,
                                                                     String artclTypCd,
                                                                     String searchDivCd,
                                                                     String searchWord,
                                                                     List<String> apprvDivCdList,
                                                                     Integer deptCd,
                                                                     String artclCateCd,
                                                                     String artclTypDtlCd,
                                                                     String delYn,
                                                                     Long artclId,
                                                                     String copyYn,
                                                                     Long orgArtclId,
                                                                     Pageable pageable) {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder query = QueryBuilders.boolQuery();

        //등록날짜 기준으로 조회
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {

            DateChangeHelper dateChangeHelper = new DateChangeHelper();
            String getSdate = dateChangeHelper.dateToStringNormal(sdate);
            String getEdate = dateChangeHelper.dateToStringNormal(edate);

            query.must(QueryBuilders.rangeQuery("inputDtm").from(getSdate).to(getEdate));
        }
        //수신 일자 기준으로 조회
        if (ObjectUtils.isEmpty(rcvDt) == false) {

            //rcvDt(수신일자)검색을 위해 +1 days
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rcvDt);
            calendar.add(Calendar.DATE, 1);
            Date rcvDtTomerrow = calendar.getTime();
            query.must(QueryBuilders.rangeQuery("inputDtm").from(rcvDt).to(rcvDtTomerrow));
        }
        //기자 아이디로 조회
        if (rptrId != null && rptrId.trim().isEmpty() == false) {
            query.must(QueryBuilders.matchQuery("rptrId", rptrId));
        }
        //등록자 아이디로 조회
        if (inputrId != null && inputrId.trim().isEmpty() == false) {
            query.must(QueryBuilders.matchQuery("inputrId", inputrId));
        }
        //방송 프로그램 아이디로 조회
        if (brdcPgmId != null && brdcPgmId.trim().isEmpty() == false) {
            query.must(QueryBuilders.matchQuery("brdcPgmId", brdcPgmId));
        }
        //기사 구분 코드로 조회
        if (artclDivCd != null && artclDivCd.trim().isEmpty() == false) {
            query.must(QueryBuilders.matchQuery("artclDivCd", artclDivCd));
        }
        //기사 타입 코드로 조회
        if (artclTypCd != null && artclTypCd.trim().isEmpty() == false) {
            query.must(QueryBuilders.matchQuery("artclTypCd", artclTypCd));
        }
        //검색조건 = 삭제 여부
        if (delYn != null && delYn.trim().isEmpty() == false) {
            query.must(QueryBuilders.matchQuery("delYn", delYn));
        } else {
            query.must(QueryBuilders.matchQuery("delYn", "N")); //삭제여부값 안들어 올시 디폴트 'N'
        }
        //검색어로 조회
        if (searchWord != null && searchWord.trim().isEmpty() == false) {
            //검색구분코드 01 일때 기사 제목으로 검색
            if ("01".equals(searchDivCd)) {
                query.must(QueryBuilders.multiMatchQuery(searchWord, "artclTitl", "artclTitlEn"));
            }
            //검색구분코드 02 일때 기자이름으로 검색
            else if ("02".equals(searchDivCd)) {
                //query.must(QueryBuilders.matchQuery("rptrId", String.valueOf(qUser.userNm.contains(searchWord))));
            }
            //검색구분코드 안들어왔을 경우
            else if (searchDivCd == null || searchDivCd.trim().isEmpty()) {
                query.must(QueryBuilders.multiMatchQuery(searchWord, "artclTitl", "artclTitlEn"));
            }

        }

        //픽스구분코드[여러개의 or조건으로 가능]
        if (CollectionUtils.isEmpty(apprvDivCdList) == false) {
            query.must(QueryBuilders.matchQuery("apprvDivCd", apprvDivCdList).operator(Operator.OR));
        }

        //부서코드
        if (deptCd != null && deptCd != 0) {
            query.must(QueryBuilders.matchQuery("deptCd", deptCd));
        }
        //기사카테고리코드
        if (artclCateCd != null && artclCateCd.trim().isEmpty() == false) {
            query.must(QueryBuilders.matchQuery("artclCateCd", artclCateCd));
        }
        //기사유형상세코드
        if (artclTypDtlCd != null && artclTypDtlCd.trim().isEmpty() == false) {
            query.must(QueryBuilders.matchQuery("artclTypDtlCd", artclTypDtlCd));
        }
        //기사 아이디
        if (ObjectUtils.isEmpty(artclId) == false) {
            query.must(QueryBuilders.matchQuery("artclId", artclId));
        }
        //원본기사 아이디
        if (ObjectUtils.isEmpty(orgArtclId) == false){
            query.must(QueryBuilders.matchQuery("orgArtclId", orgArtclId));
        }
        //원본 기사 및 복사된 기사 검색조건
        if (copyYn != null && copyYn.trim().isEmpty() == false) {

            if ("N".equals(copyYn)) {
                query.must(QueryBuilders.matchQuery("orgArtclId", artclId));
            } else {
                // TODO : 아닌거 찾아서 넣기
                query.mustNot(QueryBuilders.matchQuery("orgArtclId", artclId));
            }
        }

        sourceBuilder.query(query);
        sourceBuilder.sort(new FieldSortBuilder("orgArtclId").order(SortOrder.DESC));
        sourceBuilder.sort(new FieldSortBuilder("artclOrd").order(SortOrder.ASC));
        sourceBuilder.from((int) pageable.getOffset());
        sourceBuilder.size(pageable.getPageSize());

        SearchRequest searchRequest = new SearchRequest("ans_article");
        searchRequest.source(sourceBuilder);

        List<ElasticSearchArticle> list = new ArrayList<>();
        long totalCount = 0;
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits searchHits = response.getHits();
            totalCount = searchHits.getTotalHits().value;
            for(SearchHit hit : searchHits.getHits()) {
                ObjectMapper objectMapper = new ObjectMapper();
/*                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(hit.toString());
                list.add(objectMapper.convertValue(jsonObject, ElasticSearchArticle.class));*/
                list.add(objectMapper.convertValue(hit.getSourceAsMap(), ElasticSearchArticle.class));
            }
        } catch (Exception e) {}

        //return null;
        return new PageImpl<>(list, pageable, totalCount);
    }
}
