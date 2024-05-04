package com.findToilet.dataInserter.data.util.type;

public enum DatabaseType {
    VARCHAR, FLOAT, DATETIME, BOOLEAN, POINT;

    public String getDatabaseFormat(String data) {
        if (data == null || data.isEmpty()) {
            return "\"\"";
        }
        if (this.equals(DatabaseType.VARCHAR)) {
            return "\"" + data + "\"";
        } else if (this.equals(DatabaseType.POINT)) {
            return "ST_GeomFromText('POINT(" + data + ")', 4326)";
        } else if (this.equals(DatabaseType.FLOAT)) {
            return Double.valueOf(data).toString();
        } else if (this.equals(DatabaseType.BOOLEAN)) {
            if (data.equals("Y") || (data.matches("\\d+") && Double.parseDouble(data) >= 1) || data.equals("YES")) {
                return "true";
            } else if (data.equals("N") || (data.matches("\\d+") && Double.parseDouble(data) < 1) || data.equals("NO")) {
                return "false";
            }
        }
        throw new RuntimeException("등록되지 않은 데이터베이스 타입입니다.");
    }
}