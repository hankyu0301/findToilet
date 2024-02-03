package com.findToilet.domain.review.dto;

import com.findToilet.domain.review.entity.Review;
import com.findToilet.domain.toilet.dto.ToiletDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    private Long id;
    private String content;
    private Double score;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private ToiletDto toiletDto;

    private String memberNickname;

    public static ReviewDto of(Review review) {
        return new ReviewDto(review.getId(),
                review.getContent(),
                review.getScore(),
                review.getCreatedAt(),
                review.getModifiedAt(),
                ToiletDto.toDto(review.getToilet()),
                review.getMember().getNickname());
    }

}
