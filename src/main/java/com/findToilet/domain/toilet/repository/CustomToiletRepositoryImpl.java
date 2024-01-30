package com.findToilet.domain.toilet.repository;

import com.findToilet.domain.toilet.dto.ToiletDto;
import com.findToilet.domain.toilet.dto.ToiletSearchCondition;
import com.findToilet.domain.toilet.entity.Toilet;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
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
import static com.findToilet.global.util.UserLocationCalculator.*;
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
                .and(locationExpression(cond))
                .and(disabledEq(cond.isDisabled()))
                .and(kidsEq(cond.isKids()))
                .and(diaperEq(cond.isDiaper()));
    }

    /*  사용자의 위치정보를 기준으로 distance 내에 있는 화장실을 탐색합니다.
    * */
    private BooleanExpression locationExpression(ToiletSearchCondition cond) {

        return toilet.latitude.between(getLatitudeMinus(cond.getUserLatitude(), cond.getDistance()), getLatitudePlus(cond.getUserLatitude(), cond.getDistance()))
                .and(toilet.longitude.between(getLongitudeMinus(cond.getUserLongitude(), cond.getDistance()), getLongitudePlus(cond.getUserLongitude(), cond.getDistance())));

    }

    private BooleanExpression disabledEq(boolean disabled) {
        return disabled ? toilet.disabled.isTrue() : null;
    }

    private BooleanExpression kidsEq(boolean kids) {
        return kids ? toilet.kids.isTrue() : null;
    }

    private BooleanExpression diaperEq(boolean diaper) {
        return diaper ? toilet.diaper.isTrue() : null;
    }

    //스프링 데이터가 제공하는 페이징을 Querydsl로 편리하게 변환
    private List<ToiletDto> fetchAll(Pageable pageable, Predicate predicate, ToiletSearchCondition cond) {
        return getQuerydsl().applyPagination(pageable, createQuery(predicate, cond)).fetch();
    }

    private JPQLQuery<ToiletDto> createQuery(Predicate predicate, ToiletSearchCondition cond) {

        NumberExpression<Double> distanceExpression =
                numberTemplate(Double.class,
                        "6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1})))",
                        cond.getUserLatitude(), cond.getUserLongitude(), toilet.latitude, toilet.longitude);

        return jpaQueryFactory
                .select(constructor(ToiletDto.class,
                        toilet.id,
                        toilet.name,
                        toilet.road_address,
                        toilet.operation_time,
                        toilet.latitude,
                        toilet.longitude,
                        distanceExpression,
                        toilet.disabled,
                        toilet.kids,
                        toilet.diaper,
                        review.score.coalesce((double) 0).avg().as("score"),
                        review.count().as("scoreCount")))
                .from(toilet)
                .leftJoin(toilet.reviewList, review)
                .where(predicate)
                .orderBy(distanceExpression.asc());
    }

    private Long countTotal(Predicate predicate, ToiletSearchCondition cond) {
        return createQuery(predicate, cond).fetchCount();
    }


}
