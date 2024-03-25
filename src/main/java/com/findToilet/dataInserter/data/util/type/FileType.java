package com.findToilet.dataInserter.data.util.type;

public enum FileType {
    JSON, XML, XLSX;

    public static FileType getFileType(String extension) {
        switch (extension) {
            case "json" -> {
                return FileType.JSON;
            }
            case "xml" -> {
                return FileType.XML;
            }
            case "xlsx" -> {
                return FileType.XLSX;
            }
        }
        throw new RuntimeException("확장자가 잘못되었습니다.");
    }
}