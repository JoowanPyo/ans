package com.gemiso.zodiac.app.ytn;

import com.gemiso.zodiac.app.yonhapPhoto.dto.YonhapExceptionDomain;
import com.gemiso.zodiac.app.ytn.dto.*;
import com.gemiso.zodiac.app.ytn.mapper.YtnRundownMapper;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class YtnRundownService {

    private final YtnRundownRepository ytnRundownRepository;

    private final YtnRundownMapper ytnRundownMapper;

    //Ytn 목록조회
    public List<YtnRundownDTO> findAll(Date sdate, Date edate, String contId, String reporterId){

        //조회조건 빌드
        BooleanBuilder booleanBuilder = getSearch(sdate, edate, contId, reporterId);

        //order by 정령조건 생성[ ASC 방송일시, DESC 방송시작시간]
        List<Sort.Order> orders = new ArrayList<>();
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "ContId");
        orders.add(order1);
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "ord");
        orders.add(order2);

        List<YtnRundown> ytnRundownList = (List<YtnRundown>) ytnRundownRepository.findAll(booleanBuilder, Sort.by(orders));

        List<YtnRundownDTO> ytnRundownDTOList = ytnRundownMapper.toDtoList(ytnRundownList);

        return ytnRundownDTOList;
    }

    //검색목록 조건 빌드
    public BooleanBuilder getSearch(Date sdate, Date edate, String contId, String reporterId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QYtnRundown qYtnRundown = QYtnRundown.ytnRundown;

        //날짜 조회조건
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false){
            booleanBuilder.and(qYtnRundown.inputDtm.between(sdate, edate));
        }

        //콘텐츠 아이디 조회조건
        if (contId != null && contId.trim().isEmpty() == false){
            booleanBuilder.and(qYtnRundown.contId.eq(contId));
        }

        //기자아디디 조회조건
        if (reporterId != null && reporterId.trim().isEmpty() == false){
            booleanBuilder.and(qYtnRundown.rprt.eq(reporterId));
        }

        return booleanBuilder;
    }

    public YonhapYtnExceptionDomain create(YtnRundownCreateDTO ytnRundownCreateDTO){

        String contId = ytnRundownCreateDTO.getContId();

        List<YtnRundown> ytnRundownList = ytnRundownRepository.findByYtn(contId);

        if (CollectionUtils.isEmpty(ytnRundownList) == false){

            for (YtnRundown ytnRundown : ytnRundownList){ //기존데이터 삭제후 재등록
                Long id = ytnRundown.getId();
                ytnRundownRepository.deleteById(id);
            }
            List<YtnRowDTO> rowDTOList = ytnRundownCreateDTO.getRowList();

            for (YtnRowDTO ytnRowDTO: rowDTOList) {

                YtnRundown ytnRundown = YtnRundown.builder()
                        .contId(ytnRundownCreateDTO.getContId())
                        .brdcDtm(ytnRundownCreateDTO.getBrdcDate())
                        .brdcStartDtm(ytnRundownCreateDTO.getStartTime())
                        .brdcEndDtm(ytnRundownCreateDTO.getEndTime())
                        .ord(ytnRowDTO.getNO())
                        .frm(ytnRowDTO.getFORM())
                        .mc(ytnRowDTO.getMC())
                        .rprt(ytnRowDTO.getREPORTER())
                        .video(ytnRowDTO.getVIDEO_CNT())
                        .time(ytnRowDTO.getTIME())
                        .article(ytnRowDTO.getARTICLE())
                        .title(ytnRowDTO.getTITLE())
                        .build();

                try {
                    ytnRundownRepository.save(ytnRundown);
                }catch (RuntimeException e){
                    return new YonhapYtnExceptionDomain(null, "5001", "yonhapReuter", "RuntimeException", "");
                }
            }
        }else {

            List<YtnRowDTO> rowDTOList = ytnRundownCreateDTO.getRowList();

            for (YtnRowDTO ytnRowDTO: rowDTOList) {

                YtnRundown ytnRundown = YtnRundown.builder()
                        .contId(ytnRundownCreateDTO.getContId())
                        .brdcDtm(ytnRundownCreateDTO.getBrdcDate())
                        .brdcStartDtm(ytnRundownCreateDTO.getStartTime())
                        .brdcEndDtm(ytnRundownCreateDTO.getEndTime())
                        .ord(ytnRowDTO.getNO())
                        .frm(ytnRowDTO.getFORM())
                        .mc(ytnRowDTO.getMC())
                        .rprt(ytnRowDTO.getREPORTER())
                        .video(ytnRowDTO.getVIDEO_CNT())
                        .time(ytnRowDTO.getTIME())
                        .article(ytnRowDTO.getARTICLE())
                        .title(ytnRowDTO.getTITLE())
                        .build();

                try {
                    ytnRundownRepository.save(ytnRundown);
                }catch (RuntimeException e){
                    return new YonhapYtnExceptionDomain(null, "5001", "yonhapReuter", "RuntimeException", "");
                }
            }



        }
        return new YonhapYtnExceptionDomain(contId, "2000", "yonhapPhoto", "", "");
    }
    
    //Ytn 에이전트에 리턴할 데이터를 빌드
    public YtnRundownResponseDTO formatYtn(List<YtnRundownDTO> ytnRundownDTOList){

        YtnRundownDTO ytnRundownDTO = ytnRundownDTOList.get(0);
        List<YtnRowDTO> ytnRowDTOS = new ArrayList<>(); //리스폰스모델에 넣을 row List 생성

        //1개에 데이터만 필요하기 때문에 ytnRowDTO 1개 생성후 리스트에 add
        YtnRowDTO ytnRowDTO = YtnRowDTO.builder()
                .NO(ytnRundownDTO.getOrd())
                .MC(ytnRundownDTO.getMc())
                .TITLE(ytnRundownDTO.getTitle())
                .FORM(ytnRundownDTO.getFrm())
                .REPORTER(ytnRundownDTO.getRprt())
                .VIDEO_CNT(ytnRundownDTO.getVideo())
                .TIME(ytnRundownDTO.getTime())
                .ARTICLE(ytnRundownDTO.getArticle())
                .build();

        ytnRowDTOS.add(ytnRowDTO);

        //ytn 리스폰스 데이터 빌드
        YtnRundownResponseDTO ytnRundownResponseDTO = YtnRundownResponseDTO.builder()
                .yhArtclId(ytnRundownDTO.getId())
                //.agcyCd()
                .brdcDate(ytnRundownDTO.getBrdcDtm())
                .startTime(ytnRundownDTO.getBrdcStartDtm())
                .endTime(ytnRundownDTO.getBrdcEndDtm())
                .rowList(ytnRowDTOS)
                .build();

        return ytnRundownResponseDTO; //ytn리스폰트 데이터 리턴
    }


    public String intToString(int value){

        String returnString = "";

        if (ObjectUtils.isEmpty(value) == false){

            returnString = Integer.toString(value);

        }
        return returnString;
    }

    public int stringToInt(String str){

        int returnValue = 0;

        if (str != null && str.trim().isEmpty() == false){
            returnValue = Integer.parseInt(str);
        }

        return returnValue;
    }
}
