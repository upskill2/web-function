package com.spring.microservices.demo.webfunction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TollStation {

    private String stationId;
    private Float mileMarker;
    private Integer stallCount;
}
