package com.gemiso.zodiac.app.dictionary;

import com.gemiso.zodiac.app.dictionary.dto.DictionaryCreateDTO;
import com.gemiso.zodiac.app.dictionary.dto.DictionaryDTO;
import com.gemiso.zodiac.app.dictionary.dto.DictionaryUpdateDTO;
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

@Api(description = "단어사전 API")
@RestController
@RequestMapping("/dictionary")
@Slf4j
@RequiredArgsConstructor
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @Operation(summary = "단어사전 목록조회", description = "단어사전 목록조회")
    @GetMapping(path = "")
    public ApiResponse<List<DictionaryDTO>> findAll(@Parameter(name = "sdate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                    @Parameter(name = "edate", description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                    @Parameter(name = "searchWord", description = "검색키워드")
                                                    @RequestParam(value = "searchWord", required = false) String searchWord) throws Exception {

        List<DictionaryDTO> dictionaryDTOList = new ArrayList<>();

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {

            SearchDate searchDate = new SearchDate(sdate, edate);

            dictionaryDTOList = dictionaryService.findAll(searchDate.getStartDate(), searchDate.getEndDate(), searchWord);

        } else {
            dictionaryDTOList = dictionaryService.findAll(null, null, searchWord);
        }

        return new ApiResponse<>(dictionaryDTOList);
    }

    @Operation(summary = "단어사전 상세조회", description = "단어사전 상세조회")
    @GetMapping(path = "/{id}")
    public ApiResponse<DictionaryDTO> find(@Parameter(name = "id", required = true, description = "단어사전 아이디")
                                           @PathVariable("id") long id) {

        DictionaryDTO dictionaryDTO = dictionaryService.find(id);

        return new ApiResponse<>(dictionaryDTO);
    }

    @Operation(summary = "단어사전 등록", description = "단어사전 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<DictionaryDTO> create(@Parameter(description = "필수값<br> ", required = true)
                                             @RequestBody @Valid DictionaryCreateDTO dictionaryCreateDTO) {

        Long id = dictionaryService.create(dictionaryCreateDTO);

        DictionaryDTO dictionaryDTO = dictionaryService.find(id);

        return new ApiResponse<>(dictionaryDTO);
    }

    @Operation(summary = "단어사전 수정", description = "단어사전 수정")
    @PutMapping(path = "/{id}")
    public ApiResponse<DictionaryDTO> update(@Parameter(description = "필수값<br> ", required = true)
                                             @RequestBody @Valid DictionaryUpdateDTO dictionaryUpdateDTO,
                                             @Parameter(name = "id", required = true, description = "단어사전 아이디")
                                             @PathVariable("id") long id) {

        dictionaryService.update(dictionaryUpdateDTO, id);

        DictionaryDTO dictionaryDTO = dictionaryService.find(id);

        return new ApiResponse<>(dictionaryDTO);

    }

    @Operation(summary = "단어사전 삭제", description = "단어사전 삭제")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> delete(@Parameter(name = "id", required = true, description = "단어사전 아이디")
                                 @PathVariable("id") long id) {

        dictionaryService.delete(id);

        return ApiResponse.noContent();
    }

}
