package com.findToilet.domain.toilet.entity;

import com.findToilet.global.audit.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Toilet extends BaseEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name; // 화장실 이름 (ex: 중동공원, 1호문화공원)

    private String road_address; // 도로명 주소

    private String address; // 지번 주소

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private boolean disabled; // 장애인 이용 가능 시설인지?

    @Column(nullable = false)
    private boolean kids;   //  유아용 대,소변기 유무

    @Column(nullable = false)
    private boolean diaper; // 기저귀 교환대가 있는지? (남자에만 있고 여자에는 없고 이런 곳도 있는듯)

    private String operation_time; // 개방 시간 (제공하지 않는 곳도 있음) 09~18시, 상시, 6~22시, 05~25시, 0~24시, 09:00~18:00 등 멋대로임) 포맷팅 해서 제공해야할듯

    public void update(String name, String road_address, String address, boolean disabled, boolean kids, boolean diaper, String operation_time) {
        this.name = name;
        this.road_address = road_address;
        this.address = address;
        this.disabled = disabled;
        this.kids = kids;
        this.diaper = diaper;
    }
}
