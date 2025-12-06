import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TravelAnalysis } from '../models/travel.model';

@Injectable({
    providedIn: 'root'
})
export class TimelineService {
    private apiUrl = 'http://localhost:8080/api/timeline';

    constructor(private http: HttpClient) { }

    /**
     * Upload Timeline JSON file
     */
    uploadTimeline(file: File): Observable<any> {
        const formData = new FormData();
        formData.append('file', file);

        return this.http.post(`${this.apiUrl}/upload`, formData);
    }

    /**
     * Analyze uploaded timeline data
     */
    analyzeTimeline(): Observable<TravelAnalysis> {
        return this.http.post<TravelAnalysis>(`${this.apiUrl}/analyze`, {});
    }

    /**
     * Get analysis results
     */
    getAnalysis(): Observable<TravelAnalysis> {
        return this.http.get<TravelAnalysis>(`${this.apiUrl}/analysis`);
    }

    /**
     * Get suggestions
     */
    getSuggestions(): Observable<any> {
        return this.http.get(`${this.apiUrl}/suggestions`);
    }

    /**
     * Get all trips
     */
    getTrips(): Observable<any> {
        return this.http.get(`${this.apiUrl}/trips`);
    }

    /**
     * Health check
     */
    healthCheck(): Observable<any> {
        return this.http.get(`${this.apiUrl}/health`);
    }
}
