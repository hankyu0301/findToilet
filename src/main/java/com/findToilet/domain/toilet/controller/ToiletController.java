package com.findToilet.domain.toilet.controller;

import com.findToilet.domain.toilet.dto.*;
import com.findToilet.domain.toilet.dto.ToiletReadResponseDto;
import com.findToilet.domain.toilet.service.ToiletService;
import com.findToilet.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ToiletController {

    private final ToiletService toiletService;

    @GetMapping("/api/toilets/v1")
    public ResponseEntity<ResponseDto<ToiletListDto>> findAllToiletByConditionUsingJPQL(@Valid @ModelAttribute ToiletSearchCondition toiletSearchCondition) {
        ToiletListDto toiletListDto = toiletService.findAllByConditionUsingJPQL(toiletSearchCondition);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(toiletListDto));
    }

    @GetMapping("/api/toilets/v2")
    public ResponseEntity<ResponseDto<ToiletListDto>> findAllToiletByConditionUsingMySQLFunction(@Valid @ModelAttribute ToiletSearchCondition toiletSearchCondition) {
        ToiletListDto toiletListDto = toiletService.findAllByConditionUsingMySQLFunction(toiletSearchCondition);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(toiletListDto));
    }

    @PostMapping("/api/toilets")
    public ResponseEntity<Object> create(@Valid @RequestBody ToiletCreateRequest req) {
        toiletService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/api/toilets/{id}")
    public ResponseEntity<ResponseDto<ToiletReadResponseDto>> read(@PathVariable Long id) {
        ToiletReadResponseDto result = toiletService.read(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(result));
    }

    @PutMapping("/api/toilets/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody ToiletUpdateRequest req) {
        toiletService.update(id, req);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/api/toilets/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        toiletService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
