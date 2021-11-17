package com.gemiso.zodiac.app.articleMedia;


import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaCreateDTO;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaDTO;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaUpdateDTO;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.response.ApiResponse;
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

@Api(description = "기사 미디어 API")
@RestController
@RequestMapping("/articlemedia")
@Slf4j
@RequiredArgsConstructor
public class ArticleMediaController {

    private final ArticleMediaService articleMediaService;


    @Operation(summary = "기사 미디어 목록조회", description = "기사 미디어 목록조회")
    @GetMapping(path = "")
    public ApiResponse<List<ArticleMediaDTO>> findAll(@Parameter(description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                      @Parameter(description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                      @Parameter(name = "trnsfFileNm", description = "전송 파일 명")
                                                      @RequestParam(value = "trnsfFileNm", required = false) String trnsfFileNm) throws Exception {

        List<ArticleMediaDTO> articleMediaDTOList = new ArrayList<>();

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            //검색날짜 시간설정 (검색시작 Date = yyyy-MM-dd 00:00:00 / 검색종료 Date yyyy-MM-dd 23:59:59)
            SearchDate searchDate = new SearchDate(sdate, edate);

            articleMediaDTOList = articleMediaService.findAll(searchDate.getStartDate(), searchDate.getEndDate(), trnsfFileNm);

        }else {
            articleMediaDTOList = articleMediaService.findAll(null, null, trnsfFileNm);
        }
        return new ApiResponse<>(articleMediaDTOList);
    }

    @Operation(summary = "기사 미디어 상세조회", description = "기사 미디어 상세조회")
    @GetMapping(path = "/{artclMediaId}")
    public ApiResponse<ArticleMediaDTO> find(@Parameter(name = "artclMediaId", description = "기사미디어 아이디")
                                             @PathVariable("artclMediaId") Long artclMediaId) {

        ArticleMediaDTO articleMediaDTO = articleMediaService.find(artclMediaId);

        return new ApiResponse<>(articleMediaDTO);
    }

    @Operation(summary = "기사 미디어 등록", description = "기사 미디어 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ArticleMediaDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                               @RequestBody @Valid ArticleMediaCreateDTO articleMediaCreateDTO) {

        Long artclMediaId = articleMediaService.create(articleMediaCreateDTO);

        //기사영상 등록 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        ArticleMediaDTO articleMediaDTO = new ArticleMediaDTO();
        articleMediaDTO.setArtclMediaId(artclMediaId);

        return new ApiResponse<>(articleMediaDTO);
    }

    @Operation(summary = "기사 미디어 수정", description = "기사 미디어 수정")
    @PutMapping(path = "/{artclMediaId}")
    public ApiResponse<ArticleMediaDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                               @RequestBody @Valid ArticleMediaUpdateDTO articleMediaUpdateDTO,
                                               @Parameter(name = "artclMediaId", description = "기사미디어 아이디")
                                               @PathVariable("artclMediaId") Long artclMediaId) {

        articleMediaService.update(articleMediaUpdateDTO, artclMediaId);

        //기사영상 수정 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        ArticleMediaDTO articleMediaDTO = new ArticleMediaDTO();
        articleMediaDTO.setArtclMediaId(artclMediaId);

        return new ApiResponse<>(articleMediaDTO);

    }

    @Operation(summary = "기사 미디어 삭제", description = "기사 미디어 삭제")
    @DeleteMapping(path = "/{artclMediaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> delete(@Parameter(name = "artclMediaId", description = "기사미디어 아이디")
                                 @PathVariable("artclMediaId") Long artclMediaId) {

        articleMediaService.delete(artclMediaId);

        return ApiResponse.noContent();
    }

}
