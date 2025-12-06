package com.travellens.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travellens.model.Location;
import com.travellens.model.TransportMode;
import com.travellens.model.Trip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TimelineService {

    private static final Logger log = LoggerFactory.getLogger(TimelineService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Parse Google Maps Timeline JSON and extract trips
     * Supports both legacy (Takeout) and new (2024) formats
     */
    public List<Trip> parseTimelineJson(String jsonContent) {
        try {
            JsonNode root = objectMapper.readTree(jsonContent);

            // Check if it's the new format (has semanticSegments)
            if (root.has("semanticSegments")) {
                return parseNewFormat(root);
            }
            // Legacy format (has locations array)
            else if (root.has("locations")) {
                return parseLegacyFormat(root);
            }
            // Try timelineObjects (another variant)
            else if (root.has("timelineObjects")) {
                return parseTimelineObjects(root);
            }

            log.warn("Unknown Timeline JSON format");
            return new ArrayList<>();

        } catch (Exception e) {
            log.error("Error parsing Timeline JSON", e);
            throw new RuntimeException("Failed to parse Timeline JSON: " + e.getMessage());
        }
    }

    private List<Trip> parseNewFormat(JsonNode root) {
        List<Trip> trips = new ArrayList<>();
        JsonNode segments = root.get("semanticSegments");

        if (segments.isArray()) {
            for (JsonNode segment : segments) {
                if (segment.has("activity")) {
                    Trip trip = parseActivitySegment(segment.get("activity"));
                    if (trip != null) {
                        trips.add(trip);
                    }
                }
            }
        }

        log.info("Parsed {} trips from new format", trips.size());
        return trips;
    }

    private List<Trip> parseLegacyFormat(JsonNode root) {
        List<Trip> trips = new ArrayList<>();
        JsonNode locations = root.get("locations");

        // Group consecutive locations into trips based on time gaps
        List<Location> locationList = new ArrayList<>();
        if (locations.isArray()) {
            for (JsonNode loc : locations) {
                locationList.add(parseLocation(loc));
            }
        }

        // Simple trip detection: group locations with <30min gaps
        trips = detectTripsFromLocations(locationList);

        log.info("Parsed {} trips from legacy format", trips.size());
        return trips;
    }

    private List<Trip> parseTimelineObjects(JsonNode root) {
        List<Trip> trips = new ArrayList<>();
        JsonNode timelineObjects = root.get("timelineObjects");

        if (timelineObjects.isArray()) {
            for (JsonNode obj : timelineObjects) {
                if (obj.has("activitySegment")) {
                    Trip trip = parseActivitySegment(obj.get("activitySegment"));
                    if (trip != null) {
                        trips.add(trip);
                    }
                }
            }
        }

        log.info("Parsed {} trips from timelineObjects format", trips.size());
        return trips;
    }

    private Trip parseActivitySegment(JsonNode activity) {
        try {
            Trip trip = new Trip();
            trip.setId(UUID.randomUUID().toString());

            // Parse start/end locations
            if (activity.has("startLocation")) {
                trip.setStartLocation(parseLocationFromNode(activity.get("startLocation")));
            }
            if (activity.has("endLocation")) {
                trip.setEndLocation(parseLocationFromNode(activity.get("endLocation")));
            }

            // Parse timestamps
            if (activity.has("duration")) {
                JsonNode duration = activity.get("duration");
                if (duration.has("startTimestamp")) {
                    trip.setStartTime(parseTimestamp(duration.get("startTimestamp").asText()));
                }
                if (duration.has("endTimestamp")) {
                    trip.setEndTime(parseTimestamp(duration.get("endTimestamp").asText()));
                }
            }

            // Parse activity type (transport mode)
            if (activity.has("activityType")) {
                String activityType = activity.get("activityType").asText();
                trip.setTransportMode(mapActivityTypeToTransportMode(activityType));
            } else {
                trip.setTransportMode(TransportMode.UNKNOWN);
            }

            // Parse distance
            if (activity.has("distance")) {
                double distanceMeters = activity.get("distance").asDouble();
                trip.setDistanceKm(distanceMeters / 1000.0);
            } else if (trip.getStartLocation() != null && trip.getEndLocation() != null) {
                trip.setDistanceKm(calculateDistance(trip.getStartLocation(), trip.getEndLocation()));
            }

            // Calculate duration
            if (trip.getStartTime() != null && trip.getEndTime() != null) {
                trip.setDurationMinutes((trip.getEndTime() - trip.getStartTime()) / (1000 * 60));
            }

            return trip;
        } catch (Exception e) {
            log.error("Error parsing activity segment", e);
            return null;
        }
    }

    private Location parseLocation(JsonNode loc) {
        Location location = new Location();

        if (loc.has("latitudeE7")) {
            location.setLatitude(loc.get("latitudeE7").asDouble() / 1e7);
        } else if (loc.has("latitude")) {
            location.setLatitude(loc.get("latitude").asDouble());
        }

        if (loc.has("longitudeE7")) {
            location.setLongitude(loc.get("longitudeE7").asDouble() / 1e7);
        } else if (loc.has("longitude")) {
            location.setLongitude(loc.get("longitude").asDouble());
        }

        if (loc.has("timestampMs")) {
            location.setTimestampMs(loc.get("timestampMs").asLong());
        } else if (loc.has("timestamp")) {
            location.setTimestampMs(parseTimestamp(loc.get("timestamp").asText()));
        }

        if (loc.has("accuracy")) {
            location.setAccuracy(loc.get("accuracy").asInt());
        }

        return location;
    }

    private Location parseLocationFromNode(JsonNode locNode) {
        Location location = new Location();

        if (locNode.has("latitudeE7")) {
            location.setLatitude(locNode.get("latitudeE7").asDouble() / 1e7);
        } else if (locNode.has("latitude")) {
            location.setLatitude(locNode.get("latitude").asDouble());
        }

        if (locNode.has("longitudeE7")) {
            location.setLongitude(locNode.get("longitudeE7").asDouble() / 1e7);
        } else if (locNode.has("longitude")) {
            location.setLongitude(locNode.get("longitude").asDouble());
        }

        return location;
    }

    private List<Trip> detectTripsFromLocations(List<Location> locations) {
        List<Trip> trips = new ArrayList<>();

        if (locations.isEmpty()) {
            return trips;
        }

        // Sort by timestamp
        locations.sort((a, b) -> Long.compare(a.getTimestampMs(), b.getTimestampMs()));

        List<Location> currentTrip = new ArrayList<>();
        currentTrip.add(locations.get(0));

        for (int i = 1; i < locations.size(); i++) {
            Location prev = locations.get(i - 1);
            Location curr = locations.get(i);

            long timeDiffMinutes = (curr.getTimestampMs() - prev.getTimestampMs()) / (1000 * 60);

            // If gap > 30 minutes, consider it a new trip
            if (timeDiffMinutes > 30) {
                if (currentTrip.size() > 1) {
                    trips.add(createTripFromLocations(currentTrip));
                }
                currentTrip = new ArrayList<>();
            }

            currentTrip.add(curr);
        }

        // Add last trip
        if (currentTrip.size() > 1) {
            trips.add(createTripFromLocations(currentTrip));
        }

        return trips;
    }

    private Trip createTripFromLocations(List<Location> locations) {
        Trip trip = new Trip();
        trip.setId(UUID.randomUUID().toString());

        Location start = locations.get(0);
        Location end = locations.get(locations.size() - 1);

        trip.setStartLocation(start);
        trip.setEndLocation(end);
        trip.setStartTime(start.getTimestampMs());
        trip.setEndTime(end.getTimestampMs());
        trip.setDurationMinutes((end.getTimestampMs() - start.getTimestampMs()) / (1000 * 60));
        trip.setDistanceKm(calculateDistance(start, end));
        trip.setTransportMode(inferTransportMode(trip.getDistanceKm(), trip.getDurationMinutes()));

        return trip;
    }

    private TransportMode inferTransportMode(double distanceKm, long durationMinutes) {
        if (durationMinutes == 0)
            return TransportMode.UNKNOWN;

        double speedKmh = (distanceKm / durationMinutes) * 60;

        if (speedKmh < 5)
            return TransportMode.WALKING;
        if (speedKmh < 20)
            return TransportMode.BICYCLE;
        if (speedKmh < 40)
            return TransportMode.BUS;
        if (speedKmh < 100)
            return TransportMode.CAR;
        return TransportMode.AIRPLANE;
    }

    private TransportMode mapActivityTypeToTransportMode(String activityType) {
        return switch (activityType.toUpperCase()) {
            case "IN_VEHICLE", "DRIVING" -> TransportMode.CAR;
            case "IN_BUS" -> TransportMode.BUS;
            case "IN_TRAIN" -> TransportMode.TRAIN;
            case "IN_TRAM" -> TransportMode.TRAM;
            case "IN_SUBWAY" -> TransportMode.SUBWAY;
            case "CYCLING", "BICYCLING" -> TransportMode.BICYCLE;
            case "WALKING", "ON_FOOT" -> TransportMode.WALKING;
            case "MOTORCYCLING" -> TransportMode.MOTORCYCLE;
            case "FLYING" -> TransportMode.AIRPLANE;
            default -> TransportMode.UNKNOWN;
        };
    }

    private long parseTimestamp(String timestamp) {
        try {
            // Handle ISO 8601 format
            return java.time.Instant.parse(timestamp).toEpochMilli();
        } catch (Exception e) {
            log.error("Error parsing timestamp: {}", timestamp);
            return 0L;
        }
    }

    /**
     * Calculate distance between two locations using Haversine formula
     */
    private double calculateDistance(Location start, Location end) {
        final int R = 6371; // Earth's radius in km

        double lat1 = Math.toRadians(start.getLatitude());
        double lat2 = Math.toRadians(end.getLatitude());
        double dLat = Math.toRadians(end.getLatitude() - start.getLatitude());
        double dLon = Math.toRadians(end.getLongitude() - start.getLongitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
