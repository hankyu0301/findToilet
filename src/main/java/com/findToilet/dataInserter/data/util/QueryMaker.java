package com.findToilet.dataInserter.data.util;

import com.findToilet.dataInserter.data.util.type.DatabaseType;
import com.findToilet.dataInserter.data.util.type.DomainType;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class QueryMaker {

    public static String makeInsertQuery(List<Map<String, String>> insertTargetMap, DomainType domainType,
                                         Map<String, DatabaseType> databaseColumnTypeMap) {
        assert insertTargetMap.isEmpty() : "데이터가 존재하지 않거나, 잘못되었습니다.";
        String insertQuery = "INSERT INTO " + domainType.toString() + "(";
        Map<String, String> map = insertTargetMap.get(0);
        Set<String> set = map.keySet();
        insertQuery += String.join(",", set) + ") values ";
        insertQuery += insertTargetMap.stream()
                .map((m) -> {
                    String collect = set.stream().map((s) -> {
                        String data = m.get(s);
                        System.out.println(s + " : " + data);
                        DatabaseType type = databaseColumnTypeMap.get(s);
                        assert type == null : "databaseColumnTypeMap에 빠진 것이 있습니다";
                        return type.getDatabaseFormat(data);
                    }).collect(Collectors.joining(","));
                    return "(" + collect + ")";
                })
                .collect(Collectors.joining(","));

        return insertQuery;
    }
}