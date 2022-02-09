package com.gemiso.zodiac.app.yonhapAttchFile;

import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.yonhap.Yonhap;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(
        name = "tb_yonhap_attc_file"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@DynamicUpdate
public class YonhapAttchFile {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "file_id")
    private AttachFile attachFile;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "yh_artcl_id")
    private Yonhap yonhap;

    @Column(name = "file_ord")
    private int fileOrd;

    @Column(name = "file_titl", length = 200)
    private String fileTitl;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "capTemplate", length = 4000)
    private String cap;

    @Column(name = "yh_url", length = 2000)
    private String yhUrl;
}
