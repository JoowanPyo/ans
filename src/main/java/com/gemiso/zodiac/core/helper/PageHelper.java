package com.gemiso.zodiac.core.helper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

public class PageHelper {

    private Integer page;
    private Integer limit;

    public PageHelper(Integer page, Integer limit) {

        if (ObjectUtils.isEmpty(page) && ObjectUtils.isEmpty(limit)) {

            this.page = Optional.ofNullable(page).orElse(0);
            this.limit = Optional.ofNullable(limit).orElse(50);

        } else if (ObjectUtils.isEmpty(page) == false && ObjectUtils.isEmpty(limit) == false) {

            if (page > 0 && limit > 0) {
                this.page = page - 1;
                this.limit = limit;
            }else if( page > 0 && limit == 0 ){

                this.page = page - 1;
                this.limit = Optional.ofNullable(limit).orElse(50);
            }else if (page == 0 && limit > 0){

                this.page = Optional.ofNullable(page).orElse(0);
                this.limit = Optional.ofNullable(limit).orElse(50);

            }else if (page == 0 && limit == 0){

                this.page = Optional.ofNullable(page).orElse(0);
                this.limit = Optional.ofNullable(limit).orElse(50);
            }


        } else if (ObjectUtils.isEmpty(page) == false || ObjectUtils.isEmpty(limit) == false) {

            if (ObjectUtils.isEmpty(page) == false && page > 0){

                this.page = page - 1;
                this.limit = Optional.ofNullable(limit).orElse(50);
            }else {

                this.page = Optional.ofNullable(page).orElse(0);
                this.limit = Optional.ofNullable(limit).orElse(50);

            }

        }
    }

    public Pageable getArticlePageInfo() {
        return PageRequest.of(page, limit, Sort.by("artclId", "inputDtm").descending());
    }

    public Pageable getArticleOrderPageInfo() {
        return PageRequest.of(page, limit, Sort.by("inputDtm").descending());
    }

    public Pageable getTakerCue() {
        return PageRequest.of(page, limit, Sort.by("cueId").descending());
    }

    public Pageable getYonhapPage() {
        return PageRequest.of(page, limit);
    }
}
