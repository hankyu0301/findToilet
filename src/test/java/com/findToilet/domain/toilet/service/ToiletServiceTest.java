package com.findToilet.domain.toilet.service;


import com.findToilet.domain.toilet.dto.ToiletDto;
import com.findToilet.domain.toilet.dto.ToiletListDto;
import com.findToilet.domain.toilet.dto.ToiletSearchCondition;
import com.findToilet.domain.toilet.repository.ToiletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    void findAllByCondition() {
        // given
        ToiletSearchCondition cond = ToiletSearchCondition.builder()
                .page(0)
                .size(20)
                .userLatitude(37.445620228619)
                .userLongitude(126.65182310263)
                .limit(100.0) // km
                .disabled(false)
                .kids(false)
                .diaper(false)
                .build();

        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());

        Page<ToiletDto> toiletDtoPage = new PageImpl<>(List.of(ToiletDto.builder()
                .id(1L)
                .name("testName")
                .road_address("우리집")
                .latitude(37.445620228619)
                .longitude(126.65182310263)
                .disabled(false)
                .kids(false)
                .diaper(false)
                .operation_time("everyday")
                .score(4.5)
                .scoreCount(99L)
                .build()), pageable, 1L);

        given(toiletRepository.findAllByCondition(cond)).willReturn(toiletDtoPage);

        // when
        ToiletListDto toiletListDto = toiletService.findAllByCondition(cond);

        // then
        assertThat(toiletListDto.getToiletDtoList().size()).isEqualTo(1);
    }
}
