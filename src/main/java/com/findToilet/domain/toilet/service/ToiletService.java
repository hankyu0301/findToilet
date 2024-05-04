package com.findToilet.domain.toilet.service;

import com.findToilet.domain.toilet.dto.*;
import com.findToilet.domain.toilet.dto.ToiletReadResponseDto;
import com.findToilet.domain.toilet.entity.Toilet;
import com.findToilet.domain.toilet.repository.ToiletRepository;
import com.findToilet.global.exception.CustomException;
import com.findToilet.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class ToiletService {

    private final ToiletRepository toiletRepository;

    @Transactional(readOnly = true)
    public ToiletListDto findAllByConditionUsingMySQLFunction(ToiletSearchCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        String point = String.format("POINT(%f %f)", cond.getUserLatitude(), cond.getUserLongitude());
        List<ToiletDtoInterface> toiletDtoInterfaces = toiletRepository.findAllByConditionUsingMySQLFunction(cond.getKids(), cond.getDisabled(), cond.getDiaper(), point, cond.getLimit());
        List<ToiletDto> toiletDtoList = toiletDtoInterfaces.stream().map(ToiletDto::new).collect(Collectors.toList());
        return ToiletListDto.toDto(new PageImpl<>(toiletDtoList, pageable, toiletDtoList.size()));
    }

    public void create(ToiletCreateRequest req) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(req.getLongitude(), req.getLatitude()));

        Toilet toilet = Toilet.builder()
                .name(req.getName())
                .road_address(req.getRoad_address())
                .address(req.getAddress())
                .location(point)
                .male_disabled(req.isMale_disabled())
                .female_disabled(req.isFemale_disabled())
                .male_kids(req.isMale_kids())
                .female_kids(req.isFemale_kids())
                .diaper(req.isDiaper())
                .operation_time(req.getOperation_time())
                .build();

        toiletRepository.save(toilet);
    }

    @Transactional(readOnly = true)
    public ToiletReadResponseDto read(Long id) {
        return toiletRepository.findByIdWithReview(id);
    }

    public void update(Long id,ToiletUpdateRequest req) {
        Toilet toilet = toiletRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionCode.TOILET_NOT_FOUND));
        toilet.update(req.getName(), req.getRoad_address(), req.getAddress(), req.getOperation_time());
    }

    public void delete(Long id) {
        Toilet toilet = toiletRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionCode.TOILET_NOT_FOUND));
        toiletRepository.delete(toilet);
    }
}
