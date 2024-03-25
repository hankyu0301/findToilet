package com.findToilet.domain.toilet.service;

import com.findToilet.domain.toilet.dto.*;
import com.findToilet.domain.toilet.dto.ToiletReadResponseDto;
import com.findToilet.domain.toilet.entity.Toilet;
import com.findToilet.domain.toilet.repository.ToiletRepository;
import com.findToilet.global.exception.CustomException;
import com.findToilet.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ToiletService {

    private final ToiletRepository toiletRepository;

    @Transactional(readOnly = true)
    public ToiletListDto findAllByCondition(ToiletSearchCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        return ToiletListDto.toDto(toiletRepository.findAllByCondition(cond));
    }

    public void create(ToiletCreateRequest req) {

        Toilet toilet = Toilet.builder()
                .name(req.getName())
                .road_address(req.getRoad_address())
                .address(req.getAddress())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
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
