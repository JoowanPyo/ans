package com.gemiso.zodiac.app.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemiso.zodiac.app.elasticsearch.articleEntity.ElasticSearchArticle;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
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

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
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
                                                                     String rptrId,
                                                                     String inputrId,
                                                                     String brdcPgmId,
                                                                     String artclDivCd,
                                                                     String artclTypCd,
                                                                     String searchDivCd,
                                                                     String searchWord,
                                                                     List<String> apprvDivCdList,
                                                                     Long deptCd,
                                                                     String artclCateCd,
                                                                     String artclTypDtlCd,
                                                                     String delYn,
                                                                     Long artclId,
                                                                     String copyYn,
                                                                     Long orgArtclId,
                                                                     Long cueId,
                                                                     Pageable pageable) throws Exception {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder query = QueryBuilders.boolQuery();


        //???????????? = ?????? ??????
        String setDelYn = "N";
        if (delYn != null && delYn.trim().isEmpty() == false) {
            setDelYn = delYn;
        }
        query.must(QueryBuilders.termQuery("delYn", setDelYn));


        //???????????? ???????????? ??????
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {

            DateChangeHelper dateChangeHelper = new DateChangeHelper();
            String getSdate = dateChangeHelper.dateToStringNormal(sdate);
            String getEdate = dateChangeHelper.dateToStringNormal(edate);

            query.must(QueryBuilders.rangeQuery("inputDtm").gte(getSdate).lte(getEdate));
        }else {
            DateChangeHelper dateChangeHelper = new DateChangeHelper();

            //???????????? ???????????? ?????? ??????
            Date nweDate = new Date();
            String formatDate = dateChangeHelper.dateToStringNoTime(nweDate);
            Date getDate = dateChangeHelper.StringToDateNoTime(formatDate);

            SearchDate searchDate = new SearchDate(getDate, getDate);
            Date newSdate = searchDate.getStartDate();
            Date newNdate = searchDate.getEndDate();

            String getSdate = dateChangeHelper.dateToStringNormal(newSdate);
            String getEdate = dateChangeHelper.dateToStringNormal(newNdate);

            query.must(QueryBuilders.rangeQuery("inputDtm").gte(getSdate).lte(getEdate));

        }
        //???????????? ??????
        if (searchWord != null && searchWord.trim().isEmpty() == false) {

            String[] searchWords = searchWord.split("\\s+");

            //?????????????????? 01 ?????? ?????? ???????????? ??????
            if ("01".equals(searchDivCd)) {

                for (String search : searchWords) {

                    query.must(QueryBuilders.boolQuery().should(QueryBuilders.multiMatchQuery(search,
                            "artclTitl", "artclTitl.nori", "artclTitl.fulltext", "artclTitlEn", "artclTitlEn.nori",
                            "artclTitlEn.fulltext"))
                            .should(QueryBuilders.wildcardQuery("artclTitl.fulltext", "*"+searchWord+"*")) //????????? ????????????
                            .should(QueryBuilders.wildcardQuery("artclTitlEn.fulltext", "*"+searchWord+"*")));
                }

            }
            //?????????????????? 02 ?????? ?????????????????? ??????
            else if ("02".equals(searchDivCd)) {

                Long getArtclId = 0L;
                boolean isNumeric =  searchWord.matches("[+-]?\\d*(\\.\\d+)?");
                if (isNumeric){
                    getArtclId = Long.parseLong(searchWord);
                }

                query.must(QueryBuilders.boolQuery().should(QueryBuilders.termQuery("orgArtclId", getArtclId)));
            }
            //?????????????????? ??????????????? ??????
            else if (searchDivCd == null || searchDivCd.trim().isEmpty()) {

                for (String search : searchWords) {

                    Long getArtclId = 0L;
                    String str = search;
                    boolean isNumeric =  str.matches("[+-]?\\d*(\\.\\d+)?");
                    if (isNumeric){
                        getArtclId = Long.parseLong(str);
                    }

                    query.must(QueryBuilders.boolQuery().should(QueryBuilders.multiMatchQuery(search,
                            "artclTitl", "artclTitl.nori", "artclTitl.fulltext", "artclTitlEn",
                            "artclTitlEn.nori", "artclTitlEn.fulltext", "ancMentCtt.nori", "ancMentCtt.fulltext",
                            "artclCtt.nori", "artclCtt.fulltext"))
                            .should(QueryBuilders.termQuery("orgArtclId", getArtclId))
                            .should(QueryBuilders.wildcardQuery("artclTitl.fulltext", "*"+searchWord+"*")) //????????? ????????????
                            .should(QueryBuilders.wildcardQuery("artclTitlEn.fulltext", "*"+searchWord+"*")) //??????????????? ????????????
                            .should(QueryBuilders.wildcardQuery("ancMentCtt.fulltext", "*"+searchWord+"*")) //?????????????????? ????????????
                            .should(QueryBuilders.wildcardQuery("artclCtt.fulltext", "*"+searchWord+"*")) //?????? ????????????
                            //??????
                            .should(QueryBuilders.nestedQuery(
                                    "tags", QueryBuilders.matchQuery("tags.tag", searchWord), ScoreMode.None)));
                }

            }

        }
        //??????????????????[???????????? or???????????? ??????]
        if (CollectionUtils.isEmpty(apprvDivCdList) == false) {
            query.must(QueryBuilders.termsQuery("apprvDivCd", apprvDivCdList));
        }
        //?????? ?????? ??? ????????? ?????? ????????????
        if (copyYn != null && copyYn.trim().isEmpty() == false) {

            if ("N".equals(copyYn)) {
                query.must(QueryBuilders.termQuery("artclOrd", 0));
            } else {
                // TODO : ????????? ????????? ??????
                query.mustNot(QueryBuilders.termQuery("artclOrd", 0));
            }
        }
        //?????? ?????? ????????? ??????
        if (artclTypCd != null && artclTypCd.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("artclTypCd", artclTypCd));
        }
        //????????????????????????
        if (artclTypDtlCd != null && artclTypDtlCd.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("artclTypDtlCd", artclTypDtlCd));
        }
        //????????????????????????
        if (artclCateCd != null && artclCateCd.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("artclCateCd", artclCateCd));
        }
        //????????????
        if (deptCd != null && deptCd != 0) {
            query.must(QueryBuilders.termQuery("deptCd", deptCd));
        }
        //????????? ???????????? ??????
        if (inputrId != null && inputrId.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("inputrId", inputrId));
        }
        //?????? ???????????? ??????
        if (rptrId != null && rptrId.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("rptrId", rptrId));
        }
        //?????? ???????????? ???????????? ??????
        if (brdcPgmId != null && brdcPgmId.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("brdcPgmId", brdcPgmId));
        }

        //?????? ?????? ????????? ??????
        /*if (artclDivCd != null && artclDivCd.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("artclDivCd", artclDivCd));
        }
        //?????? ?????????
        if (ObjectUtils.isEmpty(artclId) == false) {
            query.must(QueryBuilders.matchQuery("artclId", artclId));
        }
        //???????????? ?????????
        if (ObjectUtils.isEmpty(orgArtclId) == false){
            query.must(QueryBuilders.matchQuery("orgArtclId", orgArtclId));
        }*/
        //?????? ?????? ???????????? ??????
       /* if (ObjectUtils.isEmpty(rcvDt) == false) {

            //rcvDt(????????????)????????? ?????? +1 days
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rcvDt);
            calendar.add(Calendar.DATE, 1);
            Date rcvDtTomerrow = calendar.getTime();
            query.must(QueryBuilders.rangeQuery("inputDtm").from(rcvDt).to(rcvDtTomerrow));
        }*/

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
        } catch (IOException | RuntimeException e) {
            log.error("findByElasticSearchArticleList ERROR ");
        }

        //return null;
        return new PageImpl<>(list, pageable, totalCount);
    }

    @Override
    public Page<ElasticSearchArticle> findByElasticSearchArticleListCue(Date sdate, Date edate, String searchWord,
                                                                        Long cueId, String brdcPgmId, String artclTypCd,
                                                                        String artclTypDtlCd, String copyYn, Long deptCd,
                                                                        Long orgArtclId, String rptrId, Pageable pageable) throws Exception {


        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder query = QueryBuilders.boolQuery();


        //???????????? = ?????? ??????

        query.must(QueryBuilders.termQuery("delYn", "N"));


        //???????????? ???????????? ??????
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {

            DateChangeHelper dateChangeHelper = new DateChangeHelper();
            String getSdate = dateChangeHelper.dateToStringNormal(sdate);
            String getEdate = dateChangeHelper.dateToStringNormal(edate);

            query.must(QueryBuilders.rangeQuery("inputDtm").gte(getSdate).lte(getEdate));
        }else {
            DateChangeHelper dateChangeHelper = new DateChangeHelper();

            //???????????? ???????????? ?????? ??????
            Date nweDate = new Date();
            String formatDate = dateChangeHelper.dateToStringNoTime(nweDate);
            Date getDate = dateChangeHelper.StringToDateNoTime(formatDate);

            SearchDate searchDate = new SearchDate(getDate, getDate);
            Date newSdate = searchDate.getStartDate();
            Date newNdate = searchDate.getEndDate();

            String getSdate = dateChangeHelper.dateToStringNormal(newSdate);
            String getEdate = dateChangeHelper.dateToStringNormal(newNdate);

            query.must(QueryBuilders.rangeQuery("inputDtm").gte(getSdate).lte(getEdate));

        }

        //?????????
        //???????????? ??????
        if (searchWord != null && searchWord.trim().isEmpty() == false) {

            String[] searchWords = searchWord.split("\\s+");

            for (String search : searchWords) {

                Long getArtclId = 0L;
                String str = search;
                boolean isNumeric =  str.matches("[+-]?\\d*(\\.\\d+)?");
                if (isNumeric){
                    getArtclId = Long.parseLong(str);
                }

                query.must(QueryBuilders.boolQuery().should(QueryBuilders.multiMatchQuery(search,
                        "artclTitl", "artclTitl.nori", "artclTitl.fulltext", "artclTitlEn",
                        "artclTitlEn.nori", "artclTitlEn.fulltext"))
                        .should(QueryBuilders.termQuery("orgArtclId", getArtclId))
                        .should(QueryBuilders.wildcardQuery("artclTitl.fulltext", "*"+searchWord+"*")) //????????? ????????????
                        .should(QueryBuilders.wildcardQuery("artclTitlEn.fulltext", "*"+searchWord+"*"))); //??????????????? ????????????

            }
        }

        //?????? ?????? ??? ????????? ?????? ????????????
        if (copyYn != null && copyYn.trim().isEmpty() == false) {

            if ("N".equals(copyYn)) {
                query.must(QueryBuilders.termQuery("artclOrd", 0));
            } else {
                // TODO : ????????? ????????? ??????
                query.mustNot(QueryBuilders.termQuery("artclOrd", 0));
            }
        }
        //?????? ?????? ????????? ??????
        if (artclTypCd != null && artclTypCd.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("artclTypCd", artclTypCd));
        }
        //????????????????????????
        if (artclTypDtlCd != null && artclTypDtlCd.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("artclTypDtlCd", artclTypDtlCd));
        }
        //?????? ???????????? ???????????? ??????
        if (brdcPgmId != null && brdcPgmId.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("brdcPgmId", brdcPgmId));
        }
        //????????????
        if (deptCd != null && deptCd != 0) {
            query.must(QueryBuilders.termQuery("deptCd", deptCd));
        }
        if (rptrId != null && rptrId.trim().isEmpty() == false){
            query.must(QueryBuilders.termsQuery("rptrId", rptrId));
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
        } catch (IOException | RuntimeException e) {
            log.error("findByElasticSearchArticleListCue ERROR ");
        }

        //return null;
        return new PageImpl<>(list, pageable, totalCount);
    }

    @Override
    public List<ElasticSearchArticle> findByStatisticsArticle(Date sdate, Date edate, String rptrId,
                                                              String inputrId, String brdcPgmId, String artclDivCd,
                                                              String artclTypCd, String searchDivCd, String searchWord,
                                                              List<String> apprvDivCdList, Long deptCd, String artclCateCd,
                                                              String artclTypDtlCd, String delYn, Long artclId, String copyYn,
                                                              Long orgArtclId, Long cueId, Pageable pageable) throws Exception {



        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder query = QueryBuilders.boolQuery();


        //???????????? = ?????? ??????
        String setDelYn = "N";
        if (delYn != null && delYn.trim().isEmpty() == false) {
            setDelYn = delYn;
        }
        query.must(QueryBuilders.termQuery("delYn", setDelYn));


        //???????????? ???????????? ??????
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {

            DateChangeHelper dateChangeHelper = new DateChangeHelper();
            String getSdate = dateChangeHelper.dateToStringNormal(sdate);
            String getEdate = dateChangeHelper.dateToStringNormal(edate);

            query.must(QueryBuilders.rangeQuery("inputDtm").gte(getSdate).lte(getEdate));
        }else {
            DateChangeHelper dateChangeHelper = new DateChangeHelper();

            //???????????? ???????????? ?????? ??????
            Date nweDate = new Date();
            String formatDate = dateChangeHelper.dateToStringNoTime(nweDate);
            Date getDate = dateChangeHelper.StringToDateNoTime(formatDate);

            SearchDate searchDate = new SearchDate(getDate, getDate);
            Date newSdate = searchDate.getStartDate();
            Date newNdate = searchDate.getEndDate();

            String getSdate = dateChangeHelper.dateToStringNormal(newSdate);
            String getEdate = dateChangeHelper.dateToStringNormal(newNdate);

            query.must(QueryBuilders.rangeQuery("inputDtm").gte(getSdate).lte(getEdate));

        }
        //???????????? ??????
        if (searchWord != null && searchWord.trim().isEmpty() == false) {

            String[] searchWords = searchWord.split("\\s+");

            //?????????????????? 01 ?????? ?????? ???????????? ??????
            if ("01".equals(searchDivCd)) {

                for (String search : searchWords) {

                    query.must(QueryBuilders.boolQuery().should(QueryBuilders.multiMatchQuery(search,
                            "artclTitl", "artclTitl.nori", "artclTitl.fulltext", "artclTitlEn", "artclTitlEn.nori",
                            "artclTitlEn.fulltext"))
                            .should(QueryBuilders.wildcardQuery("artclTitl.fulltext", "*"+searchWord+"*")) //????????? ????????????
                            .should(QueryBuilders.wildcardQuery("artclTitlEn.fulltext", "*"+searchWord+"*")));
                }

            }
            //?????????????????? 02 ?????? ?????????????????? ??????
            else if ("02".equals(searchDivCd)) {

                Long getArtclId = 0L;
                boolean isNumeric =  searchWord.matches("[+-]?\\d*(\\.\\d+)?");
                if (isNumeric){
                    getArtclId = Long.parseLong(searchWord);
                }

                query.must(QueryBuilders.boolQuery().should(QueryBuilders.termQuery("orgArtclId", getArtclId)));
            }
            //?????????????????? ??????????????? ??????
            else if (searchDivCd == null || searchDivCd.trim().isEmpty()) {

                for (String search : searchWords) {

                    Long getArtclId = 0L;
                    String str = search;
                    boolean isNumeric =  str.matches("[+-]?\\d*(\\.\\d+)?");
                    if (isNumeric){
                        getArtclId = Long.parseLong(str);
                    }

                    query.must(QueryBuilders.boolQuery().should(QueryBuilders.multiMatchQuery(search,
                            "artclTitl", "artclTitl.nori", "artclTitl.fulltext", "artclTitlEn",
                            "artclTitlEn.nori", "artclTitlEn.fulltext", "ancMentCtt.nori", "ancMentCtt.fulltext",
                            "artclCtt.nori", "artclCtt.fulltext"))
                            .should(QueryBuilders.termQuery("orgArtclId", getArtclId))
                            .should(QueryBuilders.wildcardQuery("artclTitl.fulltext", "*"+searchWord+"*")) //????????? ????????????
                            .should(QueryBuilders.wildcardQuery("artclTitlEn.fulltext", "*"+searchWord+"*")) //??????????????? ????????????
                            .should(QueryBuilders.wildcardQuery("ancMentCtt.fulltext", "*"+searchWord+"*")) //?????????????????? ????????????
                            .should(QueryBuilders.wildcardQuery("artclCtt.fulltext", "*"+searchWord+"*")) //?????? ????????????
                            //??????
                            .should(QueryBuilders.nestedQuery(
                                    "tags", QueryBuilders.matchQuery("tags.tag", searchWord), ScoreMode.None)));
                }

            }

        }
        //??????????????????[???????????? or???????????? ??????]
        if (CollectionUtils.isEmpty(apprvDivCdList) == false) {
            query.must(QueryBuilders.termsQuery("apprvDivCd", apprvDivCdList));
        }
        //?????? ?????? ??? ????????? ?????? ????????????
        if (copyYn != null && copyYn.trim().isEmpty() == false) {

            if ("N".equals(copyYn)) {
                query.must(QueryBuilders.termQuery("artclOrd", 0));
            } else {
                // TODO : ????????? ????????? ??????
                query.mustNot(QueryBuilders.termQuery("artclOrd", 0));
            }
        }
        //?????? ?????? ????????? ??????
        if (artclTypCd != null && artclTypCd.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("artclTypCd", artclTypCd));
        }
        //????????????????????????
        if (artclTypDtlCd != null && artclTypDtlCd.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("artclTypDtlCd", artclTypDtlCd));
        }
        //????????????????????????
        if (artclCateCd != null && artclCateCd.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("artclCateCd", artclCateCd));
        }
        //????????????
        if (deptCd != null && deptCd != 0) {
            query.must(QueryBuilders.termQuery("deptCd", deptCd));
        }
        //????????? ???????????? ??????
        if (inputrId != null && inputrId.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("inputrId", inputrId));
        }
        //?????? ???????????? ??????
        if (rptrId != null && rptrId.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("rptrId", rptrId));
        }
        //?????? ???????????? ???????????? ??????
        if (brdcPgmId != null && brdcPgmId.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("brdcPgmId", brdcPgmId));
        }

        sourceBuilder.query(query);
        sourceBuilder.sort(new FieldSortBuilder("inputDtm").order(SortOrder.DESC));
        sourceBuilder.from((int) pageable.getOffset());
        sourceBuilder.size(pageable.getPageSize());
        sourceBuilder.trackTotalHits(true);

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
        } catch (IOException | RuntimeException e) {
            log.error("findByStatisticsArticle ERROR ");
        }

        //return null;
        return list;
    }

    @Override
    public Long findByStatisticsArticleCount(Date sdate, Date edate, String rptrId, String inputrId, String brdcPgmId, String artclDivCd, String artclTypCd, String searchDivCd, String searchWord, List<String> apprvDivCdList, Long deptCd, String artclCateCd, String artclTypDtlCd, String delYn, Long artclId, String copyYn, Long orgArtclId, Long cueId) throws Exception {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder query = QueryBuilders.boolQuery();


        //???????????? = ?????? ??????
        String setDelYn = "N";
        if (delYn != null && delYn.trim().isEmpty() == false) {
            setDelYn = delYn;
        }
        query.must(QueryBuilders.termQuery("delYn", setDelYn));


        //???????????? ???????????? ??????
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {

            DateChangeHelper dateChangeHelper = new DateChangeHelper();
            String getSdate = dateChangeHelper.dateToStringNormal(sdate);
            String getEdate = dateChangeHelper.dateToStringNormal(edate);

            query.must(QueryBuilders.rangeQuery("inputDtm").gte(getSdate).lte(getEdate));
        }else {
            DateChangeHelper dateChangeHelper = new DateChangeHelper();

            //???????????? ???????????? ?????? ??????
            Date nweDate = new Date();
            String formatDate = dateChangeHelper.dateToStringNoTime(nweDate);
            Date getDate = dateChangeHelper.StringToDateNoTime(formatDate);

            SearchDate searchDate = new SearchDate(getDate, getDate);
            Date newSdate = searchDate.getStartDate();
            Date newNdate = searchDate.getEndDate();

            String getSdate = dateChangeHelper.dateToStringNormal(newSdate);
            String getEdate = dateChangeHelper.dateToStringNormal(newNdate);

            query.must(QueryBuilders.rangeQuery("inputDtm").gte(getSdate).lte(getEdate));

        }
        //???????????? ??????
        if (searchWord != null && searchWord.trim().isEmpty() == false) {

            String[] searchWords = searchWord.split("\\s+");

            //?????????????????? 01 ?????? ?????? ???????????? ??????
            if ("01".equals(searchDivCd)) {

                for (String search : searchWords) {

                    query.must(QueryBuilders.boolQuery().should(QueryBuilders.multiMatchQuery(search,
                            "artclTitl", "artclTitl.nori", "artclTitl.fulltext", "artclTitlEn", "artclTitlEn.nori",
                            "artclTitlEn.fulltext"))
                            .should(QueryBuilders.wildcardQuery("artclTitl.fulltext", "*"+searchWord+"*")) //????????? ????????????
                            .should(QueryBuilders.wildcardQuery("artclTitlEn.fulltext", "*"+searchWord+"*")));
                }

            }
            //?????????????????? 02 ?????? ?????????????????? ??????
            else if ("02".equals(searchDivCd)) {

                Long getArtclId = 0L;
                boolean isNumeric =  searchWord.matches("[+-]?\\d*(\\.\\d+)?");
                if (isNumeric){
                    getArtclId = Long.parseLong(searchWord);
                }

                query.must(QueryBuilders.boolQuery().should(QueryBuilders.termQuery("orgArtclId", getArtclId)));
            }
            //?????????????????? ??????????????? ??????
            else if (searchDivCd == null || searchDivCd.trim().isEmpty()) {

                for (String search : searchWords) {

                    Long getArtclId = 0L;
                    String str = search;
                    boolean isNumeric =  str.matches("[+-]?\\d*(\\.\\d+)?");
                    if (isNumeric){
                        getArtclId = Long.parseLong(str);
                    }

                    query.must(QueryBuilders.boolQuery().should(QueryBuilders.multiMatchQuery(search,
                            "artclTitl", "artclTitl.nori", "artclTitl.fulltext", "artclTitlEn",
                            "artclTitlEn.nori", "artclTitlEn.fulltext", "ancMentCtt.nori", "ancMentCtt.fulltext",
                            "artclCtt.nori", "artclCtt.fulltext"))
                            .should(QueryBuilders.termQuery("orgArtclId", getArtclId))
                            .should(QueryBuilders.wildcardQuery("artclTitl.fulltext", "*"+searchWord+"*")) //????????? ????????????
                            .should(QueryBuilders.wildcardQuery("artclTitlEn.fulltext", "*"+searchWord+"*")) //??????????????? ????????????
                            .should(QueryBuilders.wildcardQuery("ancMentCtt.fulltext", "*"+searchWord+"*")) //?????????????????? ????????????
                            .should(QueryBuilders.wildcardQuery("artclCtt.fulltext", "*"+searchWord+"*")) //?????? ????????????
                            //??????
                            .should(QueryBuilders.nestedQuery(
                                    "tags", QueryBuilders.matchQuery("tags.tag", searchWord), ScoreMode.None)));
                }

            }

        }
        //??????????????????[???????????? or???????????? ??????]
        if (CollectionUtils.isEmpty(apprvDivCdList) == false) {
            query.must(QueryBuilders.termsQuery("apprvDivCd", apprvDivCdList));
        }
        //?????? ?????? ??? ????????? ?????? ????????????
        if (copyYn != null && copyYn.trim().isEmpty() == false) {

            if ("N".equals(copyYn)) {
                query.must(QueryBuilders.termQuery("artclOrd", 0));
            } else {
                // TODO : ????????? ????????? ??????
                query.mustNot(QueryBuilders.termQuery("artclOrd", 0));
            }
        }
        //?????? ?????? ????????? ??????
        if (artclTypCd != null && artclTypCd.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("artclTypCd", artclTypCd));
        }
        //????????????????????????
        if (artclTypDtlCd != null && artclTypDtlCd.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("artclTypDtlCd", artclTypDtlCd));
        }
        //????????????????????????
        if (artclCateCd != null && artclCateCd.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("artclCateCd", artclCateCd));
        }
        //????????????
        if (deptCd != null && deptCd != 0) {
            query.must(QueryBuilders.termQuery("deptCd", deptCd));
        }
        //????????? ???????????? ??????
        if (inputrId != null && inputrId.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("inputrId", inputrId));
        }
        //?????? ???????????? ??????
        if (rptrId != null && rptrId.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("rptrId", rptrId));
        }
        //?????? ???????????? ???????????? ??????
        if (brdcPgmId != null && brdcPgmId.trim().isEmpty() == false) {
            query.must(QueryBuilders.termQuery("brdcPgmId", brdcPgmId));
        }

        sourceBuilder.trackTotalHits(true);
        sourceBuilder.query(query);
        //sourceBuilder.sort(new FieldSortBuilder("inputDtm").order(SortOrder.DESC));
        //sourceBuilder.from(page);
        //sourceBuilder.size(10000);

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
        } catch (IOException | RuntimeException e) {
            log.error("findByStatisticsArticleCount ERROR ");
        }

        //return null;
        return totalCount;
    }
}
