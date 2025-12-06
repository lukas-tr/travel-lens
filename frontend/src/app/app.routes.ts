import { Routes } from '@angular/router';
import { UploadComponent } from './components/upload/upload.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { SuggestionsComponent } from './components/suggestions/suggestions.component';

export const routes: Routes = [
    { path: '', component: UploadComponent },
    { path: 'dashboard', component: DashboardComponent },
    { path: 'suggestions', component: SuggestionsComponent },
    { path: '**', redirectTo: '' }
];
