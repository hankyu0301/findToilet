package com.findToilet.dataInserter.data.dataloader;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@Transactional
public class QueryRunner {

    @PersistenceContext
    EntityManager em;

    public void runQuery(String queryString) {
        int changeNum = excuteQuery(queryString);
        System.out.printf("총 %d개의 데이터가 업데이트 되었습니다.%n", changeNum);
    }

    int excuteQuery(String insertQuery) {
        em.joinTransaction();
        return em.createNativeQuery(insertQuery).executeUpdate();
    }
}