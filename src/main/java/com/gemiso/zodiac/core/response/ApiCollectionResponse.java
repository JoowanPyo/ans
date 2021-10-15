package com.gemiso.zodiac.core.response;

import com.gemiso.zodiac.core.page.PageResultDTO;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * 컬렉션 Api 응답
 * @param <T> 리소스 객체
 *
 * {
 *     "status": 200,
 *     "success": true,
 *     "data": [
 *         {
 *             "id": 1,
 *         "user_id": "admin",
 *             "name": "관리자"
 *         },{
 *             "id": "2",
 *             "user_id": "bread",
 *             "name": "브래드"
 *         },
 *         ...
 *     ],
 *     "pagination": {
 *         "count": 2,
 *         "total": 102,
 *         "perPage": 100,
 *         "currentPage": 2,
 *         "totalPages": 2,
 *         "links": {
 *             "previous": "http://proxima.test/admin/users?page=1"
 *         }
 *     }
 * }
 */
@Data
public class ApiCollectionResponse<T> extends BaseApiResponse {
   // private final List<T> data;
    private final PageResultDTO page;

    public ApiCollectionResponse(/*List<T> data,*/ PageResultDTO page) {
        //this.data = data;
        this.page = page;
        this.status = HttpStatus.OK;
        this.success = true;
    }
}
