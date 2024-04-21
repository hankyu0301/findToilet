package com.findToilet.domain.toilet.service;

import com.findToilet.domain.toilet.dto.PointDto;
import com.findToilet.domain.toilet.dto.ToiletDto;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserLocationCalculator {

    private static final double EARTH_RADIUS = 6371.0;
    PointDto userPoint;
    double distance;

    public UserLocationCalculator(PointDto pointDto, Double distance) {
        this.userPoint = pointDto;
        this.distance = distance;
    }

    public double getLatitudeMinus() {
        return userPoint.getLatitude() - 0.0091 * distance;
    }

    public double getLatitudePlus() {
        return userPoint.getLatitude() + 0.0091 * distance;
    }

    public double getLongitudeMinus() {
        return userPoint.getLongitude() - 0.011269 * distance;
    }

    public double getLongitudePlus() {
        return userPoint.getLongitude() + 0.011269 * distance;
    }

    public List<ToiletDto> calculateDistanceAndRemove(List<ToiletDto> toiletDtos) {
        List<ToiletDto> filteredToiletDtos = toiletDtos.stream()
                .map((dto) -> {
                    double calculateDistance = calculateDistance(new PointDto(dto.getLatitude(), dto.getLongitude()));
                    return calculateDistance <= distance ? dto.withDistance(calculateDistance) : null;
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingDouble(ToiletDto::getDistance))
                .collect(Collectors.toList());

        // 거리에 따라 정렬

        return filteredToiletDtos;
    }

    private double calculateDistance(PointDto targetPoint) {
        double dLat = Math.toRadians(targetPoint.getLatitude() - userPoint.getLatitude());
        double dLon = Math.toRadians(targetPoint.getLongitude() - userPoint.getLongitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(targetPoint.getLatitude())) * Math.cos(Math.toRadians(userPoint.getLatitude()))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
