package com.travellens.service;

import com.travellens.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SuggestionService {

    private static final Logger log = LoggerFactory.getLogger(SuggestionService.class);
    private final AnalysisService analysisService;

    public SuggestionService(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    /**
     * Generate personalized suggestions for improving travel behavior
     */
    public List<Suggestion> generateSuggestions(List<Trip> trips) {
        List<Suggestion> suggestions = new ArrayList<>();

        for (Trip trip : trips) {
            // Only suggest alternatives for vehicle trips
            if (trip.getTransportMode() == TransportMode.IN_VEHICLE ||
                    trip.getTransportMode() == TransportMode.IN_ROAD_VEHICLE) {

                suggestions.addAll(generateAlternativesForTrip(trip));
            }
        }

        // Sort by impact level (HIGH first)
        suggestions.sort((a, b) -> {
            int impactCompare = b.getImpactLevel().compareTo(a.getImpactLevel());
            if (impactCompare != 0)
                return impactCompare;
            // If same impact, sort by CO2 savings
            return Double.compare(b.getCo2SavingsKg(), a.getCo2SavingsKg());
        });

        log.info("Generated {} suggestions", suggestions.size());
        return suggestions;
    }

    private List<Suggestion> generateAlternativesForTrip(Trip trip) {
        List<Suggestion> alternatives = new ArrayList<>();

        double distance = trip.getDistanceKm();

        // Short trips (<5km): Suggest bicycle or walking
        if (distance < 5) {
            if (distance < 2) {
                alternatives.add(createSuggestion(trip, TransportMode.WALKING));
            }
            alternatives.add(createSuggestion(trip, TransportMode.ON_BICYCLE));
        }

        // Medium trips (5-20km): Suggest public transport or bicycle
        if (distance >= 5 && distance < 20) {
            alternatives.add(createSuggestion(trip, TransportMode.ON_BICYCLE));
            alternatives.add(createSuggestion(trip, TransportMode.IN_PASSENGER_VEHICLE));
        }

        // Long trips (20km+): Suggest train or public transport
        if (distance >= 20) {
            alternatives.add(createSuggestion(trip, TransportMode.IN_RAIL_VEHICLE));
            alternatives.add(createSuggestion(trip, TransportMode.IN_PASSENGER_VEHICLE));
        }

        return alternatives;
    }

    private Suggestion createSuggestion(Trip trip, TransportMode suggestedMode) {
        Suggestion suggestion = new Suggestion();
        suggestion.setId(UUID.randomUUID().toString());
        suggestion.setTripId(trip.getId());
        suggestion.setCurrentMode(trip.getTransportMode());
        suggestion.setSuggestedMode(suggestedMode);

        // Calculate savings
        double currentCo2 = trip.getCo2EmissionsKg();
        double suggestedCo2 = trip.getDistanceKm() * analysisService.getCo2PerKm(suggestedMode);
        double co2Savings = currentCo2 - suggestedCo2;
        suggestion.setCo2SavingsKg(co2Savings);

        double currentCost = trip.getCostEur();
        double suggestedCost = trip.getDistanceKm() * analysisService.getCostPerKm(suggestedMode);
        double costSavings = currentCost - suggestedCost;
        suggestion.setCostSavingsEur(costSavings);

        // Calculate time difference
        long currentTime = trip.getDurationMinutes();
        long suggestedTime = analysisService.calculateEstimatedTime(trip.getDistanceKm(), suggestedMode);
        long timeDifference = currentTime - suggestedTime;
        suggestion.setTimeDifferenceMinutes(timeDifference);

        // Determine impact level
        double improvementPercent = (co2Savings / currentCo2) * 100;
        if (improvementPercent > 30) {
            suggestion.setImpactLevel(ImpactLevel.HIGH);
        } else if (improvementPercent > 10) {
            suggestion.setImpactLevel(ImpactLevel.MEDIUM);
        } else {
            suggestion.setImpactLevel(ImpactLevel.LOW);
        }

        // Generate title and description
        suggestion.setTitle(generateTitle(trip, suggestedMode));
        suggestion.setDescription(generateDescription(suggestion));
        suggestion.setRoute(generateRoute(trip));

        return suggestion;
    }

    private String generateTitle(Trip trip, TransportMode suggestedMode) {
        String modeStr = formatTransportMode(suggestedMode);
        double distance = trip.getDistanceKm();

        return String.format("Switch to %s for %.1f km trip", modeStr, distance);
    }

    private String generateDescription(Suggestion suggestion) {
        StringBuilder desc = new StringBuilder();

        desc.append(String.format("Save %.2f kg CO2", suggestion.getCo2SavingsKg()));

        if (suggestion.getCostSavingsEur() > 0) {
            desc.append(String.format(" and €%.2f", suggestion.getCostSavingsEur()));
        }

        desc.append(" by switching from ");
        desc.append(formatTransportMode(suggestion.getCurrentMode()));
        desc.append(" to ");
        desc.append(formatTransportMode(suggestion.getSuggestedMode()));
        desc.append(".");

        if (suggestion.getTimeDifferenceMinutes() < 0) {
            desc.append(String.format(" This will add approximately %d minutes to your journey.",
                    Math.abs(suggestion.getTimeDifferenceMinutes())));
        } else if (suggestion.getTimeDifferenceMinutes() > 0) {
            desc.append(String.format(" You might even save %d minutes!",
                    suggestion.getTimeDifferenceMinutes()));
        }

        // Add health benefits for active transport
        if (suggestion.getSuggestedMode() == TransportMode.ON_BICYCLE ||
                suggestion.getSuggestedMode() == TransportMode.WALKING ||
                suggestion.getSuggestedMode() == TransportMode.RUNNING ||
                suggestion.getSuggestedMode() == TransportMode.ON_FOOT) {
            desc.append(" Plus, you'll get great exercise!");
        }

        return desc.toString();
    }

    private String generateRoute(Trip trip) {
        if (trip.getStartLocation() != null && trip.getEndLocation() != null) {
            return String.format("%.4f,%.4f → %.4f,%.4f",
                    trip.getStartLocation().getLatitude(),
                    trip.getStartLocation().getLongitude(),
                    trip.getEndLocation().getLatitude(),
                    trip.getEndLocation().getLongitude());
        }
        return "Route information unavailable";
    }

    private String formatTransportMode(TransportMode mode) {
        return switch (mode) {
            case WALKING -> "walking";
            case RUNNING -> "running";
            case ON_FOOT -> "on foot";
            case ON_BICYCLE -> "bicycle";
            case IN_VEHICLE -> "vehicle";
            case IN_ROAD_VEHICLE -> "car";
            case IN_PASSENGER_VEHICLE -> "public transport";
            case IN_RAIL_VEHICLE -> "train";
            default -> "alternative transport";
        };
    }
}
