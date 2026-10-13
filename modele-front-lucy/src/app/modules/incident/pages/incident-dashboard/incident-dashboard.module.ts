import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IncidentDashboardComponent } from './incident-dashboard.component';

@NgModule({
  declarations: [IncidentDashboardComponent],
  imports: [CommonModule, FormsModule],
  exports: [IncidentDashboardComponent]
})
export class IncidentDashboardModule { }