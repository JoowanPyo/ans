package com.gemiso.zodiac.core.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public class BaseEntity {

    @CreatedDate
    @Column( name = "input_dtm", updatable = false)
    private Date inputDtm;

    @LastModifiedDate
    @Column(name = "updt_dtm")
    private Date updtDtm;


}