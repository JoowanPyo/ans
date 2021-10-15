package com.gemiso.zodiac.core.helper;

import io.swagger.models.auth.In;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class PageHelper {

    private Integer page;
    private Integer limit;

    public PageHelper(Integer page, Integer limit){
        this.page = Optional.ofNullable(page).orElse(0);
        this.limit = Optional.ofNullable(limit).orElse(50);
    }

    public Pageable getPageInfo(){

        return PageRequest.of(page, limit, Sort.by("artclId","inputDtm").descending());
    }
}
