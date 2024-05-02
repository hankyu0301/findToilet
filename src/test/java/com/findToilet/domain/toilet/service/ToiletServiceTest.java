package com.findToilet.domain.toilet.service;


import com.findToilet.domain.toilet.dto.PointDto;
import com.findToilet.domain.toilet.dto.ToiletDto;
import com.findToilet.domain.toilet.dto.ToiletListDto;
import com.findToilet.domain.toilet.dto.ToiletSearchCondition;
import com.findToilet.domain.toilet.repository.ToiletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@Transactional
class ToiletServiceTest {

    @InjectMocks
    ToiletService toiletService;

    @Mock
    ToiletRepository toiletRepository;
    @Test
    void findAllByConditionUsingMySQLFunction() {
        // given
        ToiletSearchCondition cond = ToiletSearchCondition.builder()
                .page(0)
                .size(20)
                .userLatitude(37.445620228619)
                .userLongitude(126.65182310263)
                .limit(100.0) // m
                .disabled(false)
                .kids(false)
                .diaper(false)
                .build();

        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());

        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(126.65182310263, 37.445620228619));

        List<ToiletDto> toiletDtoList = List.of(ToiletDto.builder()
                .id(1L)
                .name("testName")
                .road_address("우리집")
                .location(point)
                .male_disabled(false)
                .female_disabled(false)
                .male_kids(false)
                .female_kids(false)
                .diaper(false)
                .operation_time("everyday")
                .score(4.5)
                .scoreCount(99L).build());

        Page<ToiletDto> toiletDtoPage = new PageImpl<>(toiletDtoList, pageable, 1L);

        Boolean kids = cond.getKids();
        Boolean disabled = cond.getDisabled();
        Boolean diaper = cond.getDiaper();

        given(toiletRepository.findAllByConditionUsingMySQLFunction(kids,disabled,diaper,cond.getUserLongitude(),cond.getUserLatitude(),cond.getLimit())).willReturn(toiletDtoList);

        // when
        ToiletListDto toiletListDto = toiletService.findAllByConditionUsingMySQLFunction(cond);

        // then
        assertThat(toiletListDto.getToiletDtoList().size()).isEqualTo(1);
    }

    @Test
    void findAllByConditionUsingJPQL() {
        // given
        ToiletSearchCondition cond = ToiletSearchCondition.builder()
                .page(0)
                .size(20)
                .userLatitude(37.445620228619)
                .userLongitude(126.65182310263)
                .limit(0.5) // km
                .disabled(false)
                .kids(false)
                .diaper(false)
                .build();

        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());

        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(126.65182310263, 37.445620228619));

        List<ToiletDto> toiletDtoList = List.of(ToiletDto.builder()
                .id(1L)
                .name("testName")
                .road_address("우리집")
                .location(point)
                .male_disabled(false)
                .female_disabled(false)
                .male_kids(false)
                .female_kids(false)
                .diaper(false)
                .operation_time("everyday")
                .score(4.5)
                .scoreCount(99L)
                .build());

        Boolean kids = cond.getKids();
        Boolean disabled = cond.getDisabled();
        Boolean diaper = cond.getDiaper();

        UserLocationCalculator locationService = new UserLocationCalculator(new PointDto(cond.getUserLatitude(), cond.getUserLongitude()), cond.getLimit());

        List<ToiletDto> updatedToiletDtos = locationService.calculateDistanceAndRemove(toiletDtoList);
        Page<ToiletDto> toiletDtoPage = new PageImpl<>(updatedToiletDtos, pageable, updatedToiletDtos.size());

        given(toiletRepository.findAllByCondition(kids, disabled, diaper,
                locationService.getLatitudeMinus(),
                locationService.getLatitudePlus(), locationService.getLongitudeMinus(),
                locationService.getLongitudePlus())).willReturn(updatedToiletDtos);

        // when
        ToiletListDto toiletListDto = toiletService.findAllByConditionUsingJPQL(cond);

        // then
        assertThat(toiletListDto.getToiletDtoList().size()).isEqualTo(1);
    }
}
