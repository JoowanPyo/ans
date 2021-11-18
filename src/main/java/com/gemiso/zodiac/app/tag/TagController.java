package com.gemiso.zodiac.app.tag;

import com.gemiso.zodiac.app.tag.dto.TagCreateDTO;
import com.gemiso.zodiac.app.tag.dto.TagDTO;
import com.gemiso.zodiac.app.tag.dto.TagIdDTO;
import com.gemiso.zodiac.app.tag.dto.TagUpdateDTO;
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

//@Tag(name = "User Controllers", description = "User API")
@Api(description = "테그 API")
@RestController
@RequestMapping("/tags")
@Slf4j
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;


    @Operation(summary = "테그 목록조회", description = "테그 목록조회")
    @GetMapping(path = "")
    public AnsApiResponse<List<TagDTO>> findAll(@Parameter(name = "tagName", description = "테그명")
                                             @RequestParam(value = "tagName", required = false) String tagName) {

        List<TagDTO> tagDTOList = tagService.findAll(tagName);

        return new AnsApiResponse<>(tagDTOList);

    }

    @Operation(summary = "테그 상세조회", description = "테그 상세조회")
    @GetMapping(path = "/{tagId}")
    public AnsApiResponse<TagDTO> find(@Parameter(name = "tagId", required = true, description = "테그아이디")
                                    @PathVariable("tagId") Long tagId) {

        TagDTO tagDTO = tagService.find(tagId);

        return new AnsApiResponse<>(tagDTO);
    }

    @Operation(summary = "테그 등록", description = "테그 등록")
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<TagIdDTO> create(@Parameter(name = "tagCreateDTO", required = true, description = "테그 생성 DTO")
                                        @Valid @RequestBody TagCreateDTO tagCreateDTO) {

        TagIdDTO tagDTO = new TagIdDTO();

        Long tagId = tagService.create(tagCreateDTO);
        tagDTO.setTagId(tagId);

        return new AnsApiResponse<>(tagDTO);
    }

    @Operation(summary = "테그 수정", description = "테그 수정")
    @PutMapping(path = "/{tagId}")
    public AnsApiResponse<TagIdDTO> update(@Parameter(name = "tagUpdateDTO", required = true, description = "테그 수정 DTO")
                                        @Valid @RequestBody TagUpdateDTO tagUpdateDTO,
                                           @Parameter(name = "tagId", required = true, description = "테그아이디")
                                        @PathVariable("tagId") Long tagId) {

        TagIdDTO tagIdDTO = new TagIdDTO();

        tagService.update(tagUpdateDTO, tagId);
        tagIdDTO.setTagId(tagId);

        return new AnsApiResponse<>(tagIdDTO);
    }

    @Operation(summary = "테그 삭제", description = "테그 삭제")
    @DeleteMapping(path = "/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AnsApiResponse<?> delete(@Parameter(name = "tagId", required = true, description = "테그아이디")
                                 @PathVariable("tagId") Long tagId) {

        tagService.delete(tagId);

        return AnsApiResponse.noContent();
    }
}
