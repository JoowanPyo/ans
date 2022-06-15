package com.gemiso.zodiac.core.scheduling;

import com.gemiso.zodiac.app.article.ArticleService;
import com.gemiso.zodiac.app.cueSheet.CueSheetService;
import com.gemiso.zodiac.core.mis.MisService;
import com.gemiso.zodiac.core.scheduling.dto.BisDailyScheduleDTO;
import com.gemiso.zodiac.core.scheduling.dto.BisProgramDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class Scheduling {

    private final BisInterfaceService bisInterfaceService;
    private final MisService misService;
    private final ArticleService articleService;
    private final CueSheetService cueSheetService;

    //BIS프로그램 조회 및 등록
    @Scheduled(cron = "0 0 5 * * ?")//매일 새벽 5시에 한번씩
    //@Scheduled(cron = "0 * * * * *")
    public void bisProgramCreate() throws Exception {

        log.info("Program Create Start");

        //bis에서 프로그램 정보를 가져온다.
        BisProgramDTO bisProgramDTO = bisInterfaceService.bisProgramfindAll();

        //현재 등록되어있는 ANS프로그램 정보와 bis에서 불러온 프로그램 정보를 비교하여 새로들어온 프로그램 정보를 업데이트 해준다.
        bisInterfaceService.matchProgramCreate(bisProgramDTO);

    }

    /**************** BIS에서 사용하는 기본편성이 관리가 되지않아 ANS에서 주간편성으로 기본편성 생성 *********/
    //BIS기본편성 조회 및 등록
    /*@Scheduled(cron = "0 * * * * *")*/
    //@Scheduled(cron = "0 0 5 * * ?")//매일 새벽 5시에 한번씩
    /*public void bisBasicScheduleCreate() throws Exception {

        //Bis에서 기본편성 조회
        BisBasicScheduleDTO bisBasicScheduleDTO = bisInterfaceService.bisBasicSchedulefindAll();

        bisInterfaceService.matchBasicScheduleCreate(bisBasicScheduleDTO);

    }*/

    //Bis주간편성 조회 및 등록
    //@Scheduled(cron = "0 * * * * *")
    @Scheduled(cron = "0 15 5 * * ?")//매일 새벽 5시10분에 한번씩
    //@Scheduled(cron = "* * 3 * * ?")//3시간에 한번씩
    public void bisDailyScheduleCreate() throws Exception {

        log.info("Dailly Program Create Start");

        //bis에서 프로그램 정보를 가져온다.
        BisProgramDTO bisProgramDTO = bisInterfaceService.bisProgramfindAll();

        BisDailyScheduleDTO bisDailyScheduleDTO = bisInterfaceService.bisDailyScheduleFindAll();

        bisInterfaceService.bisDailyScheduleCreate(bisDailyScheduleDTO, bisProgramDTO);

    }

    //주간편성 금요일 새벽 5시마다 업데이트
    @Scheduled(cron = "0 0 23 ? * FRI")//매주 금요일 새벽 5시에 한번씩
    //@Scheduled(cron = "0 * * * * *")
    //@Scheduled(cron = "0 10 5 * * ?")//매일 새벽 5시10분에 한번씩
    public void bisDailyScheduleCreateFri() throws Exception {

        log.info("Dailly Program Week Create Start");

        //bis에서 프로그램 정보를 가져온다.
        BisProgramDTO bisProgramDTO = bisInterfaceService.bisProgramfindAll();

        List<BisDailyScheduleDTO> bisDailyScheduleDTOList = bisInterfaceService.bisDailyScheduleFindAllFri();

        bisInterfaceService.bisDailyScheduleCreateFri(bisDailyScheduleDTOList, bisProgramDTO);

    }

    //Mis 부서 조회 및 등록(수정)
    //@Scheduled(cron = "10 * * * * *")
    @Scheduled(cron = "0 0 5 * * ?")//매일 새벽 5시에 한번씩
    public void misDeptScheduled(){

        log.info("Mis Dept Create Start");

        misService.findMisDept();

    }

    //Mis 사용자 조회 및 등록(수정)
    //@Scheduled(cron = "0 * * * * *")
    @Scheduled(cron = "0 10 5 * * ?")//매일 새벽 5시에 한번씩
    public void misUserScheduled() throws NoSuchAlgorithmException {

        log.info("Mis User Create Start");

        misService.findUser();

    }

    //@Scheduled(cron = "0 * * * * *")
    @Scheduled(cron = "0 0 0/1 * * *")//한시간마다
    public void cueSheetLockScheduled(){

        cueSheetService.cueSheetLockChk();

    }

    //@Scheduled(cron = "30 * * * * *")
    @Scheduled(cron = "0 0 0/1 * * *")//한시간마다
    public void articleLockScheduled(){

        articleService.articleLockChk();

    }

}
