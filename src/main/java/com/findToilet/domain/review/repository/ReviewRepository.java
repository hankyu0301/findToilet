package com.findToilet.domain.review.repository;

import com.findToilet.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r left join fetch r.toilet left join fetch r.member where r.toilet.id = :toiletId order by r.id desc")
    List<Review> findByToiletId(Long toiletId);
}
