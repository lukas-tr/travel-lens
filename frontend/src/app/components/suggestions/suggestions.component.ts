import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { TimelineService } from '../../services/timeline.service';
import { TravelAnalysis, Suggestion, ImpactLevel } from '../../models/travel.model';

@Component({
    selector: 'app-suggestions',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './suggestions.component.html',
    styleUrls: ['./suggestions.component.css']
})
export class SuggestionsComponent implements OnInit {
    analysis: TravelAnalysis | null = null;
    isLoading = true;
    error = '';
    Math = Math; // Expose Math to template

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

    goBack(): void {
        this.router.navigate(['/dashboard']);
    }
}
