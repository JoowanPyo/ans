package com.gemiso.zodiac.app.ArticleOrder;

import com.gemiso.zodiac.app.ArticleOrder.dto.ArticleOrderCreateDTO;
import com.gemiso.zodiac.app.ArticleOrder.dto.ArticleOrderDTO;
import com.gemiso.zodiac.app.ArticleOrder.dto.ArticleOrderUpdateDTO;
import com.gemiso.zodiac.core.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(description = "기사 의뢰 API")
@RestController
@RequestMapping("/order")
@Slf4j
@RequiredArgsConstructor
public class ArticleOrderController {

    private final ArticleOrderService articleOrderService;


    @Operation(summary = "기사의뢰 상세조회", description = "기사의뢰 상세조회")
    @GetMapping(path = "/{orderId}")
    public ApiResponse<ArticleOrderDTO> find(@Parameter(name = "orderId", required = true, description = "의뢰 아이디")
                                             @PathVariable("orderId") long orderId) {

        ArticleOrderDTO articleOrderDTO = articleOrderService.find(orderId);

        return new ApiResponse<>(articleOrderDTO);
    }

    @Operation(summary = "기사의뢰 등록", description = "기사의뢰 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ArticleOrderDTO> create(@Parameter(description = "필수값<br>", required = true)
                                               @RequestBody @Valid ArticleOrderCreateDTO articleOrderCreateDTO) {

        Long orderId = articleOrderService.create(articleOrderCreateDTO);

        ArticleOrderDTO articleOrderDTO = articleOrderService.find(orderId);

        return new ApiResponse<>(articleOrderDTO);
    }

    @Operation(summary = "기사의뢰 수정", description = "기사의뢰 수정")
    @PutMapping(path = "/{orderId}")
    public ApiResponse<ArticleOrderDTO> update(@Parameter(description = "필수값<br>", required = true)
                                               @RequestBody @Valid ArticleOrderUpdateDTO articleOrderUpdateDTO,
                                               @Parameter(name = "orderId", required = true, description = "의뢰 아이디")
                                               @PathVariable("orderId") long orderId) {

        articleOrderService.update(articleOrderUpdateDTO, orderId);

        ArticleOrderDTO articleOrderDTO = articleOrderService.find(orderId);

        return new ApiResponse<>(articleOrderDTO);
    }

    //삭제처리 API가 없는듯.
  /*  @Operation(summary = "기사의뢰 삭제", description = "기사의뢰 삭제")
    @DeleteMapping(path = "/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<?> delete(@Parameter(name = "orderId", required = true, description = "의뢰 아이디")
                                 @PathVariable("orderId") long orderId) {

        articleOrderService.delete(orderId);

        return ApiResponse.noContent();
    }*/


}
