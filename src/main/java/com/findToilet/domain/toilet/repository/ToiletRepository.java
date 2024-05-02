package com.findToilet.domain.toilet.repository;

import com.findToilet.domain.toilet.dto.ToiletDto;
import com.findToilet.domain.toilet.dto.ToiletReadResponseDto;
import com.findToilet.domain.toilet.entity.Toilet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ToiletRepository extends JpaRepository<Toilet, Long>{

    @Query("select new com.findToilet.domain.toilet.dto.ToiletReadResponseDto(t.id, t.name, t.road_address, t.male_disabled, t.female_disabled, t.male_kids, t.female_kids, t.diaper, t.operation_time, avg (r.score), count (r)) from Toilet t left join t.reviewList r where t.id = :id")
    ToiletReadResponseDto findByIdWithReview(Long id);


    @Query("select new com.findToilet.domain.toilet.dto.ToiletDto(" +
            "t.id, t.name, t.road_address, t.location, t.male_disabled, t.female_disabled, t.male_kids, t.female_kids, " +
            "t.diaper, t.operation_time, avg(r.score), count(r)) " +
            "from Toilet t " +
            "left join t.reviewList r " +
            "where ST_X(t.location) >= :long_minus " +
            "and ST_X(t.location) <= :long_plus " +
            "and ST_Y(t.location) >= :lati_minus " +
            "and ST_Y(t.location) <= :lati_plus " +
            "and (:kids is null or (t.male_kids = :kids and t.female_kids = :kids)) " +
            "and (:disabled is null or (t.male_disabled = :disabled and t.female_disabled = :disabled)) " +
            "and (:diaper is null or (t.diaper = :diaper)) " +
            "group by t ")
    List<ToiletDto> findAllByCondition(Boolean kids, Boolean disabled, Boolean diaper, @Param("lati_minus")Double x1, @Param("lati_plus")Double x2, @Param("long_minus")Double y1, @Param("long_plus")Double y2);


    @Query("select new com.findToilet.domain.toilet.dto.ToiletDto(" +
            "t.id, t.name, t.road_address, t.location, ST_DISTANCE_SPHERE(ST_GeomFromText('POINT(' || :userLongitude || ' ' || :userLatitude || ')', 4326), t.location), t.male_disabled, t.female_disabled, t.male_kids, t.female_kids, " +
            "t.diaper, t.operation_time, avg(r.score), count(r)) " +
            "from Toilet t " +
            "left join t.reviewList r " +
            "where ST_Contains(ST_Buffer(ST_GeomFromText('POINT(' || :userLongitude || ' ' || :userLatitude || ')', 4326), :limit), t.location) = true " +
            "and (:kids is null or (t.male_kids = :kids and t.female_kids = :kids)) " +
            "and (:disabled is null or (t.male_disabled = :disabled and t.female_disabled = :disabled)) " +
            "and (:diaper is null or (t.diaper = :diaper)) " +
            "group by t ")
    List<ToiletDto> findAllByConditionUsingMySQLFunction(Boolean kids, Boolean disabled, Boolean diaper, Double userLongitude, Double userLatitude, Double limit);
}
