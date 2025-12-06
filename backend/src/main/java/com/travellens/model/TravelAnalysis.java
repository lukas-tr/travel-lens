package com.travellens.model;

import java.util.List;
import java.util.Map;

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

    public TravelAnalysis() {
    }

    public TravelAnalysis(int totalTrips, double totalDistanceKm, double totalCo2EmissionsKg,
            double totalCostEur, long totalTimeMinutes,
            Map<TransportMode, Integer> tripsByMode,
            Map<TransportMode, Double> distanceByMode,
            Map<TransportMode, Double> co2ByMode,
            GreenScore greenScore, List<Suggestion> suggestions,
            double potentialCo2SavingsKg, double potentialCostSavingsEur) {
        this.totalTrips = totalTrips;
        this.totalDistanceKm = totalDistanceKm;
        this.totalCo2EmissionsKg = totalCo2EmissionsKg;
        this.totalCostEur = totalCostEur;
        this.totalTimeMinutes = totalTimeMinutes;
        this.tripsByMode = tripsByMode;
        this.distanceByMode = distanceByMode;
        this.co2ByMode = co2ByMode;
        this.greenScore = greenScore;
        this.suggestions = suggestions;
        this.potentialCo2SavingsKg = potentialCo2SavingsKg;
        this.potentialCostSavingsEur = potentialCostSavingsEur;
    }

    public int getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(int totalTrips) {
        this.totalTrips = totalTrips;
    }

    public double getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public void setTotalDistanceKm(double totalDistanceKm) {
        this.totalDistanceKm = totalDistanceKm;
    }

    public double getTotalCo2EmissionsKg() {
        return totalCo2EmissionsKg;
    }

    public void setTotalCo2EmissionsKg(double totalCo2EmissionsKg) {
        this.totalCo2EmissionsKg = totalCo2EmissionsKg;
    }

    public double getTotalCostEur() {
        return totalCostEur;
    }

    public void setTotalCostEur(double totalCostEur) {
        this.totalCostEur = totalCostEur;
    }

    public long getTotalTimeMinutes() {
        return totalTimeMinutes;
    }

    public void setTotalTimeMinutes(long totalTimeMinutes) {
        this.totalTimeMinutes = totalTimeMinutes;
    }

    public Map<TransportMode, Integer> getTripsByMode() {
        return tripsByMode;
    }

    public void setTripsByMode(Map<TransportMode, Integer> tripsByMode) {
        this.tripsByMode = tripsByMode;
    }

    public Map<TransportMode, Double> getDistanceByMode() {
        return distanceByMode;
    }

    public void setDistanceByMode(Map<TransportMode, Double> distanceByMode) {
        this.distanceByMode = distanceByMode;
    }

    public Map<TransportMode, Double> getCo2ByMode() {
        return co2ByMode;
    }

    public void setCo2ByMode(Map<TransportMode, Double> co2ByMode) {
        this.co2ByMode = co2ByMode;
    }

    public GreenScore getGreenScore() {
        return greenScore;
    }

    public void setGreenScore(GreenScore greenScore) {
        this.greenScore = greenScore;
    }

    public List<Suggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }

    public double getPotentialCo2SavingsKg() {
        return potentialCo2SavingsKg;
    }

    public void setPotentialCo2SavingsKg(double potentialCo2SavingsKg) {
        this.potentialCo2SavingsKg = potentialCo2SavingsKg;
    }

    public double getPotentialCostSavingsEur() {
        return potentialCostSavingsEur;
    }

    public void setPotentialCostSavingsEur(double potentialCostSavingsEur) {
        this.potentialCostSavingsEur = potentialCostSavingsEur;
    }
}
