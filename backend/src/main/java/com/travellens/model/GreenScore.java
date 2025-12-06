package com.travellens.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GreenScore {
    private int totalScore; // 0-100
    private int co2Score; // 0-100
    private int costScore; // 0-100
    private int sustainabilityScore; // 0-100
    private String rating; // A+, A, B, C, D, E, F
    private String message;
}
