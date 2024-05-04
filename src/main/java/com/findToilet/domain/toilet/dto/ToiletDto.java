package com.findToilet.domain.toilet.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ToiletDto {

    private Long id;
    private String name;    // 화장실 명
    private String road_address;    //  화장실 주소
    private Double distance;    //  사용자 위치로부터의 거리
    private boolean disabled;   //  장애인용 화장실
    private boolean kids;   //  유아용 화장실
    private boolean diaper;   //  기저귀 교환대 유무
    private String operation_time;  //  화장실 운영 시간
    private Double score;   //  화장실 평점 평균
    private Long scoreCount;    //  화장실 평점 갯수

    @Builder
    public ToiletDto(Long id, String name, String road_address, boolean male_disabled, boolean female_disabled, boolean male_kids, boolean female_kids, boolean diaper, String operation_time, Double score, Long scoreCount) {
        this.id = id;
        this.name = name;
        this.road_address = road_address;
        this.disabled = (male_disabled && female_disabled);
        this.kids = (male_kids && female_kids);
        this.diaper = diaper;
        this.operation_time = operation_time;
        this.score = score;
        this.scoreCount = scoreCount;
    }


    public ToiletDto(Long id, String name, String road_address, Double distance, boolean male_disabled, boolean female_disabled, boolean male_kids, boolean female_kids, boolean diaper, String operation_time, Double score, Long scoreCount) {
        this.id = id;
        this.name = name;
        this.road_address = road_address;
        this.distance = distance;
        this.disabled = (male_disabled && female_disabled);
        this.kids = (male_kids && female_kids);
        this.diaper = diaper;
        this.operation_time = operation_time;
        this.score = score;
        this.scoreCount = scoreCount;
    }

    public ToiletDto(ToiletDtoInterface toiletDtoInterface) {
        this.id = toiletDtoInterface.getId();
        this.name = toiletDtoInterface.getName();
        this.road_address = toiletDtoInterface.getRoad_address();
        this.distance = toiletDtoInterface.getDistance();
        this.disabled = toiletDtoInterface.getDisabled() != null ? toiletDtoInterface.getDisabled() : false;
        this.kids = toiletDtoInterface.getKids() != null ? toiletDtoInterface.getKids() : false;
        this.diaper = toiletDtoInterface.getDiaper() != null ? toiletDtoInterface.getDiaper() : false;
        this.operation_time = toiletDtoInterface.getOperation_time();
        this.score = toiletDtoInterface.getScore();
        this.scoreCount = toiletDtoInterface.getScoreCount();
    }

    public ToiletDto withDistance(double calculateDistance) {
        this.setDistance(calculateDistance * 1000);
        return this;
    }
}
