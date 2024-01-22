package com.findToilet.domain.toilet.service;

import com.findToilet.domain.toilet.dto.*;
import com.findToilet.domain.toilet.entity.Toilet;
import com.findToilet.domain.toilet.repository.ToiletRepository;
import com.findToilet.global.exception.CustomException;
import com.findToilet.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ToiletService {

    private final ToiletRepository toiletRepository;

    public ToiletListDto findAllByCondition(ToiletSearchCondition cond) {
        return ToiletListDto.toDto(toiletRepository.findAllByCondition(cond));
    }

    //위도, 경도는 API를 사용해서 받아올까?
    public void create(ToiletCreateRequest req) {
        Toilet toilet = Toilet.builder()
                .name(req.getName())
                .road_address(req.getRoad_address())
                .address(req.getAddress())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .disabled(req.isDisabled())
                .diaper(req.isDiaper())
                .operation_time(req.getOperation_time())
                .build();
        toiletRepository.save(toilet);
    }

    public ToiletDto read(Long id) {
        return ToiletDto.toDto(toiletRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionCode.TOILET_NOT_FOUND)));
    }

    public void update(Long id,ToiletUpdateRequest req) {
        Toilet toilet = toiletRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionCode.TOILET_NOT_FOUND));
        toilet.update(req.getName(), req.getRoad_address(), req.getAddress(), req.isDisabled(), req.isKids(), req.isDiaper(), req.getOperation_time());
    }

    public void delete(Long id) {
        Toilet toilet = toiletRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionCode.TOILET_NOT_FOUND));
        toiletRepository.delete(toilet);
    }
}
