package com.gemiso.zodiac.app.cueSheetItemSymbol;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.symbol.Symbol;
import com.gemiso.zodiac.app.user.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tb_cue_item_symbol")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude ={"cueSheetItem", "symbol"})
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CueSheetItemSymbol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /*@NotFound(
           action = NotFoundAction.IGNORE)*/
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cue_item_id")
    @JsonBackReference
    private CueSheetItem cueSheetItem;

    /*@NotFound(
           action = NotFoundAction.IGNORE)*/
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "symbol_id")
    private Symbol symbol;

    @Column(name = "ord", nullable = false)
    private int ord;

}
