package com.gemiso.zodiac.app.breakingNewsFtpInfo;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(
        name = "tb_breaking_news_ftp_info"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@DynamicUpdate
public class BreakingNewsFtpInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "ftp_ip", length = 100)
    private String ftpIp;

    @Column(name = "ftp_port", length = 100)
    private Integer ftpPort;

    @Column(name = "ftp_id", length = 100)
    private String ftpId;

    @Column(name = "ftp_pw", length = 100)
    private String ftpPw;

    @Column(name = "ftp_path", length = 100)
    private String ftpPath;

    @Column(name = "ftp_type", length = 100)
    private String ftpType;
}
