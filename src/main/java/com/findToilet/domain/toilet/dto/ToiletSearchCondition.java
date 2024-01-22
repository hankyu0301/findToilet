package com.findToilet.domain.toilet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToiletSearchCondition {

    @NotNull(message = "페이지 번호를 입력해주세요.")
    @PositiveOrZero(message = "올바른 페이지 번호를 입력해주세요. (0 이상)")
    private Integer page;

    @NotNull(message = "페이지 크기를 입력해주세요.")
    @Positive(message = "올바른 페이지 크기를 입력해주세요. (1 이상)")
    private Integer size;

    //사용자의 위치를 기준으로 인근의 화장실을 탐색.
    private Double userLatitude;

    private Double userLongitude;

    private Double distance;

    private boolean disabled; // 장애인용 변기가 구비되어 있는지?

    private boolean kids; // 유아용 변기가 구비되어 있는지?

    private boolean diaper; // 기저귀 교환대가 있는지?

}
