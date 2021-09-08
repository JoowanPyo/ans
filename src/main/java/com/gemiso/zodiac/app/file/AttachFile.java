package com.gemiso.zodiac.app.file;

import com.gemiso.zodiac.app.user.UserGroupUser;
import com.gemiso.zodiac.app.yonhap.YonhapAttchFile;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_strg_file",
        uniqueConstraints = {
                @UniqueConstraint(name = "file_fileId_unique", columnNames = "file_id")
        })
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
public class AttachFile {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "file_id", nullable = false)
        private Long fileId;

        @Column(name = "file_div_cd", length = 3)
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

        @Column(name = "inputr_id", length = 50)
        private String inputrId;


        @OneToMany(mappedBy="attachFile")
        private List<YonhapAttchFile> yonhapAttchFiles = new ArrayList<>();

        @PrePersist
        public void prePersist() {

                if(this.delYn == null || this.delYn == ""){
                        this.delYn = "N";
                }
        }
}
