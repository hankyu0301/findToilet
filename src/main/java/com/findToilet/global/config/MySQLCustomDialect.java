package com.findToilet.global.config;

import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class MySQLCustomDialect extends MySQL8Dialect {

    /*
    WHERE 절과 ORDER BY 절은 데이터베이스 쿼리의 조건을 필터링하거나 정렬하는 데 사용.
    이들 절에서 사용되는 함수는 일반적으로 데이터베이스 서버에서 처리 되기 때문에 JPA에서 사용하는 JPQL에서는 사용할 수 없다.
    Select 절에서 특정 함수를 사용하려면 Hibernate의 Dialect에 해당 함수가 등록되어 있어야 한다.
    */
    public MySQLCustomDialect() {
        super();
        this.registerFunction("ST_Distance_Sphere",
                new StandardSQLFunction("ST_Distance_Sphere", StandardBasicTypes.DOUBLE));
    }
}
