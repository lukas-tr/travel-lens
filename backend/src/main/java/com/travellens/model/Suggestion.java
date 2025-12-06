package com.travellens.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Suggestion {
    private String id;
    private String tripId;
    private TransportMode currentMode;
    private TransportMode suggestedMode;
    private String title;
    private String description;
    private ImpactLevel impactLevel;
    private double co2SavingsKg;
    private double costSavingsEur;
    private long timeDifferenceMinutes; // negative means slower, positive means faster
    private String route;
}
