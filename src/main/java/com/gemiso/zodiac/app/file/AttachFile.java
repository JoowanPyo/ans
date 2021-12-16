package com.gemiso.zodiac.app.file;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_strg_file")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //json 파싱때 필드가 없는 클래스를 Serialize문제 설정
public class AttachFile {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "file_id", nullable = false)
        private Long fileId;

        @Column(name = "file_div_cd", length = 50)
        private String fileDivCd;

        @Column(name = "file_nm", length = 600)
        private String fileNm;

        @Column(name = "file_expl", length = 1000)
        private String fileExpl;

        @Column(name = "file_loc", length = 1000)
        private String fileLoc;

        @Column(name = "file_size", length = 10)
        private Integer fileSize;

        @Column(name = "file_upld_dtm")
        private Date fileUpldDtm;

        @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'")
        private String delYn;

        @Column(name = "input_dtm")
        private Date inputDtm;

        @Column(name = "org_file_nm", length = 600)
        private String orgFileNm;

        @Column(name = "inputr_id", nullable = false, length = 50)
        private String inputrId;

        @Basic(fetch = FetchType.LAZY)
        @Formula("(select a.user_nm from tb_user_mng a where a.user_id = inputr_id)")
        private String updtrNm;


        /*@OneToMany(mappedBy="attachFile")
        private List<YonhapAttchFile> yonhapAttchFiles;*/

        /*@OneToMany(mappedBy="attachFile")
        private List<Symbol> symbol;*/


        @PrePersist
        public void prePersist() {

                if(this.delYn == null || this.delYn == ""){
                        this.delYn = "N";
                }
        }
}
