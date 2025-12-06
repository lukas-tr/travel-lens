package com.travellens.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelAnalysis {
    private int totalTrips;
    private double totalDistanceKm;
    private double totalCo2EmissionsKg;
    private double totalCostEur;
    private long totalTimeMinutes;
    private Map<TransportMode, Integer> tripsByMode;
    private Map<TransportMode, Double> distanceByMode;
    private Map<TransportMode, Double> co2ByMode;
    private GreenScore greenScore;
    private List<Suggestion> suggestions;
    private double potentialCo2SavingsKg;
    private double potentialCostSavingsEur;
}
