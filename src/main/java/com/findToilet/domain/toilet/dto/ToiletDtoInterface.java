package com.findToilet.domain.toilet.dto;

public interface ToiletDtoInterface {

    Long getId();
    String getName();
    String getRoad_address();
    Double getDistance();
    Boolean getDisabled();
    Boolean getKids();
    Boolean getDiaper();
    String getOperation_time();
    Double getScore();
    Long getScoreCount();
}
