package com.gemiso.zodiac.app.auth;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AuthRepositoryImpl implements AuthRepositoryCustorm{

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public AuthRepositoryImpl(JPAQueryFactory jpaQueryFactory){

        this.jpaQueryFactory = jpaQueryFactory;
    }


    @Override
    public List<Auth> findAllUserType(String userId, String userLoginTyp) {

        QAuth qAuth = QAuth.auth;

        JPAQuery jpaQuery = jpaQueryFactory.select(qAuth).from(qAuth).distinct();

        if (userId != null && userId.trim().isEmpty() == false){
            jpaQuery.where(qAuth.userId.eq(userId));
        }

        if (userLoginTyp != null && userLoginTyp.trim().isEmpty() == false){
            jpaQuery.where(qAuth.userLoginTyp.eq(userLoginTyp));
        }

        jpaQuery.orderBy(qAuth.loginDtm.desc());


        return jpaQuery.fetch();
    }
}
