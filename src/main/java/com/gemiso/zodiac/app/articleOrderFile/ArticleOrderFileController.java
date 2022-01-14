package com.gemiso.zodiac.app.articleOrderFile;

import com.gemiso.zodiac.app.articleOrderFile.dto.ArticleOrderFileCreateDTO;
import com.gemiso.zodiac.app.articleOrderFile.dto.ArticleOrderFileDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
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
    public AnsApiResponse<List<ArticleOrderFileDTO>> findAll(@Parameter(name = "orderId", description = "의뢰 아이디")
                                                          @RequestParam(value = "orderId", required = false) Long orderId) {

        List<ArticleOrderFileDTO> articleOrderFileDTOList = articleOrderFileService.findAll(orderId);

        return new AnsApiResponse<>(articleOrderFileDTOList);
    }

    @Operation(summary = "기사의뢰 첨부파일 상세조회", description = "기사의뢰 첨부파일 상세조회")
    @GetMapping(path = "/{id}")
    public AnsApiResponse<ArticleOrderFileDTO> find(@Parameter(name = "id", required = true, description = "의뢰 첨부파일 아이디")
                                                 @PathVariable("id") long id) {

        ArticleOrderFileDTO articleOrderFileDTO = articleOrderFileService.find(id);

        return new AnsApiResponse<>(articleOrderFileDTO);
    }

    @Operation(summary = "기사의뢰 첨부파일 등록", description = "기사의뢰 첨부파일 등록")
    @PostMapping(path = "/{orderId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<List<ArticleOrderFileDTO>> create(@Parameter(name = "orderId", required = true, description = "의뢰 첨부파일 아이디")
                                                           @PathVariable("orderId") long orderId,
                                                            @Parameter(description = "필수값<br>", required = true)
                                                           @RequestBody @Valid List<ArticleOrderFileCreateDTO> articleOrderFileCreateDTOList) {

        //오더 파일등록하고 오더아이디로 등록된 파일리스트 조회 하기위해 오더아이디 response
        /*ArticleOrderResponseDTO responseDTO = new ArticleOrderResponseDTO();*/

        articleOrderFileService.create(articleOrderFileCreateDTOList, orderId);

        List<ArticleOrderFileDTO> articleOrderFileDTOList = articleOrderFileService.findAll(orderId);

        return new AnsApiResponse<>(articleOrderFileDTOList);
    }

    @Operation(summary = "기사의뢰 첨부파일 수정", description = "기사의뢰 첨부파일 수정")
    @PutMapping(path = "/{orderId}")
    public AnsApiResponse<List<ArticleOrderFileDTO>> update(@Parameter(name = "orderId", required = true, description = "의뢰 첨부파일 아이디")
                                                           @PathVariable("orderId") long orderId,
                                                            @Parameter(description = "필수값<br>", required = true)
                                                           @RequestBody @Valid List<ArticleOrderFileCreateDTO> articleOrderFileCreateDTOList) {

        //오더 파일등록하고 오더아이디로 등록된 파일리스트 조회 하기위해 오더아이디 response
       /* ArticleOrderResponseDTO responseDTO = new ArticleOrderResponseDTO();*/

        articleOrderFileService.update(orderId, articleOrderFileCreateDTOList);

        List<ArticleOrderFileDTO> articleOrderFileDTOList = articleOrderFileService.findAll(orderId);

        return new AnsApiResponse<>(articleOrderFileDTOList);
    }

    @Operation(summary = "기사의뢰 첨부파일 삭제", description = "기사의뢰 첨부파일 삭제")
    @DeleteMapping(path = "/{orderFileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "orderFileId", required = true, description = "의뢰 첨부파일 아이디")
                                 @PathVariable("orderFileId") long orderFileId) {

        articleOrderFileService.delete(orderFileId);

        return AnsApiResponse.noContent();
    }
}
