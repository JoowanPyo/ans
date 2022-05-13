package com.gemiso.zodiac.app.cueCapTmplt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gemiso.zodiac.app.capTemplate.CapTemplate;
import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.app.user.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tb_cue_cap_tmplt")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"capTemplate","program"})
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CueCapTmplt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "cg_div_cd", length = 50)
    private String cgDivCd;

    /*@NotFound(
           action = NotFoundAction.IGNORE)*/
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cap_tmplt_id")
    private CapTemplate capTemplate;

    /*@NotFound(
           action = NotFoundAction.IGNORE)*/
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brdc_pgm_id")
    private Program program;
}
