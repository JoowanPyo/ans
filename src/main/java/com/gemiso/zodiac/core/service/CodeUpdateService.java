package com.gemiso.zodiac.core.service;

import com.gemiso.zodiac.app.code.Code;
import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
public class CodeUpdateService {

    
    public Boolean codeUpdateCheck(Code code, CodeSimpleDTO codeSimpleDTO){

        //수정할 코드가 빈값이면 수정할게 없기 때문에 false 리턴
        if (ObjectUtils.isEmpty(codeSimpleDTO)){
            return false;
        }
        
        //코드네임을 불러올때 null포인트 에러가 날수 있기 때문에 Optional로 처리,
        //둘다 ""로 처리하게되면 같은값으로 예외처리가 날 수 있기 때문에 "","null"로 처리
        String orgCodeName = Optional.ofNullable(code.getCdNm()).orElse("");
        String newCodeName = Optional.ofNullable(codeSimpleDTO.getCdNm()).orElse("null");

        if (orgCodeName.equals(newCodeName)){
            return false;
        }

        return true;
        
    }

 
}
