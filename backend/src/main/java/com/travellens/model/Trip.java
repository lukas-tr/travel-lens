package com.travellens.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    private String id;
    private Location startLocation;
    private Location endLocation;
    private Long startTime;
    private Long endTime;
    private TransportMode transportMode;
    private double distanceKm;
    private long durationMinutes;
    private double co2EmissionsKg;
    private double costEur;
}
