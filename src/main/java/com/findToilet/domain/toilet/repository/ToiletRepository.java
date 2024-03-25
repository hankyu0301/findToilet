package com.findToilet.domain.toilet.repository;

import com.findToilet.domain.toilet.dto.ToiletReadResponseDto;
import com.findToilet.domain.toilet.entity.Toilet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ToiletRepository extends JpaRepository<Toilet, Long>, CustomToiletRepository {

    @Query("select new com.findToilet.domain.toilet.dto.ToiletReadResponseDto(t.id, t.name, t.road_address, t.male_disabled, t.female_disabled, t.male_kids, t.female_kids, t.diaper, t.operation_time, avg (r.score), count (r)) from Toilet t left join t.reviewList r where t.id =: id")
    ToiletReadResponseDto findByIdWithReview(Long id);

}
