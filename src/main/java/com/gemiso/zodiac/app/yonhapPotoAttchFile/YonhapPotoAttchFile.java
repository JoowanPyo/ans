package com.gemiso.zodiac.app.yonhapPotoAttchFile;

import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.app.yonhapPoto.YonhapPoto;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(
        name = "tb_yonhap_photo_attc_file"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "userGroupUser")
@Setter
@DynamicUpdate
public class YonhapPotoAttchFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "file_ord")
    private int fileOrd;

    @Column(name = "file_typ_cd", length = 50)
    private String fileTypCd;

    @Column(name = "mime_typ", length = 100)
    private String mimeTyp;

    @Column(name = "yonhap_url", length = 2000)
    private String yonhapUrl;

    @Column(name = "expl", length = 2000)
    private String expl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "file_id")
    private AttachFile attachFile;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "yonhap_artcl_id")
    private YonhapPoto yonhapPoto;

}
