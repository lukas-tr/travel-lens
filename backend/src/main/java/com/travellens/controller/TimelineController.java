package com.travellens.controller;

import com.travellens.model.Suggestion;
import com.travellens.model.TravelAnalysis;
import com.travellens.model.Trip;
import com.travellens.service.AnalysisService;
import com.travellens.service.SuggestionService;
import com.travellens.service.TimelineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/timeline")
@RequiredArgsConstructor
@Slf4j
public class TimelineController {

    private final TimelineService timelineService;
    private final AnalysisService analysisService;
    private final SuggestionService suggestionService;

    // Store parsed data in memory (in production, use a database)
    private List<Trip> currentTrips;
    private TravelAnalysis currentAnalysis;

    /**
     * Upload and parse Google Maps Timeline JSON file
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadTimeline(@RequestParam("file") MultipartFile file) {
        try {
            log.info("Received file upload: {}", file.getOriginalFilename());

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
            }

            if (!file.getOriginalFilename().endsWith(".json")) {
                return ResponseEntity.badRequest().body(Map.of("error", "File must be a JSON file"));
            }

            // Parse the JSON content
            String jsonContent = new String(file.getBytes());
            currentTrips = timelineService.parseTimelineJson(jsonContent);

            if (currentTrips.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "No trips found in the Timeline data. Please check the file format."));
            }

            log.info("Successfully parsed {} trips", currentTrips.size());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("tripCount", currentTrips.size());
            response.put("message", "Timeline data uploaded and parsed successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error processing upload", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to process file: " + e.getMessage()));
        }
    }

    /**
     * Analyze the uploaded timeline data
     */
    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeTimeline() {
        try {
            if (currentTrips == null || currentTrips.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "No timeline data available. Please upload a file first."));
            }

            log.info("Analyzing {} trips", currentTrips.size());

            // Perform analysis
            currentAnalysis = analysisService.analyzeTrips(currentTrips);

            // Generate suggestions
            List<Suggestion> suggestions = suggestionService.generateSuggestions(currentTrips);
            currentAnalysis.setSuggestions(suggestions);

            // Calculate potential savings
            analysisService.calculatePotentialSavings(currentAnalysis, suggestions);

            log.info("Analysis complete with {} suggestions", suggestions.size());

            return ResponseEntity.ok(currentAnalysis);

        } catch (Exception e) {
            log.error("Error during analysis", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to analyze data: " + e.getMessage()));
        }
    }

    /**
     * Get suggestions for improvement
     */
    @GetMapping("/suggestions")
    public ResponseEntity<?> getSuggestions() {
        try {
            if (currentAnalysis == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "No analysis available. Please analyze timeline data first."));
            }

            return ResponseEntity.ok(currentAnalysis.getSuggestions());

        } catch (Exception e) {
            log.error("Error retrieving suggestions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve suggestions: " + e.getMessage()));
        }
    }

    /**
     * Get current analysis results
     */
    @GetMapping("/analysis")
    public ResponseEntity<?> getAnalysis() {
        try {
            if (currentAnalysis == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "No analysis available. Please analyze timeline data first."));
            }

            return ResponseEntity.ok(currentAnalysis);

        } catch (Exception e) {
            log.error("Error retrieving analysis", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve analysis: " + e.getMessage()));
        }
    }

    /**
     * Get all trips
     */
    @GetMapping("/trips")
    public ResponseEntity<?> getTrips() {
        try {
            if (currentTrips == null || currentTrips.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "No trips available. Please upload timeline data first."));
            }

            return ResponseEntity.ok(currentTrips);

        } catch (Exception e) {
            log.error("Error retrieving trips", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve trips: " + e.getMessage()));
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "TravelLens API"));
    }
}
