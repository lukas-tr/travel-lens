package com.travellens.model;

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

    public Suggestion() {
    }

    public Suggestion(String id, String tripId, TransportMode currentMode, TransportMode suggestedMode,
            String title, String description, ImpactLevel impactLevel, double co2SavingsKg,
            double costSavingsEur, long timeDifferenceMinutes, String route) {
        this.id = id;
        this.tripId = tripId;
        this.currentMode = currentMode;
        this.suggestedMode = suggestedMode;
        this.title = title;
        this.description = description;
        this.impactLevel = impactLevel;
        this.co2SavingsKg = co2SavingsKg;
        this.costSavingsEur = costSavingsEur;
        this.timeDifferenceMinutes = timeDifferenceMinutes;
        this.route = route;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public TransportMode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(TransportMode currentMode) {
        this.currentMode = currentMode;
    }

    public TransportMode getSuggestedMode() {
        return suggestedMode;
    }

    public void setSuggestedMode(TransportMode suggestedMode) {
        this.suggestedMode = suggestedMode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImpactLevel getImpactLevel() {
        return impactLevel;
    }

    public void setImpactLevel(ImpactLevel impactLevel) {
        this.impactLevel = impactLevel;
    }

    public double getCo2SavingsKg() {
        return co2SavingsKg;
    }

    public void setCo2SavingsKg(double co2SavingsKg) {
        this.co2SavingsKg = co2SavingsKg;
    }

    public double getCostSavingsEur() {
        return costSavingsEur;
    }

    public void setCostSavingsEur(double costSavingsEur) {
        this.costSavingsEur = costSavingsEur;
    }

    public long getTimeDifferenceMinutes() {
        return timeDifferenceMinutes;
    }

    public void setTimeDifferenceMinutes(long timeDifferenceMinutes) {
        this.timeDifferenceMinutes = timeDifferenceMinutes;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
