package com.gemiso.zodiac.app.file;

import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.file.dto.StatusCodeFileDTO;
import com.gemiso.zodiac.core.response.AnsApiResponse;
import com.gemiso.zodiac.core.service.JwtGetUserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

@Api(description = "파일 API")
@RestController
@RequestMapping("/files")
@Slf4j
@RequiredArgsConstructor
public class AttachFileController {

    private final AttachFileService attachFileService;

    private final JwtGetUserService jwtGetUserService;

    @Operation(summary = "파일 업로드", description = "파일 업로드")
    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<StatusCodeFileDTO> create(@RequestPart MultipartFile file, @RequestParam String fileDivCd,
                                                    @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        StatusCodeFileDTO statusCodeFileDTO = attachFileService.create(file, fileDivCd, userId);

        return new AnsApiResponse<>(statusCodeFileDTO);
    }

    @Operation(summary = "파일 업로드[ 외부 ]", description = "파일 업로드[ 외부 ]")
    @PostMapping(path = "/interface", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AnsApiResponse<StatusCodeFileDTO> createFile(@RequestPart MultipartFile file, @RequestParam String fileDivCd, @RequestParam String fileNm,
                                                        @RequestHeader(value = "Authorization", required = false) String Authorization) throws Exception {

        String userId = jwtGetUserService.getUser(Authorization);

        StatusCodeFileDTO statusCodeFileDTO = attachFileService.createFile(file, fileDivCd, fileNm, userId);

        return new AnsApiResponse<>(statusCodeFileDTO);
    }


    @Operation(summary = "파일 다운로드", description = "파일 다운로드")
    @GetMapping(path = "/{fileId}")
    public ResponseEntity find(HttpServletRequest request, HttpServletResponse response,
                               @Parameter(name = "fileId", description = "파일 아이디") @PathVariable Long fileId,
                               @Parameter(name = "fileDivCd", description = "파일 구분코드")
                               @RequestParam(value = "fileDivCd", required = false) String fileDivCd) throws Exception {

        ResponseEntity rep = null;
        AttachFileDTO attachFileDTO = attachFileService.strFilefind(fileId);

        if (attachFileDTO.getFileDivCd().equals(fileDivCd)) {
            rep = attachFileService.find(request, response, attachFileDTO, fileDivCd);
        } else {

            String errorMessage = "File does not exist";
            OutputStream outputStream = response.getOutputStream();
            try {
                outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            } catch (IOException e) {
                log.error("파일 다운로드 에러 : IOException ");
            } finally {

                if (outputStream != null) {
                    outputStream.close();
                }
            }

            return null;
        }

        return rep;

    }

}
