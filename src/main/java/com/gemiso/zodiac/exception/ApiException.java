package com.gemiso.zodiac.exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
@Data
@Getter
public class ApiException{


        private final String message;
        private final Throwable throwable;
        private final HttpStatus httpStatus;
        private final ZonedDateTime zonedDateTime;


}
