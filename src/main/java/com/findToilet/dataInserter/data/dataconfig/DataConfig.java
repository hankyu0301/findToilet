package com.findToilet.dataInserter.data.dataconfig;

import com.findToilet.dataInserter.data.util.type.DatabaseType;
import com.findToilet.dataInserter.data.util.type.DomainType;
import com.findToilet.dataInserter.data.util.type.FileType;

import java.util.Map;
import java.util.function.Consumer;

public interface DataConfig {
    String getFilePath();

    FileType getFileType();

    Map<String, String> getFileColumnToDatabaseColumnMap();

    DomainType getTargetClass();

    Consumer<Map<String, String>> getMapConsumer();

    Map<String, DatabaseType> getDatabaseColumnTypeMap();

    Boolean isTypeOf(FileType fileType);
}