package com.gage.traffic_optimizer.analysis;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record TrafficRequest(
        @NotBlank String intersectionName,
        @Min(0) int northbound,
        @Min(0) int southbound,
        @Min(0) int eastbound,
        @Min(0) int westbound,
        @Min(15) int currentNorthSouthGreen,
        @Min(15) int currentEastWestGreen
) {
}