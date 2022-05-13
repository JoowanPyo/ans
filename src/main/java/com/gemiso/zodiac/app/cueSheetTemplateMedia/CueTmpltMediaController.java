package com.gemiso.zodiac.app.cueSheetTemplateMedia;

import com.gemiso.zodiac.app.cueSheetTemplateMedia.dto.CueTmpltMediaCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateMedia.dto.CueTmpltMediaDTO;
import com.gemiso.zodiac.app.cueSheetTemplateMedia.dto.CueTmpltMediaUpdateDTO;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.service.UserAuthService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(description = "큐시트 템플릿 미디어 API")
@RestController
@RequestMapping("/cuetmpltmedia")
@Slf4j
@RequiredArgsConstructor
public class CueTmpltMediaController {

    private final CueTmpltMediaService cueTmpltMediaService;

    private final UserAuthService userAuthService;


    @Operation(summary = "큐시트 템플릿 미디어 목록조회", description = "큐시트 템플릿 미디어 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<CueTmpltMediaDTO>> findAll(@Parameter(description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                         @Parameter(description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                         @Parameter(name = "trnsfFileNm", description = "전송 파일 명")
                                                         @RequestParam(value = "trnsfFileNm", required = false) String trnsfFileNm,
                                                         @Parameter(name = "cueTmpltMediaId", description = "큐시트 템플릿 미디어 아이디")
                                                         @RequestParam(value = "cueTmpltMediaId", required = false) Long cueTmpltMediaId,
                                                         @Parameter(name = "mediaTypCd", description = "미디어 유형 코드[media_typ_001 : 영상, media_typ_002 : 백드롭]")
                                                         @RequestParam(value = "mediaTypCd", required = false) String mediaTypCd) throws Exception {

        List<CueTmpltMediaDTO> cueTmpltMediaDTOList = new ArrayList<>();

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 23:59:59)
            SearchDate searchDate = new SearchDate(sdate, edate);

            cueTmpltMediaDTOList = cueTmpltMediaService.findAll(searchDate.getStartDate(), searchDate.getEndDate(), trnsfFileNm, cueTmpltMediaId, mediaTypCd);

        } else {
            cueTmpltMediaDTOList = cueTmpltMediaService.findAll(null, null, trnsfFileNm, cueTmpltMediaId, mediaTypCd);
        }
        return new AnsApiResponse<>(cueTmpltMediaDTOList);
    }

    @Operation(summary = "큐시트 템플릿 미디어 상세조회", description = "큐시트 템플릿 미디어 상세조회")
    @GetMapping(path = "/{cueTmpltMediaId}")
    public AnsApiResponse<CueTmpltMediaDTO> find(@Parameter(name = "cueTmpltMediaId", description = "큐시트 템플릿 미디어 아이디")
                                                @PathVariable("cueTmpltMediaId") Long artclMediaId) {

        CueTmpltMediaDTO cueTmpltMediaDTO = cueTmpltMediaService.find(artclMediaId);

        return new AnsApiResponse<>(cueTmpltMediaDTO);
    }

    @Operation(summary = "큐시트 템플릿 미디어 등록", description = "큐시트 템플릿 미디어 등록")
    @PostMapping(path = "")
    public AnsApiResponse<CueTmpltMediaDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                                  @RequestBody @Valid CueTmpltMediaCreateDTO cueTmpltMediaCreateDTO) {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();

        log.info(" Create CueSheet Template Media : User Id - "+userId+" Media Model -"+cueTmpltMediaCreateDTO.toString());

        CueTmpltMediaDTO cueTmpltMediaDTO = cueTmpltMediaService.create(cueTmpltMediaCreateDTO, userId);

        return new AnsApiResponse<>(cueTmpltMediaDTO);
    }

    @Operation(summary = "큐시트 템플릿 미디어 수정", description = "큐시트 템플릿 미디어 수정")
    @PutMapping(path = "/{cueTmpltMediaId}")
    public AnsApiResponse<CueTmpltMediaDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                                  @RequestBody @Valid CueTmpltMediaUpdateDTO cueTmpltMediaUpdateDTO,
                                                  @Parameter(name = "cueTmpltMediaId", description = "큐시트 템플릿 미디어 아이디")
                                                  @PathVariable("cueTmpltMediaId") Long cueTmpltMediaId) {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        log.info(" Update CueSheet Template Media : User Id - "+userId+" Media Model -"+cueTmpltMediaUpdateDTO.toString());

        CueTmpltMediaDTO cueTmpltMediaDTO = cueTmpltMediaService.update(cueTmpltMediaUpdateDTO, cueTmpltMediaId, userId);

        return new AnsApiResponse<>(cueTmpltMediaDTO);

    }

    @Operation(summary = "큐시트 템플릿 미디어 삭제", description = "큐시트 템플릿 미디어 삭제")
    @DeleteMapping(path = "/{cueTmpltMediaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "cueTmpltMediaId", description = "큐시트 템플릿 미디어 아이디")
                                    @PathVariable("cueTmpltMediaId") Long cueTmpltMediaId) {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        log.info(" Delete ueSheet Template Media : User Id - "+userId+" Media Id -"+cueTmpltMediaId);


        cueTmpltMediaService.delete(cueTmpltMediaId, userId);

        return AnsApiResponse.noContent();
    }
}
