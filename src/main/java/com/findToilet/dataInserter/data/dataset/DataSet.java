package com.findToilet.dataInserter.data.dataset;

import com.findToilet.dataInserter.data.dataconfig.DataConfig;

import java.util.Map;
import java.util.function.Consumer;

public interface DataSet {
    void setDataSetList(DataConfig dataConfig, Consumer<Map<String, String>> consumer);

    String getInsertQuery();
}
