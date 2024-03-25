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

    private String operation_time; // 개방 시간 (제공하지 않는 곳도 있음) 09~18시, 상시, 6~22시, 05~25시, 0~24시, 09:00~18:00 등 지들 멋대로임) 그냥 String으로 제공해준 값을 그대로 써야할듯

}
