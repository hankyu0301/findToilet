package com.findToilet.dataInserter.data.dataset;

import com.findToilet.dataInserter.data.dataconfig.DataConfig;
import com.findToilet.dataInserter.data.reader.XlsxReader;
import com.findToilet.dataInserter.data.util.InsertTargetListMaker;
import com.findToilet.dataInserter.data.util.QueryMaker;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class XlsxDataSet implements DataSet {

    private List<Map<String, String>> insertTargetList;
    private DataConfig dataConfig;

    public XlsxDataSet(DataConfig dataConfig) {
        this.dataConfig = dataConfig;
        setDataSetList(dataConfig, dataConfig.getMapConsumer());
    }

    @Override
    public void setDataSetList(DataConfig dataConfig, Consumer<Map<String, String>> consumer) {
        insertTargetList = InsertTargetListMaker.setInsertTargetList(dataConfig, consumer,
                new XlsxReader(dataConfig.getFilePath(), dataConfig.getFileColumnToDatabaseColumnMap()));
    }

    @Override
    public String getInsertQuery() {
        return QueryMaker.makeInsertQuery(insertTargetList, dataConfig.getTargetClass(),
                dataConfig.getDatabaseColumnTypeMap());
    }
}