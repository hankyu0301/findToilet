package com.findToilet.domain.toilet.repository;

import com.findToilet.domain.toilet.dto.ToiletDtoInterface;
import com.findToilet.domain.toilet.dto.ToiletReadResponseDto;
import com.findToilet.domain.toilet.entity.Toilet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ToiletRepository extends JpaRepository<Toilet, Long>{

    @Query("select new com.findToilet.domain.toilet.dto.ToiletReadResponseDto(t.id, t.name, t.road_address, t.male_disabled, t.female_disabled, t.male_kids, t.female_kids, t.diaper, t.operation_time, avg (r.score), count (r)) from Toilet t left join t.reviewList r where t.id = :id")
    ToiletReadResponseDto findByIdWithReview(Long id);

    @Query(value =
            "select t.id as id, t.name as name, t.road_address as road_address, ST_Distance_Sphere(ST_PointFromText(:point, 4326), t.location) as distance, t.male_disabled as male_disabled, t.female_disabled as female_disabled, t.male_kids as male_kids, t.female_kids as female_kids, t.diaper as diaper, t.operation_time as operation_time, avg(r.score) as score, count(r.id) as scoreCount " +
                    "from toilet t left join review r on t.id = r.toilet_id " +
                    "where ST_Contains(ST_Buffer(ST_PointFromText(:point, 4326), :limit), t.location) " +
                    "and (:kids is null or (t.male_kids = :kids and t.female_kids = :kids)) " +
                    "and (:disabled is null or (t.male_disabled = :disabled and t.female_disabled = :disabled)) " +
                    "and (:diaper is null or t.diaper = :diaper) " +
                    "group by t.id " +
                    "order by distance asc", nativeQuery = true)
    List<ToiletDtoInterface> findAllByCondition(Boolean kids, Boolean disabled, Boolean diaper, String point, Double limit);
}
