package com.gemiso.zodiac.core.response;

import com.fasterxml.jackson.annotation.JsonValue;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.gemiso.zodiac.exception.UserFailException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * Api 에러 응답
 *
 * https://github.com/gemiso-dev/dev-docs/blob/main/standards/restful-api-standard.md#error-handling
 * {
 *   "status": 422,
 *   "success": false,
 *   "error": {
 *     "codeDTO": "validation_exception",
 *     "message": "입력값이 유효하지 않습니다.",
 *     "errors": {
 *       "search": ["search 필드는 필수입니다."]
 *     }
 *   }
 * }
 */
@Getter
public class ApiErrorResponse extends BaseApiResponse {
    /**
     * 에러코드 열거형
     */
    public enum ErrorCodes {
        InvalidArguments("invalid_input_values"),
        ResourceNotFound("resource_not_found"),
        NoHandler("no_handler"),
        InternalServerError("internal_server_error"),
        expiredAccesstoken("Expired_accesstoken");

        @JsonValue
        private final String errorCode;

        ErrorCodes(String errorCode) {
            this.errorCode = errorCode;
        }

        @Override
        public String toString() {
            return errorCode;
        }
    }

    /**
     * 에러 클래스
     */
    @Getter
    public static class Error {
        private final ErrorCodes code;
        private final String message;
        private HashMap<String, List<String>> errors = new HashMap<>();

        public Error(ErrorCodes code, String message) {
             this.code = code;
            this.message = message;
        }

        public Error(ErrorCodes code, String message, HashMap<String, List<String>> errors) {
            this.code = code;
            this.message = message;
            this.errors = errors;
        }

        public void addError(String key, List<String> values) {
            this.errors.put(key, values);
        }
    }

    /**
     * 여기부터 필드 시작
     */
    HttpStatus status;
    boolean success;
    private final Error error;

    public ApiErrorResponse(Error error) {
        this.error = error;
        /*this.status = HttpStatus.OK;
        this.success = true;*/
    }

    public ApiErrorResponse(Error error, HttpStatus status) {
         this.error = error;
        this.status = status;
        this.success = false;
    }

    /**
     * 유효성 오류 응답 객체 생성
     * @param exception ConstraintViolationException 객체
     * @return ApiErrorResponse
     */
    public static ApiErrorResponse makeValidationErrorResponse(ConstraintViolationException exception) {
        HashMap<String, List<String>> errors = getValidationErrors(exception.getConstraintViolations().iterator());
        Error error = new Error(ErrorCodes.InvalidArguments, "입력 값이 유효하지 않습니다.", errors);

        return new ApiErrorResponse(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * 유효성 오류 응답 객체 생성
     * @param exception ConstraintViolationException 객체
     * @return ApiErrorResponse
     */
    public static ApiErrorResponse makeValidationErrorResponse(MethodArgumentNotValidException exception) {

        BindingResult bindingResult = exception.getBindingResult();

        HashMap<String, List<String>> errors = getValidationErrors(bindingResult.getFieldErrors());
        Error error = new Error(ErrorCodes.InvalidArguments, "입력 값이 유효하지 않습니다.", errors);

        return new ApiErrorResponse(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public static ApiErrorResponse makeNoHandlerErrorResponse(NoHandlerFoundException exception) {
        Error error = new Error(ErrorCodes.NoHandler, exception.getLocalizedMessage(), null);

        return new ApiErrorResponse(error, HttpStatus.NOT_FOUND);
    }

    public static ApiErrorResponse makeResourceNotFoundResponse(ResourceNotFoundException exception) {
        Error error = new Error(ErrorCodes.ResourceNotFound, exception.getLocalizedMessage(), null);

        return new ApiErrorResponse(error, HttpStatus.NOT_FOUND);
    }

    public static ApiErrorResponse makeUserFailResponse(UserFailException exception) {
        Error error = new Error(ErrorCodes.InternalServerError, exception.getLocalizedMessage(), null);

        return new ApiErrorResponse(error, HttpStatus.FORBIDDEN);
    }

    protected static HashMap<String, List<String>> getValidationErrors(List<FieldError> fieldErrors) {
        HashMap<String, List<String>> errors = new HashMap<>();

        for (FieldError fieldError : fieldErrors) {
            String fieldName = fieldError.getField();
            String message = fieldError.getDefaultMessage() + ", given : " + fieldError.getRejectedValue();

            List<String> messages = errors.get(fieldName);
            if(messages != null) {
                messages.add(message);
            } else {
                errors.put(fieldName, Arrays.asList(message));
            }
        }

        return errors;
    }

    protected static HashMap<String, List<String>> getValidationErrors(final Iterator<ConstraintViolation<?>> violationIterator) {
        HashMap<String, List<String>> errors = new HashMap<>();
        while (violationIterator.hasNext()) {
            final ConstraintViolation<?> constraintViolation = violationIterator.next();
            String propertyName = getPropertyName(constraintViolation.getPropertyPath().toString());
            String message = constraintViolation.getMessage() + ", given : " + constraintViolation.getInvalidValue();

            List<String> messages = errors.get(propertyName);
            if(messages != null) {
                messages.add(message);
            } else {
                errors.put(propertyName, Arrays.asList(message));
            }
        }

        return errors;
    }

    protected static String getPropertyName(String propertyPath) {
        return propertyPath.substring(propertyPath.lastIndexOf('.') + 1); // 전체 속성 경로에서 속성 이름만 가져온다.
    }
}
