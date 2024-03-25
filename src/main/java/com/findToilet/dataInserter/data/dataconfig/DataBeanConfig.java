package com.findToilet.dataInserter.data.dataconfig;

import com.findToilet.dataInserter.data.dataconfig.producer.ConfigProducer;
import com.findToilet.dataInserter.data.dataconfig.producer.XlsxConfigProducer;
import com.findToilet.global.util.ReverseGeocoding.ChangeByGeocoderKakao;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class DataBeanConfig {

    private final ChangeByGeocoderKakao changeByGeocoderKakao;

    @Bean
    List<ConfigProducer> configProducerList(ChangeByGeocoderKakao changeByGeocoderKakao) {
        List<ConfigProducer> configList = new ArrayList<>();

        configList.add(new XlsxConfigProducer(changeByGeocoderKakao, "testData.xlsx"));

        return configList;
    }
}
