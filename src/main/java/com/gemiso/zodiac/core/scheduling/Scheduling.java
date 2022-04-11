package com.gemiso.zodiac.core.scheduling;

import com.gemiso.zodiac.core.mis.MisDept;
import com.gemiso.zodiac.core.mis.MisService;
import com.gemiso.zodiac.core.scheduling.dto.BisBasicScheduleDTO;
import com.gemiso.zodiac.core.scheduling.dto.BisDailyScheduleDTO;
import com.gemiso.zodiac.core.scheduling.dto.BisProgramDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class Scheduling {

    private final BisInterfaceService bisInterfaceService;
    private final MisService misService;

    //BIS프로그램 조회 및 등록
    @Scheduled(cron = "* * 3 * * ?")//3시간에 한번씩
    /*@Scheduled(cron = "0 * * * * *")*/
    public void bisProgramCreate() throws Exception {

        //bis에서 프로그램 정보를 가져온다.
        BisProgramDTO bisProgramDTO = bisInterfaceService.bisProgramfindAll();

        //현재 등록되어있는 ANS프로그램 정보와 bis에서 불러온 프로그램 정보를 비교하여 새로들어온 프로그램 정보를 업데이트 해준다.
        bisInterfaceService.matchProgramCreate(bisProgramDTO);

    }

    //BIS기본편성 조회 및 등록
    /*@Scheduled(cron = "0 * * * * *")*/
    @Scheduled(cron = "* * 3 * * ?") //3시간에 한번씩
    public void bisBasicScheduleCreate() throws Exception {

        //Bis에서 기본편성 조회
        BisBasicScheduleDTO bisBasicScheduleDTO = bisInterfaceService.bisBasicSchedulefindAll();

        bisInterfaceService.matchBasicScheduleCreate(bisBasicScheduleDTO);

    }

    //Bis주간편성 조회 및 등록
    /*@Scheduled(cron = "0 * * * * *")*/
    @Scheduled(cron = "* * 3 * * ?")//3시간에 한번씩
    public void bisDailyScheduleCreate() throws Exception {

        BisDailyScheduleDTO bisDailyScheduleDTO = bisInterfaceService.bisDailyScheduleFindAll();

        bisInterfaceService.bisDailyScheduleCreate(bisDailyScheduleDTO);

    }

    //Mis 부서 조회 및 등록(수정)
    //@Scheduled(cron = "0 * * * * *")
    @Scheduled(cron = "* * 3 * * ?")//3시간에 한번씩
    public void misDeptScheduled(){

        misService.findMisDept();

    }

}
