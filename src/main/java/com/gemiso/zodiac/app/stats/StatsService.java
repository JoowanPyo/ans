package com.gemiso.zodiac.app.stats;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.ArticleRepository;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheet.CueSheetRepository;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemRepository;
import com.gemiso.zodiac.app.cueSheetItemSymbol.CueSheetItemSymbol;
import com.gemiso.zodiac.app.stats.dto.StatsCreateDTO;
import com.gemiso.zodiac.app.stats.dto.StatsDTO;
import com.gemiso.zodiac.app.stats.dto.StatsListDTO;
import com.gemiso.zodiac.app.stats.dto.StatsTotalDTO;
import com.gemiso.zodiac.app.stats.mapper.StatsMapper;
import com.gemiso.zodiac.app.symbol.Symbol;
import com.gemiso.zodiac.app.user.dto.UserDTO;
import com.gemiso.zodiac.core.enumeration.StatsEnum;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import com.gemiso.zodiac.core.helper.SearchDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;
    private final CueSheetRepository cueSheetRepository;
    private final CueSheetItemRepository cueSheetItemRepository;
    private final ArticleRepository articleRepository;

    private final StatsMapper statsMapper;

    private final DateChangeHelper dateChangeHelper;


    public List<StatsDTO> findAll(Date sdate, Date edate){

        String startDate = dateChangeHelper.dateToStringNoTime(sdate);
        String endDate = dateChangeHelper.dateToStringNoTime(edate);

        List<Stats> statsList = statsRepository.findStats(startDate, endDate);

        List<StatsDTO> statsDTOList = statsMapper.toDtoList(statsList);

        return statsDTOList;

    }

    public StatsListDTO totalStats(List<StatsDTO> statsDTOList){

        StatsListDTO returnDTO = new StatsListDTO();

        List<StatsDTO> statsOrgDTOs = new ArrayList<>();
        List<StatsDTO> statsCopyDTOs = new ArrayList<>();

        for (StatsDTO statsDTO : statsDTOList){

            String artcldiv = statsDTO.getArtclDiv();

            if (StatsEnum.ORIGINAL.getStatsEcum(StatsEnum.ORIGINAL).equals(artcldiv)){
                statsOrgDTOs.add(statsDTO);
            }
            if (StatsEnum.COPY.getStatsEcum(StatsEnum.COPY).equals(artcldiv)){
                statsCopyDTOs.add(statsDTO);
            }
        }

        List<StatsTotalDTO> orgTotalList = getOTotal(statsOrgDTOs);
        List<StatsTotalDTO> copyTotalList = getOTotal(statsCopyDTOs);

        returnDTO.setOrgStatsList(orgTotalList);
        returnDTO.setCopyStatsList(copyTotalList);

        return returnDTO;

    }

    public List<StatsTotalDTO> getOTotal(List<StatsDTO> statsDTOs){


        List<StatsTotalDTO> totalList = new ArrayList<>();

        Integer chkMm = 1;

        Integer brollCount = 0;
        Integer mngCount = 0;
        Integer telephoneCount = 0;
        Integer newsStudioCount = 0;
        Integer smartphoneCount = 0;
        Integer emptyCount = 0;
        Integer apkCount = 0;
        Integer pkCount= 0;

        Integer listSize = statsDTOs.size();

        for (StatsDTO statsDTO : statsDTOs){

            String statsDt = statsDTO.getStatsDt();
            String mm = statsDt.substring(5,7);
            Integer mmI = Integer.parseInt(mm);

            if (chkMm == mmI && listSize != 1){

                brollCount = brollCount + Optional.ofNullable(statsDTO.getBrollCount()).orElse(0);
                mngCount = mngCount + Optional.ofNullable(statsDTO.getMngCount()).orElse(0);
                telephoneCount = telephoneCount + Optional.ofNullable(statsDTO.getTelephoneCount()).orElse(0);
                newsStudioCount = newsStudioCount + Optional.ofNullable(statsDTO.getNewsStudioCount()).orElse(0);
                smartphoneCount = smartphoneCount + Optional.ofNullable(statsDTO.getSmartphoneCount()).orElse(0);
                emptyCount = emptyCount + Optional.ofNullable(statsDTO.getEmptyCount()).orElse(0);
                apkCount = apkCount + Optional.ofNullable(statsDTO.getApkCount()).orElse(0);
                pkCount = pkCount + Optional.ofNullable(statsDTO.getPkCount()).orElse(0);


            }else if (chkMm == mmI && listSize == 1){

                brollCount = brollCount + Optional.ofNullable(statsDTO.getBrollCount()).orElse(0);
                mngCount = mngCount + Optional.ofNullable(statsDTO.getMngCount()).orElse(0);
                telephoneCount = telephoneCount + Optional.ofNullable(statsDTO.getTelephoneCount()).orElse(0);
                newsStudioCount = newsStudioCount + Optional.ofNullable(statsDTO.getNewsStudioCount()).orElse(0);
                smartphoneCount = smartphoneCount + Optional.ofNullable(statsDTO.getSmartphoneCount()).orElse(0);
                emptyCount = emptyCount + Optional.ofNullable(statsDTO.getEmptyCount()).orElse(0);
                apkCount = apkCount + Optional.ofNullable(statsDTO.getApkCount()).orElse(0);
                pkCount = pkCount + Optional.ofNullable(statsDTO.getPkCount()).orElse(0);

                StatsTotalDTO statsTotalDTO = new StatsTotalDTO();
                statsTotalDTO.setMmOrd(chkMm);
                statsTotalDTO.setBrollCount(brollCount);
                statsTotalDTO.setMngCount(mngCount);
                statsTotalDTO.setTelephoneCount(telephoneCount);
                statsTotalDTO.setNewsStudioCount(newsStudioCount);
                statsTotalDTO.setSmartphoneCount(smartphoneCount);
                statsTotalDTO.setEmptyCount(emptyCount);
                statsTotalDTO.setApkCount(apkCount);
                statsTotalDTO.setPkCount(pkCount);

                totalList.add(statsTotalDTO);

            } else {

                StatsTotalDTO statsTotalDTO = new StatsTotalDTO();
                statsTotalDTO.setMmOrd(chkMm);
                statsTotalDTO.setBrollCount(brollCount);
                statsTotalDTO.setMngCount(mngCount);
                statsTotalDTO.setTelephoneCount(telephoneCount);
                statsTotalDTO.setNewsStudioCount(newsStudioCount);
                statsTotalDTO.setSmartphoneCount(smartphoneCount);
                statsTotalDTO.setEmptyCount(emptyCount);
                statsTotalDTO.setApkCount(apkCount);
                statsTotalDTO.setPkCount(pkCount);

                totalList.add(statsTotalDTO);

                brollCount = 0;
                mngCount = 0;
                telephoneCount = 0;
                newsStudioCount = 0;
                smartphoneCount = 0;
                emptyCount = 0;
                apkCount = 0;
                pkCount= 0;

                ++chkMm;

            }
            --listSize;
        }


        return totalList;
    }

    public void create(StatsCreateDTO statsCreateDTO) throws Exception {

        Date sdate = statsCreateDTO.getSdate();
        Date edate = statsCreateDTO.getEdate();

        Calendar ecal = Calendar.getInstance();
        ecal.setTime(edate);
        ecal.add(Calendar.DATE, 1);

        edate = ecal.getTime();

        while (sdate.before(edate)){

            createStatsCopyArticle(sdate);

            createStatsOrgAritlce(sdate);

            Calendar scal = Calendar.getInstance();
            scal.setTime(sdate);
            scal.add(Calendar.DATE, 1);

            sdate = scal.getTime();
        }

    }

    //받을 날짜를 통해 기사통계 등록
    public void createStatsOrgAritlce(Date date) throws Exception {

        Integer brollCount = 0;
        Integer mngCount = 0;
        Integer telephoneCount = 0;
        Integer newsStudioCount = 0;
        Integer smartphoneCount = 0;
        Integer emptyCount = 0;
        Integer apkCount = 0;
        Integer pkCount = 0;

        SearchDate searchDate = new SearchDate(date, date);

        Date sdate = searchDate.getStartDate();
        Date edate = searchDate.getEndDate();

        List<Article> articleList = articleRepository.findOrgArticleStats(sdate, edate);

        String statsDate = dateChangeHelper.dateToStringNoTime(date);

        List<Stats> statsList = statsRepository.findStats(statsDate, statsDate);

        if (CollectionUtils.isEmpty(articleList) == false){

            for (Article article : articleList){

                Date inputDtm = article.getInputDtm();

                String artclDtm = dateChangeHelper.dateToStringNoTime(date);

                for (Stats stats : statsList){

                    String statsDt = stats.getStatsDt();
                    String artclDiv = stats.getArtclDiv();

                    if (statsDt.equals(artclDtm) && artclDiv.equals(StatsEnum.ORIGINAL.getStatsEcum(StatsEnum.ORIGINAL))) {

                        statsRepository.delete(stats);
                    }
                }

                String artclTypDtlCd = article.getArtclTypDtlCd();

                if ("broll".equals(artclTypDtlCd)) {
                    ++brollCount;
                }
                if ("mng".equals(artclTypDtlCd)) {
                    ++mngCount;
                }
                if ("telephone".equals(artclTypDtlCd)) {
                    ++telephoneCount;
                }
                if ("news_studio".equals(artclTypDtlCd)) {
                    ++newsStudioCount;
                }
                if ("smartphone".equals(artclTypDtlCd)) {
                    ++smartphoneCount;
                }
                if ("empty".equals(artclTypDtlCd)) {
                    ++emptyCount;
                }
                if ("apk".equals(artclTypDtlCd)) {
                    ++apkCount;
                }
                if ("pk".equals(artclTypDtlCd)) {
                    ++pkCount;
                }
            }

            Stats stats = Stats.builder()
                    .statsDt(statsDate)
                    .brollCount(brollCount)
                    .mngCount(mngCount)
                    .telephoneCount(telephoneCount)
                    .newsStudioCount(newsStudioCount)
                    .smartphoneCount(smartphoneCount)
                    .emptyCount(emptyCount)
                    .apkCount(apkCount)
                    .pkCount(pkCount)
                    .artclDiv(StatsEnum.ORIGINAL.getStatsEcum(StatsEnum.ORIGINAL))
                    .build();

            statsRepository.save(stats);

        }else {

            //이미 기다통계가 등록되어 있다면 다시등록하기 위해 이미 등록되어 있던 통계 삭제 후 재등록.
            for (Stats stats : statsList) {

                String statsDt = stats.getStatsDt();
                String artclDiv = stats.getArtclDiv();


                if (statsDt.equals(statsDate) && artclDiv.equals(StatsEnum.ORIGINAL.getStatsEcum(StatsEnum.ORIGINAL))) {

                    statsRepository.delete(stats);
                }
            }

            Stats stats = Stats.builder()
                    .statsDt(statsDate)
                    .brollCount(brollCount)
                    .mngCount(mngCount)
                    .telephoneCount(telephoneCount)
                    .newsStudioCount(newsStudioCount)
                    .smartphoneCount(smartphoneCount)
                    .emptyCount(emptyCount)
                    .apkCount(apkCount)
                    .pkCount(pkCount)
                    .artclDiv(StatsEnum.ORIGINAL.getStatsEcum(StatsEnum.ORIGINAL))
                    .build();

            statsRepository.save(stats);

        }

    }

    //받을 날짜를 통해 기사통계 등록
    public void createStatsCopyArticle(Date searchDate){

        Integer brollCount = 0;
        Integer mngCount = 0;
        Integer telephoneCount = 0;
        Integer newsStudioCount = 0;
        Integer smartphoneCount = 0;
        Integer emptyCount = 0;
        Integer apkCount = 0;
        Integer pkCount = 0;


       // Date today = new Date();

        String statsDate = dateChangeHelper.dateToStringNoTime(searchDate);

        List<CueSheet> cueSheetList = cueSheetRepository.findStatsCue(statsDate);

        List<Stats> statsList = statsRepository.findStats(statsDate, statsDate);

        if (CollectionUtils.isEmpty(cueSheetList) == false) {

            for (CueSheet cueSheet : cueSheetList) {

                String cueBrdcDt = cueSheet.getBrdcDt();

                //이미 기다통계가 등록되어 있다면 다시등록하기 위해 이미 등록되어 있던 통계 삭제 후 재등록.
                for (Stats stats : statsList) {

                    String statsDt = stats.getStatsDt();
                    String artclDiv = stats.getArtclDiv();

                    if (statsDt.equals(cueBrdcDt) && artclDiv.equals(StatsEnum.COPY.getStatsEcum(StatsEnum.COPY))) {

                        statsRepository.delete(stats);
                    }
                }

                Long cueId = cueSheet.getCueId();

                List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findStatsCueItemList(cueId);

                for (CueSheetItem cueSheetItem : cueSheetItemList) {

                    String mngYn = "N";

                    Set<CueSheetItemSymbol> symbols = cueSheetItem.getCueSheetItemSymbol();

                    for (CueSheetItemSymbol cueSheetItemSymbol : symbols) {

                        Symbol symbol = cueSheetItemSymbol.getSymbol();

                        String symbolId = symbol.getSymbolId();

                        if ("VMNG".equals(symbolId) || "AMNG".equals(symbolId)) {

                            ++mngCount;

                            mngYn = "Y";

                            break;

                        }
                    }

                    Article article = cueSheetItem.getArticle();

                    if (ObjectUtils.isEmpty(article) == false && "N".equals(mngYn)) {

                        String artclTypDtlCd = article.getArtclTypDtlCd();

                        if ("broll".equals(artclTypDtlCd)) {
                            ++brollCount;
                        }
                        if ("mng".equals(artclTypDtlCd)) {
                            ++mngCount;
                        }
                        if ("telephone".equals(artclTypDtlCd)) {
                            ++telephoneCount;
                        }
                        if ("news_studio".equals(artclTypDtlCd)) {
                            ++newsStudioCount;
                        }
                        if ("smartphone".equals(artclTypDtlCd)) {
                            ++smartphoneCount;
                        }
                        if ("empty".equals(artclTypDtlCd)) {
                            ++emptyCount;
                        }
                        if ("apk".equals(artclTypDtlCd)) {
                            ++apkCount;
                        }
                        if ("pk".equals(artclTypDtlCd)) {
                            ++pkCount;
                        }

                    }
                }
            }

            Stats stats = Stats.builder()
                    .statsDt(statsDate)
                    .brollCount(brollCount)
                    .mngCount(mngCount)
                    .telephoneCount(telephoneCount)
                    .newsStudioCount(newsStudioCount)
                    .smartphoneCount(smartphoneCount)
                    .emptyCount(emptyCount)
                    .apkCount(apkCount)
                    .pkCount(pkCount)
                    .artclDiv(StatsEnum.COPY.getStatsEcum(StatsEnum.COPY))
                    .build();

            statsRepository.save(stats);
            
        }else { //큐시트가 없는경우

            //이미 기다통계가 등록되어 있다면 다시등록하기 위해 이미 등록되어 있던 통계 삭제 후 재등록.
            for (Stats stats : statsList) {

                String statsDt = stats.getStatsDt();
                String artclDiv = stats.getArtclDiv();

                if (statsDt.equals(statsDate) && artclDiv.equals(StatsEnum.COPY.getStatsEcum(StatsEnum.COPY))) {

                    statsRepository.delete(stats);
                }
            }

            Stats stats = Stats.builder()
                    .statsDt(statsDate)
                    .brollCount(brollCount)
                    .mngCount(mngCount)
                    .telephoneCount(telephoneCount)
                    .newsStudioCount(newsStudioCount)
                    .smartphoneCount(smartphoneCount)
                    .emptyCount(emptyCount)
                    .apkCount(apkCount)
                    .pkCount(pkCount)
                    .artclDiv(StatsEnum.COPY.getStatsEcum(StatsEnum.COPY))
                    .build();

            statsRepository.save(stats);

        }
    }

    //스케쥴러( 크론탭 ) 으로 매일 기사통계를 등록한다.
    public void createDaily() throws Exception {


        Date today = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, -1);

        Date setTime = cal.getTime();

        String settingDate = dateChangeHelper.dateToStringNoTime(setTime);
        Date orgSearchDate = dateChangeHelper.StringToDateNoTime(settingDate);

        createStatsOrgAritlce(orgSearchDate); // 원본기사 등록
        createStatsCopyArticle(setTime); // 복사본 기사 등록
    }

    public void excelDownload(HttpServletResponse response, List<StatsDTO> statsList) throws IOException {

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("ANS 기사 통계");
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 3500);
        sheet.setColumnWidth(3, 3500);
        sheet.setColumnWidth(4, 3500);
        sheet.setColumnWidth(5, 3500);
        sheet.setColumnWidth(6, 3500);
        sheet.setColumnWidth(7, 3500);
        sheet.setColumnWidth(8, 3500);
        sheet.setColumnWidth(9, 3500);
        //sheet.setColumnWidth(6, 4000);
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        // Header
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("통계일");
        cell = row.createCell(1);
        cell.setCellValue("기사 구분");
        cell = row.createCell(2);
        cell.setCellValue("broll");
        cell = row.createCell(3);
        cell.setCellValue("apk");
        cell = row.createCell(4);
        cell.setCellValue("pk");
        cell = row.createCell(5);
        cell.setCellValue("mng");
        cell = row.createCell(6);
        cell.setCellValue("news_studio");
        cell = row.createCell(7);
        cell.setCellValue("smartphone");
        cell = row.createCell(8);
        cell.setCellValue("telephone");
        cell = row.createCell(9);
        cell.setCellValue("empty");


        // Body
        for (StatsDTO statsDTO : statsList) {

            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(statsDTO.getStatsDt());
            cell = row.createCell(1);
            cell.setCellValue(statsDTO.getArtclDiv());
            cell = row.createCell(2);
            cell.setCellValue(statsDTO.getBrollCount());
            cell = row.createCell(3);
            cell.setCellValue(statsDTO.getApkCount());
            cell = row.createCell(4);
            cell.setCellValue(statsDTO.getPkCount());
            cell = row.createCell(5);
            cell.setCellValue(statsDTO.getMngCount());
            cell = row.createCell(6);
            cell.setCellValue(statsDTO.getNewsStudioCount());
            cell = row.createCell(7);
            cell.setCellValue(statsDTO.getSmartphoneCount());
            cell = row.createCell(8);
            cell.setCellValue(statsDTO.getTelephoneCount());
            cell = row.createCell(9);
            cell.setCellValue(statsDTO.getEmptyCount());

        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
//        response.setHeader("Content-Disposition", "attachment;filename=example.xls");
        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();

    }
}
