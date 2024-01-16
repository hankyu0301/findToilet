package com.findToilet.global.util.ReverseGeocoding;

import com.findToilet.global.dto.CoordinateInfoDto;


public interface ReverseGeocoding {
    CoordinateInfoDto getAddressByCoordinate(Double x, Double y);
}
