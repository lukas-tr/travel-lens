import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { TimelineService } from '../../services/timeline.service';
import { TravelAnalysis, TransportMode, ImpactLevel } from '../../models/travel.model';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
    analysis: TravelAnalysis | null = null;
    isLoading = true;
    error = '';

    // For circular progress animation
    circleCircumference = 2 * Math.PI * 90; // radius = 90
    circleDashoffset = this.circleCircumference;

    constructor(
        private timelineService: TimelineService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.loadAnalysis();
    }

    loadAnalysis(): void {
        this.timelineService.getAnalysis().subscribe({
            next: (data) => {
                this.analysis = data;
                this.isLoading = false;
                this.animateGreenScore();
            },
            error: (error) => {
                this.error = 'Failed to load analysis. Please upload a file first.';
                this.isLoading = false;
                setTimeout(() => {
                    this.router.navigate(['/']);
                }, 2000);
            }
        });
    }

    animateGreenScore(): void {
        if (!this.analysis) return;

        const score = this.analysis.greenScore.totalScore;
        const offset = this.circleCircumference - (score / 100) * this.circleCircumference;

        setTimeout(() => {
            this.circleDashoffset = offset;
        }, 100);
    }

    getTransportModeIcon(mode: string): string {
        const icons: { [key: string]: string } = {
            'CAR': '🚗',
            'BUS': '🚌',
            'TRAIN': '🚆',
            'TRAM': '🚊',
            'SUBWAY': '🚇',
            'BICYCLE': '🚴',
            'WALKING': '🚶',
            'MOTORCYCLE': '🏍️',
            'AIRPLANE': '✈️',
            'FERRY': '⛴️'
        };
        return icons[mode] || '🚗';
    }

    getImpactBadgeClass(level: ImpactLevel): string {
        switch (level) {
            case ImpactLevel.HIGH:
                return 'badge-success';
            case ImpactLevel.MEDIUM:
                return 'badge-warning';
            case ImpactLevel.LOW:
                return 'badge-info';
            default:
                return 'badge-info';
        }
    }

    getRatingColor(rating: string): string {
        if (rating.startsWith('A')) return 'var(--success)';
        if (rating === 'B') return 'var(--primary-green)';
        if (rating === 'C') return 'var(--warning)';
        return 'var(--danger)';
    }

    formatDistance(km: number): string {
        return km.toFixed(1);
    }

    formatCO2(kg: number): string {
        return kg.toFixed(2);
    }

    formatCost(eur: number): string {
        return eur.toFixed(2);
    }

    formatTime(minutes: number): string {
        const hours = Math.floor(minutes / 60);
        const mins = Math.floor(minutes % 60);
        return hours > 0 ? `${hours}h ${mins}m` : `${mins}m`;
    }

    getTopSuggestions() {
        return this.analysis?.suggestions.slice(0, 5) || [];
    }

    uploadNewFile(): void {
        this.router.navigate(['/']);
    }
}
