package com.findToilet.domain.toilet.repository;

import com.findToilet.domain.toilet.dto.ToiletDto;
import com.findToilet.domain.toilet.dto.ToiletSearchCondition;
import org.springframework.data.domain.Page;

public interface CustomToiletRepository {

    Page<ToiletDto> findAllByCondition(ToiletSearchCondition cond);
}
