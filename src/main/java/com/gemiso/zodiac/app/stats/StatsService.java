package com.gemiso.zodiac.app.stats;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheet.CueSheetRepository;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemRepository;
import com.gemiso.zodiac.app.cueSheetItemSymbol.CueSheetItemSymbol;
import com.gemiso.zodiac.app.symbol.Symbol;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;
    private final CueSheetRepository cueSheetRepository;
    private final CueSheetItemRepository cueSheetItemRepository;

    private final DateChangeHelper dateChangeHelper;

    public void create(){

        Integer brollCount = 0;
        Integer mngCount = 0;
        Integer telephoneCount = 0;
        Integer newsStudioCount = 0;
        Integer smartphoneCount = 0;
        Integer emptyCount = 0;
        Integer apkCount = 0;
        Integer pkCount = 0;


        Date today = new Date();

        String statsDate = dateChangeHelper.dateToStringNoTime(today);

        List<CueSheet> cueSheetList = cueSheetRepository.findStatsCue(statsDate);

        for (CueSheet cueSheet : cueSheetList){

            Long cueId = cueSheet.getCueId();

            List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findStatsCueItemList(cueId);

            for (CueSheetItem cueSheetItem : cueSheetItemList){

                String mngYn = "N";

                Set<CueSheetItemSymbol> symbols = cueSheetItem.getCueSheetItemSymbol();

                for (CueSheetItemSymbol cueSheetItemSymbol : symbols){

                    Symbol symbol = cueSheetItemSymbol.getSymbol();

                    String symbolId = symbol.getSymbolId();

                    if ("VMNG".equals(symbolId) || "AMNG".equals(symbolId)){

                        ++mngCount;

                        mngYn = "Y";

                        break;

                    }
                }

                Article article = cueSheetItem.getArticle();

                if (ObjectUtils.isEmpty(article) == false && "N".equals(mngYn) ){

                    String artclTypDtlCd = article.getArtclTypDtlCd();

                    if ("broll".equals(artclTypDtlCd)){
                        ++brollCount;
                    }
                    if ("mng".equals(artclTypDtlCd)){
                        ++mngCount;
                    }
                    if ("telephone".equals(artclTypDtlCd)){
                        ++telephoneCount;
                    }
                    if ("news_studio".equals(artclTypDtlCd)){
                        ++newsStudioCount;
                    }
                    if ("smartphone".equals(artclTypDtlCd)){
                        ++smartphoneCount;
                    }
                    if ("empty".equals(artclTypDtlCd)){
                        ++emptyCount;
                    }
                    if ("apk".equals(artclTypDtlCd)){
                        ++apkCount;
                    }
                    if ("pk".equals(artclTypDtlCd)){
                        ++pkCount;
                    }

                }
            }
        }


        Stats stats = Stats.builder()
                .brdcDt(statsDate)
                .brollCount(brollCount)
                .mngCount(mngCount)
                .telephoneCount(telephoneCount)
                .newsStudioCount(newsStudioCount)
                .smartphoneCount(smartphoneCount)
                .emptyCount(emptyCount)
                .apkCount(apkCount)
                .pkCount(pkCount)
                .build();

        statsRepository.save(stats);

    }
}
