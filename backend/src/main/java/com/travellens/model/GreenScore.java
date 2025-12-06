package com.travellens.model;

public class GreenScore {
    private int totalScore; // 0-100
    private int co2Score; // 0-100
    private int costScore; // 0-100
    private int sustainabilityScore; // 0-100
    private String rating; // A+, A, B, C, D, E, F
    private String message;

    public GreenScore() {
    }

    public GreenScore(int totalScore, int co2Score, int costScore, int sustainabilityScore,
            String rating, String message) {
        this.totalScore = totalScore;
        this.co2Score = co2Score;
        this.costScore = costScore;
        this.sustainabilityScore = sustainabilityScore;
        this.rating = rating;
        this.message = message;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getCo2Score() {
        return co2Score;
    }

    public void setCo2Score(int co2Score) {
        this.co2Score = co2Score;
    }

    public int getCostScore() {
        return costScore;
    }

    public void setCostScore(int costScore) {
        this.costScore = costScore;
    }

    public int getSustainabilityScore() {
        return sustainabilityScore;
    }

    public void setSustainabilityScore(int sustainabilityScore) {
        this.sustainabilityScore = sustainabilityScore;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
