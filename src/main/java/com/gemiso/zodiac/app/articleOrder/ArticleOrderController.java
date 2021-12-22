package com.gemiso.zodiac.app.articleOrder;

import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderCreateDTO;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderDTO;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderResponseDTO;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderUpdateDTO;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.response.AnsApiResponse;
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

@Api(description = "기사 의뢰 API")
@RestController
@RequestMapping("/orders")
@Slf4j
@RequiredArgsConstructor
public class ArticleOrderController {

    private final ArticleOrderService articleOrderService;


    @Operation(summary = "기사의뢰 목록조회", description = "기사의뢰 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<ArticleOrderDTO>> findAll(@Parameter(name = "sdate", description = "검색 시작 데이터 날짜(yyyy-MM-dd)", required = false)
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date sdate,
                                                         @Parameter(name = "edate", description = "검색 종료 날짜(yyyy-MM-dd)", required = false)
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date edate,
                                                         @Parameter(name = "orderDivCd", description = "의뢰 구분 코드(분류)")
                                                         @RequestParam(value = "orderDivCd", required = false) String orderDivCd,
                                                         @Parameter(name = "orderStatus", description = "의뢰 상태")
                                                         @RequestParam(value = "orderStatus", required = false) String orderStatus,
                                                         @Parameter(name = "workrId", description = "작업자 아이디")
                                                         @RequestParam(value = "workrId", required = false) String workrId,
                                                         @Parameter(name = "inputrId", description = "등록자 아이디")
                                                         @RequestParam(value = "inputrId", required = false) String inputrId,
                                                         @Parameter(name = "artclId", description = "기사 아이디")
                                                         @RequestParam(value = "artclId", required = false) Long artclId) throws Exception {

        List<ArticleOrderDTO> articleOrderDTOList = new ArrayList<>();

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {

            //날짜 파싱 startDate yyyy-MM-dd 00:00:00 , endDate yyyy-MM-dd 24:00:00
            SearchDate searchDate = new SearchDate(sdate, edate);

            articleOrderDTOList = articleOrderService.findAll(searchDate.getStartDate(), searchDate.getEndDate(),
                    orderDivCd, orderStatus, workrId, inputrId, artclId);
        } else {

            articleOrderDTOList = articleOrderService.findAll(null, null,
                    orderDivCd, orderStatus, workrId, inputrId, artclId);
        }

        return new AnsApiResponse<>(articleOrderDTOList);
    }

    @Operation(summary = "기사의뢰 상세조회", description = "기사의뢰 상세조회")
    @GetMapping(path = "/{orderId}")
    public AnsApiResponse<ArticleOrderDTO> find(@Parameter(name = "orderId", required = true, description = "의뢰 아이디")
                                                @PathVariable("orderId") long orderId) {

        ArticleOrderDTO articleOrderDTO = articleOrderService.find(orderId);

        return new AnsApiResponse<>(articleOrderDTO);
    }

    @Operation(summary = "기사의뢰 등록", description = "기사의뢰 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<ArticleOrderResponseDTO> create(@Parameter(description = "필수값<br>", required = true)
                                                          @RequestBody @Valid ArticleOrderCreateDTO articleOrderCreateDTO) {

        ArticleOrderResponseDTO responseDTO = new ArticleOrderResponseDTO();

        Long orderId = articleOrderService.create(articleOrderCreateDTO);

        responseDTO.setOrderId(orderId); //Id set[ response =  id]

        return new AnsApiResponse<>(responseDTO);
    }

    @Operation(summary = "기사의뢰 수정", description = "기사의뢰 수정")
    @PutMapping(path = "/{orderId}")
    public AnsApiResponse<ArticleOrderResponseDTO> update(@Parameter(description = "필수값<br>", required = true)
                                                          @RequestBody @Valid ArticleOrderUpdateDTO articleOrderUpdateDTO,
                                                          @Parameter(name = "orderId", required = true, description = "의뢰 아이디")
                                                          @PathVariable("orderId") long orderId) {

        ArticleOrderResponseDTO responseDTO = new ArticleOrderResponseDTO();

        articleOrderService.update(articleOrderUpdateDTO, orderId);

        responseDTO.setOrderId(orderId); //Id set[ response =  id]

        return new AnsApiResponse<>(responseDTO);
    }

    //삭제처리 API가 없는듯.
    @Operation(summary = "기사의뢰 삭제", description = "기사의뢰 삭제")
    @DeleteMapping(path = "/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "orderId", required = true, description = "의뢰 아이디")
                                    @PathVariable("orderId") long orderId) {

        articleOrderService.delete(orderId);

        return AnsApiResponse.noContent();
    }


}
