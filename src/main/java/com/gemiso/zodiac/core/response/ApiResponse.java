package com.gemiso.zodiac.core.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * 단일 객체 Api 응답
 * @param <T> 리소스 객체
 *
 * {
 *   "status": 200,
 *   "success": true,
 *   "data": {
 *     "id": 1,
 *     "user_id": "admin",
 *     "name": "관리자"
 *   }
 * }
 */
@Data
public class ApiResponse<T> extends BaseApiResponse {
    private final T data;

    public ApiResponse(T data) {
        this.data = data;
        this.status = HttpStatus.OK;
        this.success = true;
    }

    public ApiResponse(T data, HttpStatus status) {
        this.data = data;
        this.status = status;
        this.success = true;
    }

    public static ApiResponse ok() {
        return new ApiResponse(null, HttpStatus.OK);
    }
    public static ApiResponse noContent() {
        return new ApiResponse(null, HttpStatus.NO_CONTENT);
    }

}
