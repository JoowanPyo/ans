package com.gemiso.zodiac.exception;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.core.response.ApiErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.security.InvalidParameterException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    // 유효성 검사 실패 시 발생하는 예외를 처리
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    protected ResponseEntity<ApiErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.makeValidationErrorResponse(exception);
        log.error(apiErrorResponse.toString());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
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
        log.error(" ResourceNotFoundException : "+ ApiErrorResponse.makeResourceNotFoundResponse(ex));
        return new ResponseEntity<>(ApiErrorResponse.makeResourceNotFoundResponse(ex), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = UserFailException.class)
    public ResponseEntity<Object> handleApiRequestException(UserFailException ex) {
        log.error(" UserFailException : "+ ApiErrorResponse.makeUserFailResponse(ex));
        return new ResponseEntity<>(ApiErrorResponse.makeUserFailResponse(ex), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        log.error(" Exception : "+ ApiErrorResponse.makeExceptionHandlerErrorResponse(ex));

        ApiErrorResponse.Error error =
                new ApiErrorResponse.Error(ApiErrorResponse.ErrorCodes.InternalServerError, ex.getMessage());
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(error, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {JsonProcessingException.class})
    public ResponseEntity<Object> handleJsonProcessingException(HttpServletRequest request, JsonProcessingException e) {

        log.error(" JsonProcessingException : "+ApiErrorResponse.makeJonsFailResponse(e));

        return new ResponseEntity<>(ApiErrorResponse.makeJonsFailResponse(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { ExpiredJwtException.class })
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex){
        ApiErrorResponse.Error error =
                new ApiErrorResponse.Error(ApiErrorResponse.ErrorCodes.expiredAccesstoken, ex.getLocalizedMessage());
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(error, HttpStatus.UNAUTHORIZED);


        log.error(" ExpiredJwtException : "+ apiErrorResponse.toString());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.UNAUTHORIZED);
    }

   /* @ExceptionHandler(value = { ServletException.class })
    public ResponseEntity<Object> handleServletException(ServletException ex){
        ApiErrorResponse.Error error =
                new ApiErrorResponse.Error(ApiErrorResponse.ErrorCodes.expiredAccesstoken, ex.getLocalizedMessage());
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(error, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.UNAUTHORIZED);
    }*/

   /* //vaiid Exception Handeler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex){

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError)c).getField(), c.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }*/

}