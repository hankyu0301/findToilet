package com.findToilet.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateRequest {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "평점을 입력해주세요.")
    @Positive(message = "올바른 값을 입력해주세요.")
    private Double score;

    @NotNull(message = "화장실 아이디를 입력해주세요.")
    @Positive(message = "올바른 값을 입력해주세요.")
    private Long toiletId;

}