package com.travellens.model;

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

    public Trip() {
    }

    public Trip(String id, Location startLocation, Location endLocation, Long startTime, Long endTime,
            TransportMode transportMode, double distanceKm, long durationMinutes,
            double co2EmissionsKg, double costEur) {
        this.id = id;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startTime = startTime;
        this.endTime = endTime;
        this.transportMode = transportMode;
        this.distanceKm = distanceKm;
        this.durationMinutes = durationMinutes;
        this.co2EmissionsKg = co2EmissionsKg;
        this.costEur = costEur;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public TransportMode getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(TransportMode transportMode) {
        this.transportMode = transportMode;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public long getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(long durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public double getCo2EmissionsKg() {
        return co2EmissionsKg;
    }

    public void setCo2EmissionsKg(double co2EmissionsKg) {
        this.co2EmissionsKg = co2EmissionsKg;
    }

    public double getCostEur() {
        return costEur;
    }

    public void setCostEur(double costEur) {
        this.costEur = costEur;
    }
}
