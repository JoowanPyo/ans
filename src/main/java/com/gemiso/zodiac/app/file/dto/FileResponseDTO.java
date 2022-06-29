package com.gemiso.zodiac.app.file.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.core.Response;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDTO {

    private Response response;
    private String fileNm;
    private String url;
}
