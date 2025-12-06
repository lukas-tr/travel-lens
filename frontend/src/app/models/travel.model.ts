export enum TransportMode {
    WALKING = 'WALKING',
    RUNNING = 'RUNNING',
    ON_FOOT = 'ON_FOOT',
    ON_BICYCLE = 'ON_BICYCLE',
    IN_VEHICLE = 'IN_VEHICLE',
    IN_ROAD_VEHICLE = 'IN_ROAD_VEHICLE',
    IN_PASSENGER_VEHICLE = 'IN_PASSENGER_VEHICLE',
    IN_RAIL_VEHICLE = 'IN_RAIL_VEHICLE',
    UNKNOWN = 'UNKNOWN'
}

export enum ImpactLevel {
    HIGH = 'HIGH',
    MEDIUM = 'MEDIUM',
    LOW = 'LOW'
}

export interface Location {
    latitude: number;
    longitude: number;
    timestampMs?: number;
    accuracy?: number;
}

export interface Trip {
    id: string;
    startLocation: Location;
    endLocation: Location;
    startTime: number;
    endTime: number;
    transportMode: TransportMode;
    distanceKm: number;
    durationMinutes: number;
    co2EmissionsKg: number;
    costEur: number;
}

export interface GreenScore {
    totalScore: number;
    co2Score: number;
    costScore: number;
    sustainabilityScore: number;
    rating: string;
    message: string;
}

export interface Suggestion {
    id: string;
    tripId: string;
    currentMode: TransportMode;
    suggestedMode: TransportMode;
    title: string;
    description: string;
    impactLevel: ImpactLevel;
    co2SavingsKg: number;
    costSavingsEur: number;
    timeDifferenceMinutes: number;
    route: string;
}

export interface TravelAnalysis {
    totalTrips: number;
    totalDistanceKm: number;
    totalCo2EmissionsKg: number;
    totalCostEur: number;
    totalTimeMinutes: number;
    tripsByMode: { [key: string]: number };
    distanceByMode: { [key: string]: number };
    co2ByMode: { [key: string]: number };
    greenScore: GreenScore;
    suggestions: Suggestion[];
    potentialCo2SavingsKg: number;
    potentialCostSavingsEur: number;
}
