package com.findToilet.domain.toilet.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ToiletDto {

    private Long id;
    private String name;    // 화장실 명
    private String road_address;    //  화장실 주소
    private Double latitude;    //  위도
    private Double longitude;   //  경도
    private Double distance;    //  사용자 위치로부터의 거리
    private boolean disabled;   //  장애인용 화장실
    private boolean kids;   //  유아용 화장실
    private boolean diaper;   //  기저귀 교환대 유무
    private String operation_time;  //  화장실 운영 시간
    private Double score;   //  화장실 평점 평균
    private Long scoreCount;    //  화장실 평점 갯수

    @Builder
    public ToiletDto(Long id, String name, String road_address, Double latitude, Double longitude, Double distance,  boolean male_disabled, boolean female_disabled, boolean male_kids, boolean female_kids, boolean diaper, String operation_time, Double score, Long scoreCount) {
        this.id = id;
        this.name = name;
        this.road_address = road_address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.disabled = (male_disabled && female_disabled);
        this.kids = (male_kids && female_kids);
        this.diaper = diaper;
        this.operation_time = operation_time;
        this.score = score;
        this.scoreCount = scoreCount;
    }

    @Builder
    public ToiletDto(Long id, String name, String road_address, Double latitude, Double longitude, boolean male_disabled, boolean female_disabled, boolean male_kids, boolean female_kids, boolean diaper, String operation_time, Double score, Long scoreCount) {
        this.id = id;
        this.name = name;
        this.road_address = road_address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.disabled = (male_disabled && female_disabled);
        this.kids = (male_kids && female_kids);
        this.diaper = diaper;
        this.operation_time = operation_time;
        this.score = score;
        this.scoreCount = scoreCount;
    }

    public ToiletDto withDistance(double calculateDistance) {
        this.setDistance(calculateDistance * 1000);
        return this;
    }
}
