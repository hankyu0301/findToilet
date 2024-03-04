package com.findToilet.domain.toilet.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToiletUpdateRequest {

    private String name; // 화장실 이름 (ex: 중동공원, 1호문화공원)

    private String road_address; // 도로명 주소

    private String address; // 지번 주소

    private Double latitude;

    private Double longitude;

    private boolean disabled; // 장애인용 대변기가 구비되어 있는지?

    private boolean kids;

    private boolean diaper; // 기저귀 교환대가 있는지? (남자에만 있고 여자에는 없고 이런 곳도 있는듯)

    private String operation_time; // 개방 시간 (제공하지 않는 곳도 있음) 09~18시, 상시, 6~22시, 05~25시, 0~24시, 09:00~18:00 등 지들 멋대로임) 그냥 String으로 제공해준 값을 그대로 써야할듯

}
