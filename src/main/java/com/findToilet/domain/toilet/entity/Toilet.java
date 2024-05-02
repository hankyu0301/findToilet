package com.findToilet.domain.toilet.entity;

import com.findToilet.domain.review.entity.Review;
import com.findToilet.global.audit.BaseEntity;
import lombok.*;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Toilet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 화장실 이름 (ex: 중동공원, 1호문화공원)

    @Column(nullable = false)
    private String road_address; // 도로명 주소

    @Column(nullable = false)
    private String address; // 지번 주소

    @Column(nullable = false, columnDefinition = "Point")
    private Point location; // 위도와 경도를 포함한 공간 정보

    @Column(nullable = false)
    private boolean male_disabled; // 장애인 이용 가능 시설인지?

    @Column(nullable = false)
    private boolean female_disabled;

    @Column(nullable = false)
    private boolean male_kids;   //  유아용 대,소변기 유무

    @Column(nullable = false)
    private boolean female_kids;

    @Column(nullable = false)
    private boolean diaper; // 기저귀 교환대가 있는지? (남자에만 있고 여자에는 없고 이런 곳도 있는듯)

    private String operation_time; // 개방 시간 (제공하지 않는 곳도 있음) 09~18시, 상시, 6~22시, 05~25시, 0~24시, 09:00~18:00 등 멋대로임) 포맷팅 해서 제공해야할듯

    @OneToMany(mappedBy = "toilet")
    private List<Review> reviewList = new ArrayList<>();

    public Toilet(String name, String road_address, String address, Point location, boolean male_disabled, boolean female_disabled, boolean male_kids, boolean female_kids, boolean diaper, String operation_time) {
        this.name = name;
        this.road_address = road_address;
        this.address = address;
        this.location = location;
        this.male_disabled = male_disabled;
        this.female_disabled = female_disabled;
        this.male_kids = male_kids;
        this.female_kids = female_kids;
        this.diaper = diaper;
        this.operation_time = operation_time;
    }

    public void update(String name, String road_address, String address, String operation_time) {
        this.name = name;
        this.road_address = road_address;
        this.address = address;
        this.operation_time = operation_time;
    }

}
