package com.findToilet.domain.review.dto;

import com.findToilet.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    private Long id;
    private String content;
    private Double score;
    private LocalDateTime createdAt;

    private Long toiletId;

    private String memberNickname;

    public static ReviewDto of(Review review) {
        return new ReviewDto(review.getId(),
                review.getContent(),
                review.getScore(),
                review.getCreatedAt(),
                review.getToilet().getId(),
                review.getMember().getNickname());
    }

}
