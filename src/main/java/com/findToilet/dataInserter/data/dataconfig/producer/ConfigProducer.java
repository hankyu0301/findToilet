package com.findToilet.dataInserter.data.dataconfig.producer;

import com.findToilet.dataInserter.data.dataconfig.DataConfig;
import com.findToilet.dataInserter.data.dataset.DataSet;

public interface ConfigProducer {
    DataConfig getDataConfig();

    DataSet getDataSet();
}