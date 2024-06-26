package com.findToilet.global.util.ReverseGeocoding;

import com.findToilet.global.dto.AddressInfoDto;
import com.findToilet.global.dto.CoordinateInfoDto;
import com.findToilet.global.exception.CustomException;
import com.github.openjson.JSONException;
import com.github.openjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static com.findToilet.global.exception.ExceptionCode.JSON_FORMAT_PARSING;

@Service
public class ChangeByGeocoderKakao implements ReverseGeocoding {

    @Value("${kakao.REST_API_KEY}")
    private String REST_API_KEY;

    public AddressInfoDto getCoordinateByAddress(String address) throws JSONException {
        String urlString = String.format(
                "https://dapi.kakao.com/v2/local/search/address.json?analyze_type=similar&page=1&size=10&query=%s",
                address);
        WebClient webClient = WebClient.builder()
                .baseUrl(urlString)
                .defaultHeader("Authorization", "KakaoAK " + REST_API_KEY)
                .build();
        String result = webClient.get()
                .uri("")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        JSONObject jsonObject = new JSONObject(result);
        if (jsonObject.getJSONArray("documents").isNull(0)) {
            AddressInfoDto nullAddressInfoDto = AddressInfoDto.builder()
                    .address(address)
                    .road_address("")
                    .longitude(0.0)
                    .latitude(0.0)
                    .build();
            return nullAddressInfoDto;
        }
        JSONObject document = jsonObject.getJSONArray("documents").getJSONObject(0);
        AddressInfoDto addressInfoDto = getAddressInfoDto(document);
        return addressInfoDto;
    }

    public CoordinateInfoDto getAddressByCoordinate(Double x, Double y) {
        String urlString = String.format("https://dapi.kakao.com/v2/local/geo/coord2address.json?x=%s&y=%s", x, y);
        WebClient webClient = WebClient.builder()
                .baseUrl(urlString)
                .defaultHeader("Authorization", "KakaoAK " + REST_API_KEY)
                .build();
        String result = webClient.get()
                .uri("")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            if (jsonObject.getJSONArray("documents").isNull(0)) {
                CoordinateInfoDto nullCoordinateInfoDto = CoordinateInfoDto.builder().build();
                return nullCoordinateInfoDto;
            }
            JSONObject document = jsonObject.getJSONArray("documents").getJSONObject(0);
            if (document.isNull("road_address")) {
                CoordinateInfoDto nullCoordinateInfoDto = CoordinateInfoDto.builder().build();
                return nullCoordinateInfoDto;
            }
            CoordinateInfoDto coordinateInfoDto = getCoordinateInfoDto(document);
            return coordinateInfoDto;
        } catch (JSONException e) {
            throw new CustomException(JSON_FORMAT_PARSING);
        }
    }

    private CoordinateInfoDto getCoordinateInfoDto(JSONObject document) throws JSONException {
        JSONObject road_address = document.getJSONObject("road_address");
        String address_name = (String)road_address.get("address_name");
        String building_name = (String)road_address.get("building_name");
        CoordinateInfoDto nullCoordinateInfoDto = CoordinateInfoDto.builder()
                .address_name(address_name)
                .address_detail(building_name)
                .build();
        return nullCoordinateInfoDto;
    }

    private AddressInfoDto getAddressInfoDto(JSONObject document) throws JSONException {
        Double longitude = Double.valueOf(((String)document.get("x")));
        Double latitude = Double.valueOf(((String)document.get("y")));
        String address_name = (String)document.get("address_name");
        String road_address_name = "";
        if (!document.isNull("road_address")) {
            JSONObject road_address = document.getJSONObject("road_address");
            road_address_name = (String)road_address.get("address_name");
        }
        AddressInfoDto addressInfoDto = AddressInfoDto.builder()
                .latitude(latitude)
                .longitude(longitude)
                .address(address_name)
                .road_address(road_address_name)
                .build();
        return addressInfoDto;
    }
}
