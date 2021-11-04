package com.gemiso.zodiac.app.articleOrder;

import com.gemiso.zodiac.app.articleOrder.dto.*;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(description = "기사의뢰 첨부파일 API")
@RestController
@RequestMapping("/orderfile")
@Slf4j
@RequiredArgsConstructor
public class ArticleOrderFileController {

    private final ArticleOrderFileService articleOrderFileService;


    @Operation(summary = "기사의뢰 첨부파일 목록조회", description = "기사의뢰 첨부파일 목록조회")
    @GetMapping(path = "")
    public ApiResponse<List<ArticleOrderFileDTO>> findAll(@Parameter(name = "orderId", description = "의뢰 아이디")
                                                          @RequestParam(value = "orderId", required = false) Long orderId) {

        List<ArticleOrderFileDTO> articleOrderFileDTOList = articleOrderFileService.findAll(orderId);

        return new ApiResponse<>(articleOrderFileDTOList);
    }

    @Operation(summary = "기사의뢰 첨부파일 상세조회", description = "기사의뢰 첨부파일 상세조회")
    @GetMapping(path = "/{id}")
    public ApiResponse<ArticleOrderFileDTO> find(@Parameter(name = "id", required = true, description = "의뢰 첨부파일 아이디")
                                                 @PathVariable("id") long id) {

        ArticleOrderFileDTO articleOrderFileDTO = articleOrderFileService.find(id);

        return new ApiResponse<>(articleOrderFileDTO);
    }

    @Operation(summary = "기사의뢰 첨부파일 등록", description = "기사의뢰 첨부파일 등록")
    @PostMapping(path = "/{orderId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ArticleOrderFileResponseDTO> create(@Parameter(name = "orderId", required = true, description = "의뢰 첨부파일 아이디")
                                                           @PathVariable("orderId") long orderId,
                                                           @Parameter(description = "필수값<br>", required = true)
                                                           @RequestBody @Valid ArticleOrderFileCreateDTO articleOrderFileCreateDTO) {

        ArticleOrderFileResponseDTO responseDTO = new ArticleOrderFileResponseDTO();

        Long id = articleOrderFileService.create(articleOrderFileCreateDTO, orderId);

        responseDTO.setId(id);

        return new ApiResponse<>(responseDTO);
    }

    @Operation(summary = "기사의뢰 첨부파일 수정", description = "기사의뢰 첨부파일 수정")
    @PutMapping(path = "/{orderId}")
    public ApiResponse<ArticleOrderFileResponseDTO> update(@Parameter(name = "orderId", required = true, description = "의뢰 첨부파일 아이디")
                                                           @PathVariable("orderId") long orderId,
                                                           @Parameter(description = "필수값<br>", required = true)
                                                           @RequestBody @Valid ArticleOrderFileUpdateDTO articleOrderFileUpdateDTO) {

        ArticleOrderFileResponseDTO responseDTO = new ArticleOrderFileResponseDTO();

        articleOrderFileService.update(orderId, articleOrderFileUpdateDTO);

        responseDTO.setId(orderId);

        return new ApiResponse<>(responseDTO);
    }

    @Operation(summary = "기사의뢰 첨부파일 삭제", description = "기사의뢰 첨부파일 삭제")
    @DeleteMapping(path = "/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> delete(@Parameter(name = "orderId", required = true, description = "의뢰 첨부파일 아이디")
                                 @PathVariable("orderId") long orderId) {

        articleOrderFileService.delete(orderId);

        return ApiResponse.noContent();
    }
}
