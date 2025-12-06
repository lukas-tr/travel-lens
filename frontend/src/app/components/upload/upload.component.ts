import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { TimelineService } from '../../services/timeline.service';

@Component({
    selector: 'app-upload',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './upload.component.html',
    styleUrls: ['./upload.component.css']
})
export class UploadComponent {
    isDragging = false;
    isUploading = false;
    uploadProgress = 0;
    errorMessage = '';
    selectedFile: File | null = null;

    constructor(
        private timelineService: TimelineService,
        private router: Router
    ) { }

    onDragOver(event: DragEvent): void {
        event.preventDefault();
        event.stopPropagation();
        this.isDragging = true;
    }

    onDragLeave(event: DragEvent): void {
        event.preventDefault();
        event.stopPropagation();
        this.isDragging = false;
    }

    onDrop(event: DragEvent): void {
        event.preventDefault();
        event.stopPropagation();
        this.isDragging = false;

        const files = event.dataTransfer?.files;
        if (files && files.length > 0) {
            this.handleFile(files[0]);
        }
    }

    onFileSelected(event: Event): void {
        const input = event.target as HTMLInputElement;
        if (input.files && input.files.length > 0) {
            this.handleFile(input.files[0]);
        }
    }

    private handleFile(file: File): void {
        this.errorMessage = '';

        if (!file.name.endsWith('.json')) {
            this.errorMessage = 'Please select a JSON file';
            return;
        }

        this.selectedFile = file;
        this.uploadFile();
    }

    private uploadFile(): void {
        if (!this.selectedFile) return;

        this.isUploading = true;
        this.uploadProgress = 0;

        // Simulate progress
        const progressInterval = setInterval(() => {
            if (this.uploadProgress < 90) {
                this.uploadProgress += 10;
            }
        }, 200);

        this.timelineService.uploadTimeline(this.selectedFile).subscribe({
            next: (response) => {
                clearInterval(progressInterval);
                this.uploadProgress = 100;

                setTimeout(() => {
                    // Automatically analyze after upload
                    this.analyzeData();
                }, 500);
            },
            error: (error) => {
                clearInterval(progressInterval);
                this.isUploading = false;
                this.uploadProgress = 0;
                this.errorMessage = error.error?.error || 'Failed to upload file. Please try again.';
            }
        });
    }

    private analyzeData(): void {
        this.timelineService.analyzeTimeline().subscribe({
            next: (analysis) => {
                // Navigate to dashboard with analysis results
                this.router.navigate(['/dashboard']);
            },
            error: (error) => {
                this.isUploading = false;
                this.errorMessage = error.error?.error || 'Failed to analyze data. Please try again.';
            }
        });
    }
}
