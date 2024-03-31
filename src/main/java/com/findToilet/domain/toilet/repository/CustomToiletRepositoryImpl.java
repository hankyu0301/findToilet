package com.findToilet.domain.toilet.repository;

import com.findToilet.domain.toilet.dto.ToiletDto;
import com.findToilet.domain.toilet.dto.ToiletSearchCondition;
import com.findToilet.domain.toilet.entity.Toilet;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.findToilet.domain.review.entity.QReview.review;
import static com.findToilet.domain.toilet.entity.QToilet.toilet;
import static com.querydsl.core.types.Projections.constructor;
import static com.querydsl.core.types.dsl.Expressions.numberTemplate;

public class CustomToiletRepositoryImpl extends QuerydslRepositorySupport implements CustomToiletRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomToiletRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Toilet.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<ToiletDto> findAllByCondition(ToiletSearchCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        List<ToiletDto> toiletDtos = fetchAll(pageable, predicate, cond);
        Long totalCount = countTotal(predicate, cond);
        return new PageImpl<>(toiletDtos, pageable, totalCount);
    }

    private Predicate createPredicate(ToiletSearchCondition cond) {
        return new BooleanBuilder()
                .and(distanceLt(cond.getUserLongitude(), cond.getUserLatitude(), cond.getLimit()))
                .and(disabledEq(cond.isDisabled()))
                .and(kidsEq(cond.isKids()))
                .and(diaperEq(cond.isDiaper()));
    }

    private BooleanExpression distanceLt(Double userLongitude, Double userLatitude, Double limit) {
        return numberTemplate(Double.class, "ST_Distance_Sphere(POINT({0}, {1}), POINT({2}, {3}))",
                userLongitude,
                userLatitude,
                toilet.longitude,
                toilet.latitude).loe(limit);
    }


    private BooleanExpression disabledEq(boolean disabled) {
        return disabled ? (toilet.male_disabled.isTrue().and(toilet.female_disabled.isTrue())) : null;
    }

    private BooleanExpression kidsEq(boolean kids) {
        return kids ? (toilet.male_kids.isTrue().and(toilet.female_kids.isTrue())) : null;
    }

    private BooleanExpression diaperEq(boolean diaper) {
        return diaper ? toilet.diaper.isTrue() : null;
    }

    //스프링 데이터가 제공하는 페이징을 Querydsl로 편리하게 변환
    private List<ToiletDto> fetchAll(Pageable pageable, Predicate predicate, ToiletSearchCondition cond) {
        return getQuerydsl().applyPagination(pageable, createQuery(predicate, cond)).fetch();
    }

    private JPQLQuery<ToiletDto> createQuery(Predicate predicate, ToiletSearchCondition cond) {


        return jpaQueryFactory
                .select(constructor(ToiletDto.class,
                        toilet.id,
                        toilet.name,
                        toilet.road_address,
                        toilet.latitude,
                        toilet.longitude,
                        getDistance(toilet.longitude, toilet.latitude, cond.getUserLongitude(), cond.getUserLatitude()).as("distance"),
                        toilet.male_disabled,
                        toilet.female_disabled,
                        toilet.male_kids,
                        toilet.female_kids,
                        toilet.diaper,
                        toilet.operation_time,
                        review.score.coalesce((double) 0).avg().as("score"),
                        review.count().as("scoreCount")))
                .from(toilet)
                .leftJoin(toilet.reviewList, review)
                .where(predicate)
                .groupBy(toilet.id)
                .orderBy(getDistance(toilet.longitude, toilet.latitude, cond.getUserLongitude(), cond.getUserLatitude()).asc());
    }

    private NumberTemplate<Double> getDistance(NumberPath<Double> longitude, NumberPath<Double> latitude, Double userLongitude, Double userLatitude) {
        return numberTemplate(Double.class, "ST_Distance_Sphere(POINT({0}, {1}), POINT({2}, {3}))",
                userLongitude,
                userLatitude,
                longitude,
                latitude);
    }

    private Long countTotal(Predicate predicate, ToiletSearchCondition cond) {
        return createQuery(predicate, cond).fetchCount();
    }


}
