package com.gemiso.zodiac.app.dept;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_depts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
@EntityListeners(value = {AuditingEntityListener.class})
public class Depts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "code", length = 50)
    private String code;

    @Column(name = "is_enabled", columnDefinition = "bpchar(1) default 'N'")
    private String isEnabled;

    @CreatedDate
    @Column( name = "input_dtm", updatable = false)
    private Date inputDtm;

    @LastModifiedDate
    @Column(name = "updt_dtm")
    private Date updtDtm;

    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "parent_code", length = 50)
    private String parentCode;

    @Column(name = "id2")
    private Long id2;

}
