package com.findToilet.domain.review.service;

import com.findToilet.domain.member.entity.Member;
import com.findToilet.domain.member.repository.MemberRepository;
import com.findToilet.domain.review.dto.ReviewCreateRequest;
import com.findToilet.domain.review.dto.ReviewDto;
import com.findToilet.domain.review.entity.Review;
import com.findToilet.domain.review.repository.ReviewRepository;
import com.findToilet.domain.toilet.entity.Toilet;
import com.findToilet.domain.toilet.repository.ToiletRepository;
import com.findToilet.global.exception.CustomException;
import com.findToilet.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.findToilet.global.util.AuthUtil.getMemberId;

@Transactional
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ToiletRepository toiletRepository;
    private final MemberRepository memberRepository;

    public void create(ReviewCreateRequest req) {

        Toilet toilet = toiletRepository.findById(req.getToiletId())
                .orElseThrow(() -> new CustomException(ExceptionCode.TOILET_NOT_FOUND));

        Member member = memberRepository.findById(getMemberId())
                .orElseThrow(() -> new CustomException(ExceptionCode.MEMBER_NOT_FOUND));

        //지금 로그인한 회원의 정보가 필요함
        Review review = Review.builder()
                .content(req.getContent())
                .score(req.getScore())
                .member(member)
                .toilet(toilet)
                .build();

        reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> readAll(Long toiletId) {
        List<Review> reviewList = reviewRepository.findByToiletId(toiletId);
        return reviewList.stream().map(ReviewDto::of).collect(Collectors.toList());
    }

    public void delete(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionCode.REVIEW_NOT_FOUND));
        reviewRepository.delete(review);
    }
}
