package com.travellens.service;

import com.travellens.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalysisService {

    private static final Logger log = LoggerFactory.getLogger(AnalysisService.class);

    // CO2 emissions in kg per km for different transport modes
    private static final Map<TransportMode, Double> CO2_PER_KM = Map.of(
            TransportMode.WALKING, 0.0,
            TransportMode.RUNNING, 0.0,
            TransportMode.ON_FOOT, 0.0,
            TransportMode.ON_BICYCLE, 0.0,
            TransportMode.IN_VEHICLE, 0.192, // Average private vehicle
            TransportMode.IN_ROAD_VEHICLE, 0.192, // Cars, motorcycles
            TransportMode.IN_PASSENGER_VEHICLE, 0.089, // Buses, trams
            TransportMode.IN_RAIL_VEHICLE, 0.041 // Trains, subways
    );

    // Cost in EUR per km for different transport modes
    private static final Map<TransportMode, Double> COST_PER_KM = Map.of(
            TransportMode.WALKING, 0.0,
            TransportMode.RUNNING, 0.0,
            TransportMode.ON_FOOT, 0.0,
            TransportMode.ON_BICYCLE, 0.02, // Maintenance only
            TransportMode.IN_VEHICLE, 0.35, // Fuel + maintenance
            TransportMode.IN_ROAD_VEHICLE, 0.35,
            TransportMode.IN_PASSENGER_VEHICLE, 0.12, // Public transport ticket
            TransportMode.IN_RAIL_VEHICLE, 0.15
    );

    // Average speed in km/h for different transport modes
    private static final Map<TransportMode, Double> AVERAGE_SPEED_KMH = Map.of(
            TransportMode.WALKING, 5.0,
            TransportMode.RUNNING, 10.0,
            TransportMode.ON_FOOT, 5.0,
            TransportMode.ON_BICYCLE, 15.0,
            TransportMode.IN_VEHICLE, 50.0,
            TransportMode.IN_ROAD_VEHICLE, 50.0,
            TransportMode.IN_PASSENGER_VEHICLE, 20.0,
            TransportMode.IN_RAIL_VEHICLE, 60.0
    );

    /**
     * Analyze trips and calculate comprehensive travel statistics
     */
    public TravelAnalysis analyzeTrips(List<Trip> trips) {
        TravelAnalysis analysis = new TravelAnalysis();

        // Calculate CO2 and costs for each trip
        trips.forEach(this::calculateTripMetrics);

        // Basic statistics
        analysis.setTotalTrips(trips.size());
        analysis.setTotalDistanceKm(trips.stream().mapToDouble(Trip::getDistanceKm).sum());
        analysis.setTotalCo2EmissionsKg(trips.stream().mapToDouble(Trip::getCo2EmissionsKg).sum());
        analysis.setTotalCostEur(trips.stream().mapToDouble(Trip::getCostEur).sum());
        analysis.setTotalTimeMinutes(trips.stream().mapToLong(Trip::getDurationMinutes).sum());

        // Group by transport mode
        Map<TransportMode, Integer> tripsByMode = trips.stream()
                .collect(Collectors.groupingBy(Trip::getTransportMode, Collectors.summingInt(t -> 1)));
        analysis.setTripsByMode(tripsByMode);

        Map<TransportMode, Double> distanceByMode = trips.stream()
                .collect(Collectors.groupingBy(Trip::getTransportMode,
                        Collectors.summingDouble(Trip::getDistanceKm)));
        analysis.setDistanceByMode(distanceByMode);

        Map<TransportMode, Double> co2ByMode = trips.stream()
                .collect(Collectors.groupingBy(Trip::getTransportMode,
                        Collectors.summingDouble(Trip::getCo2EmissionsKg)));
        analysis.setCo2ByMode(co2ByMode);

        // Calculate green score
        GreenScore greenScore = calculateGreenScore(trips, analysis);
        analysis.setGreenScore(greenScore);

        log.info("Analysis complete: {} trips, {:.2f} km, {:.2f} kg CO2",
                analysis.getTotalTrips(), analysis.getTotalDistanceKm(), analysis.getTotalCo2EmissionsKg());

        return analysis;
    }

    /**
     * Calculate CO2 emissions and cost for a single trip
     */
    private void calculateTripMetrics(Trip trip) {
        double co2PerKm = CO2_PER_KM.getOrDefault(trip.getTransportMode(), 0.1);
        double costPerKm = COST_PER_KM.getOrDefault(trip.getTransportMode(), 0.2);

        trip.setCo2EmissionsKg(trip.getDistanceKm() * co2PerKm);
        trip.setCostEur(trip.getDistanceKm() * costPerKm);
    }

    /**
     * Calculate green score (0-100) based on travel behavior
     */
    private GreenScore calculateGreenScore(List<Trip> trips, TravelAnalysis analysis) {
        GreenScore score = new GreenScore();

        // CO2 Score: Compare to average car-only scenario
        double carOnlyCo2 = analysis.getTotalDistanceKm() * CO2_PER_KM.get(TransportMode.IN_ROAD_VEHICLE);
        double actualCo2 = analysis.getTotalCo2EmissionsKg();
        int co2Score = (int) Math.max(0, Math.min(100, 100 - (actualCo2 / carOnlyCo2 * 100)));
        score.setCo2Score(co2Score);

        // Cost Score: Compare to average car-only scenario
        double carOnlyCost = analysis.getTotalDistanceKm() * COST_PER_KM.get(TransportMode.IN_ROAD_VEHICLE);
        double actualCost = analysis.getTotalCostEur();
        int costScore = (int) Math.max(0, Math.min(100, 100 - (actualCost / carOnlyCost * 100)));
        score.setCostScore(costScore);

        // Sustainability Score: Percentage of eco-friendly trips
        long ecoTrips = trips.stream()
                .filter(t -> t.getTransportMode() == TransportMode.ON_BICYCLE ||
                        t.getTransportMode() == TransportMode.WALKING ||
                        t.getTransportMode() == TransportMode.RUNNING ||
                        t.getTransportMode() == TransportMode.ON_FOOT ||
                        t.getTransportMode() == TransportMode.IN_RAIL_VEHICLE)
                .count();
        int sustainabilityScore = (int) ((ecoTrips * 100.0) / trips.size());
        score.setSustainabilityScore(sustainabilityScore);

        // Total Score: Equal to CO2 score only
    // Cost and sustainability scores are still calculated and displayed for reference
    int totalScore = co2Score;
    score.setTotalScore(totalScore);

        // Rating
        String rating = getRating(totalScore);
        score.setRating(rating);

        // Message
        String message = getMessage(totalScore, rating);
        score.setMessage(message);

        return score;
    }

    private String getRating(int score) {
        if (score >= 90)
            return "A+";
        if (score >= 80)
            return "A";
        if (score >= 70)
            return "B";
        if (score >= 60)
            return "C";
        if (score >= 50)
            return "D";
        if (score >= 40)
            return "E";
        return "F";
    }

    private String getMessage(int score, String rating) {
        if (score >= 90) {
            return "Excellent! You're a sustainability champion! 🌟";
        } else if (score >= 80) {
            return "Great job! Your travel choices are very eco-friendly! 🌱";
        } else if (score >= 70) {
            return "Good work! You're making positive environmental choices! 🌿";
        } else if (score >= 60) {
            return "Not bad! There's room for improvement in your travel habits.";
        } else if (score >= 50) {
            return "Average. Consider switching to more sustainable transport options.";
        } else {
            return "Your travel habits have significant environmental impact. Let's improve!";
        }
    }

    /**
     * Calculate potential savings if all suggestions are followed
     */
    public void calculatePotentialSavings(TravelAnalysis analysis, List<Suggestion> suggestions) {
        double potentialCo2Savings = suggestions.stream()
                .mapToDouble(Suggestion::getCo2SavingsKg)
                .sum();

        double potentialCostSavings = suggestions.stream()
                .mapToDouble(Suggestion::getCostSavingsEur)
                .sum();

        analysis.setPotentialCo2SavingsKg(potentialCo2Savings);
        analysis.setPotentialCostSavingsEur(potentialCostSavings);
    }

    /**
     * Get CO2 emissions per km for a transport mode
     */
    public double getCo2PerKm(TransportMode mode) {
        return CO2_PER_KM.getOrDefault(mode, 0.1);
    }

    /**
     * Get cost per km for a transport mode
     */
    public double getCostPerKm(TransportMode mode) {
        return COST_PER_KM.getOrDefault(mode, 0.2);
    }

    /**
     * Get average speed for a transport mode
     */
    public double getAverageSpeed(TransportMode mode) {
        return AVERAGE_SPEED_KMH.getOrDefault(mode, 30.0);
    }

    /**
     * Calculate estimated time for a distance with a transport mode
     */
    public long calculateEstimatedTime(double distanceKm, TransportMode mode) {
        double speedKmh = getAverageSpeed(mode);
        return (long) ((distanceKm / speedKmh) * 60); // Convert to minutes
    }
}
