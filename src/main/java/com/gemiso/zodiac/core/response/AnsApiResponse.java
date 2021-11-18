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
public class AnsApiResponse<T> /*extends BaseApiResponse*/ {
    private final HttpStatus status;
    private final boolean success;
    private final T data;


    public AnsApiResponse(T data) {
        this.data = data;
        this.status = HttpStatus.OK;
        this.success = true;
    }

    public AnsApiResponse(T data, HttpStatus status) {
        this.data = data;
        this.status = status;
        this.success = true;
    }

    public static AnsApiResponse ok() {
        return new AnsApiResponse(null, HttpStatus.OK);
    }
    public static AnsApiResponse noContent() {
        return new AnsApiResponse(null, HttpStatus.NO_CONTENT);
    }

}
