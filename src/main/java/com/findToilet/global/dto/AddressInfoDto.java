package com.findToilet.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressInfoDto {

    Double latitude;
    Double longitude;
    String address;
    String road_address;

}