package com.gemiso.zodiac.app.program;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProgramRepositoryImpl implements ProgramRepositoryCustorm{

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public ProgramRepositoryImpl(JPAQueryFactory jpaQueryFactory){
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Program> findByProgram(String brdcPgmNm, String useYn) {

        QProgram qProgram = QProgram.program;


        JPAQuery jpaQuery = jpaQueryFactory.select(qProgram).from(qProgram).distinct();

        jpaQuery.where(qProgram.delYn.eq("N"));

        if (brdcPgmNm != null && brdcPgmNm.trim().isEmpty() == false) {
            jpaQuery.where(qProgram.brdcPgmNm.contains(brdcPgmNm));
        }

        if (useYn != null && useYn.trim().isEmpty() == false){
            jpaQuery.where(qProgram.useYn.eq(useYn));
        }

        jpaQuery.orderBy(qProgram.inputDtm.asc());

        return jpaQuery.fetch();
    }
}
