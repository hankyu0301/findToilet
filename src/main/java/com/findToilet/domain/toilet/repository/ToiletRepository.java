package com.findToilet.domain.toilet.repository;

import com.findToilet.domain.toilet.dto.ToiletDto;
import com.findToilet.domain.toilet.dto.ToiletReadResponseDto;
import com.findToilet.domain.toilet.entity.Toilet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ToiletRepository extends JpaRepository<Toilet, Long>, CustomToiletRepository {

    @Query("select new com.findToilet.domain.toilet.dto.ToiletReadResponseDto(t.id, t.name, t.road_address, t.male_disabled, t.female_disabled, t.male_kids, t.female_kids, t.diaper, t.operation_time, avg (r.score), count (r)) from Toilet t left join t.reviewList r where t.id = :id")
    ToiletReadResponseDto findByIdWithReview(Long id);

    @Query("select new com.findToilet.domain.toilet.dto.ToiletDto(t.id, t.name, t.road_address, t.latitude, t.longitude, t.male_disabled, t.female_disabled, t.male_kids, t.female_kids, t.diaper, t.operation_time, avg(r.score), count(r)) " +
            "from Toilet t " +
            "left join t.reviewList r " +
            "where t.latitude >= :lati_minus " +
            "and t.latitude <= :lati_plus " +
            "and t.longitude >= :long_minus " +
            "and t.longitude <= :long_plus " +
            "and (:kids is null or (t.male_kids = :kids and t.female_kids = :kids)) " +
            "and (:disabled is null or (t.male_disabled = :disabled and t.female_disabled = :disabled)) " +
            "and (:diaper is null or (t.diaper = :diaper)) " +
            "group by t ")
    List<ToiletDto> findAllByCondition(Boolean kids, Boolean disabled, Boolean diaper, @Param("lati_minus")Double x1, @Param("lati_plus")Double x2, @Param("long_minus")Double y1, @Param("long_plus")Double y2);
}
