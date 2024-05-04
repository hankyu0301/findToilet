package com.findToilet.dataInserter.data.dataconfig.producer;

import com.findToilet.dataInserter.data.dataconfig.DataConfig;
import com.findToilet.dataInserter.data.dataconfig.ToiletConfig;
import com.findToilet.dataInserter.data.dataset.DataSet;
import com.findToilet.dataInserter.data.dataset.XlsxDataSet;
import com.findToilet.dataInserter.data.util.type.DatabaseType;
import com.findToilet.dataInserter.data.util.type.DomainType;
import com.findToilet.global.dto.AddressInfoDto;
import com.findToilet.global.util.ReverseGeocoding.ChangeByGeocoderKakao;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class XlsxConfigProducer implements ConfigProducer {

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
        return new XlsxDataSet(getDataConfig());
    }

    private Consumer<Map<String, String>> makeMapConsumer() {
        return (map) -> {
            AddressInfoDto address = changeByGeocoderKakao.getCoordinateByAddress(map.get("address"));
            String longitude = address.getLongitude().toString();
            String latitude = address.getLatitude().toString();
            map.put("location", latitude + " " + longitude);
            map.put("road_address", address.getRoad_address());
        };
    }

    private static Map<String, DatabaseType> makeDatabaseTypeMap() {
        Map<String, DatabaseType> databaseTypeMap = new HashMap<>();

        databaseTypeMap.put("name", DatabaseType.VARCHAR);
        databaseTypeMap.put("road_address", DatabaseType.VARCHAR);
        databaseTypeMap.put("address", DatabaseType.VARCHAR);
        databaseTypeMap.put("location", DatabaseType.POINT);
        databaseTypeMap.put("male_disabled", DatabaseType.BOOLEAN);
        databaseTypeMap.put("female_disabled", DatabaseType.BOOLEAN);
        databaseTypeMap.put("male_kids", DatabaseType.BOOLEAN);
        databaseTypeMap.put("female_kids", DatabaseType.BOOLEAN);
        databaseTypeMap.put("diaper", DatabaseType.BOOLEAN);
        databaseTypeMap.put("operation_time", DatabaseType.VARCHAR);

        return databaseTypeMap;
    }

    private static Map<String, String> makeToiletMap() {
        Map<String, String> bathroomMap = new HashMap<>();

        bathroomMap.put("화장실명", "name");
        bathroomMap.put("소재지도로명주소", "address");
        bathroomMap.put("소재지지번주소", "address");
        bathroomMap.put("남성용-장애인용대변기수", "male_disabled");
        bathroomMap.put("여성용-장애인용대변기수", "female_disabled");
        bathroomMap.put("남성용-어린이용대변기수", "male_kids");
        bathroomMap.put("여성용-어린이용대변기수", "female_kids");
        bathroomMap.put("기저귀교환대유무", "diaper");
        bathroomMap.put("개방시간", "operation_time");

        return bathroomMap;
    }

}