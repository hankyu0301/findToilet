package com.findToilet.dataInserter.data;

import com.findToilet.dataInserter.data.dataconfig.producer.ConfigProducer;
import com.findToilet.dataInserter.data.dataloader.QueryRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DataInserter implements CommandLineRunner {

    private final QueryRunner queryRunner;
    private final List<ConfigProducer> configProducerList;

    @Override
    public void run(String... args) {
       /* for (ConfigProducer configProducer : configProducerList) {
            DataSet dataSet = configProducer.getDataSet();
            String queryString = dataSet.getInsertQuery();
            queryRunner.runQuery(queryString);
        }*/
    }
}
