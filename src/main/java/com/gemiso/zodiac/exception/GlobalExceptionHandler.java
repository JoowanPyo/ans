package com.gemiso.zodiac.exception;


import com.gemiso.zodiac.core.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.ZonedDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    // 유효성 검사 실패 시 발생하는 예외를 처리
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    protected ResponseEntity<ApiErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        return new ResponseEntity<>(ApiErrorResponse.makeValidationErrorResponse(exception), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * validation 에러  (@RequestBody)
     * @param ex MethodArgumentNotValidException
     * @return ResponseEntity<ApiErrorResponse>
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(ApiErrorResponse.makeValidationErrorResponse(ex), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(ApiErrorResponse.makeNoHandlerErrorResponse(ex), HttpStatus.UNPROCESSABLE_ENTITY);

    }

    /**
     * 이건 그냥 테스트용
     * @param ex
     * @return
     */
    @ExceptionHandler(value = ApiRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException ex) {
        ApiException apiException = new ApiException(
                ex.getMessage(),
                ex,
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> handleApiRequestException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ApiErrorResponse.makeResourceNotFoundResponse(ex), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ApiErrorResponse.Error error =
                new ApiErrorResponse.Error(ApiErrorResponse.ErrorCodes.InternalServerError, ex.getLocalizedMessage());
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(error, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}