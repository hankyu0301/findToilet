package com.findToilet.domain.toilet.dto;

import com.findToilet.domain.toilet.entity.Toilet;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToiletDto {

    private Long id;
    private String name;    // 화장실 명
    private String road_address;    //  화장실 주소
    private String operation_time;  //  화장실 운영 시간
    private Double latitude;    //  화장실 위도
    private Double longitude;   //  화장실 경도
    private Double distance;    //  화장실까지 거리(m)
    private boolean disabled;   //  장애인용 화장실
    private boolean kids;   //  유아용 화장실
    private boolean diaper;   //  기저귀 교환대 유무
    private Double score;   //  화장실 평점 평균
    private Long scoreCount;    //  화장실 평점 갯수

    public ToiletDto withDistance(double calculateDistance) {
        this.setDistance(calculateDistance * 1000);
        return this;
    }

    public static ToiletDto toDto(Toilet toilet) {
        return ToiletDto.builder().build();
    }
}
