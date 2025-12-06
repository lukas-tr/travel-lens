package com.travellens.model;

public class Location {
    private double latitude;
    private double longitude;
    private Long timestampMs;
    private Integer accuracy;

    public Location() {
    }

    public Location(double latitude, double longitude, Long timestampMs, Integer accuracy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestampMs = timestampMs;
        this.accuracy = accuracy;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Long getTimestampMs() {
        return timestampMs;
    }

    public void setTimestampMs(Long timestampMs) {
        this.timestampMs = timestampMs;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }
}
