package com.findToilet.dataInserter.data.dataconfig.producer;

import com.findToilet.dataInserter.data.dataconfig.DataConfig;
import com.findToilet.dataInserter.data.dataconfig.ToiletConfig;
import com.findToilet.dataInserter.data.dataset.DataSet;
import com.findToilet.dataInserter.data.dataset.JsonDataSet;
import com.findToilet.dataInserter.data.util.type.DatabaseType;
import com.findToilet.dataInserter.data.util.type.DomainType;
import com.findToilet.global.dto.AddressInfoDto;
import com.findToilet.global.util.ReverseGeocoding.ChangeByGeocoderKakao;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class JsonConfigProducer implements ConfigProducer {

    private final ChangeByGeocoderKakao changeByGeocoderKakao;
    private final String fileName;

    @Override
    public DataConfig getDataConfig() {
        return ToiletConfig.builder()
                .fileName(fileName)
                .changeMap(makeToiletMap())
                .domainType(DomainType.toilet)
                .databaseColumnTypeMap(makeDatabaseTypeMap())
                .mapConsumer(makeMapConsumer())
                .build();
    }

    @Override
    public DataSet getDataSet() {
        return new JsonDataSet(getDataConfig());
    }

    private Consumer<Map<String, String>> makeMapConsumer() {
        return (map) -> {
            AddressInfoDto address = changeByGeocoderKakao.getCoordinateByAddress(map.get("address"));
            map.put("road_address", address.getRoad_address());
        };
    }

    private Map<String, DatabaseType> makeDatabaseTypeMap() {
        Map<String, DatabaseType> databaseTypeMap = new HashMap<>();

        databaseTypeMap.put("name", DatabaseType.VARCHAR);
        databaseTypeMap.put("road_address", DatabaseType.VARCHAR);
        databaseTypeMap.put("address", DatabaseType.VARCHAR);
        databaseTypeMap.put("latitude", DatabaseType.FLOAT);
        databaseTypeMap.put("longitude", DatabaseType.FLOAT);
        databaseTypeMap.put("male_disabled", DatabaseType.BOOLEAN);
        databaseTypeMap.put("female_disabled", DatabaseType.BOOLEAN);
        databaseTypeMap.put("male_kids", DatabaseType.BOOLEAN);
        databaseTypeMap.put("female_kids", DatabaseType.BOOLEAN);
        databaseTypeMap.put("diaper", DatabaseType.BOOLEAN);
        databaseTypeMap.put("operation_time", DatabaseType.VARCHAR);

        return databaseTypeMap;
    }

    private Map<String, String> makeToiletMap() {
        Map<String, String> bathroomMap = new HashMap<>();

        bathroomMap.put("toiletName", "name");
        bathroomMap.put("address", "address");
        bathroomMap.put("lat", "latitude");
        bathroomMap.put("lng", "longitude");

        return bathroomMap;
    }
}