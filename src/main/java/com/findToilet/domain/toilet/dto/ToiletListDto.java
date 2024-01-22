package com.findToilet.domain.toilet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class ToiletListDto {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<ToiletDto> postList;

    public static ToiletListDto toDto(Page<ToiletDto> page) {
        return new ToiletListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}