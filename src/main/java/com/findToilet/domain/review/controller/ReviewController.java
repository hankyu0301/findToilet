package com.findToilet.domain.review.controller;

import com.findToilet.domain.review.dto.ReviewCreateRequest;
import com.findToilet.domain.review.dto.ReviewDto;
import com.findToilet.domain.review.dto.ReviewReadCondition;
import com.findToilet.domain.review.entity.Review;
import com.findToilet.domain.review.service.ReviewService;
import com.findToilet.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/api/reviews")
    public ResponseEntity<Object> create(@Valid @RequestBody ReviewCreateRequest req) {
        reviewService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/api/reviews")
    public ResponseEntity<ResponseDto<List<ReviewDto>>> readAll(@Valid ReviewReadCondition cond) {
        List<ReviewDto> reviewList = reviewService.readAll(cond);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(reviewList));
    }

    @DeleteMapping("/api/reviews/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.ok().build();
    }
}
