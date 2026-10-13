import { Component, OnInit } from '@angular/core';
import { IncidentService } from '../../model/incident.service';
import { Incident, IncidentStats, AvailabilityData } from '../../model/incident.model';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  incidents: Incident[] = [];
  stats: IncidentStats | null = null;
  availability: AvailabilityData | null = null;
  loading = true;
  error: string | null = null;
  timeRange = '7days';
  isMobile = false;
  
  
  timeRanges = [
    { value: '24h', label: '24 Heures' },
    { value: '7days', label: '7 Jours' },
    { value: '30days', label: '30 Jours' }
  ];

  severityLevels = ['Critical', 'High', 'Medium', 'Low'];

  constructor(private incidentService: IncidentService) {}

  ngOnInit(): void {
    this.loadData();
    this.checkScreenSize();
    window.addEventListener('resize', () => this.checkScreenSize());
  }

  checkScreenSize() {
    this.isMobile = window.innerWidth < 768;
  }

  getDisplayTrendData() {
    const data = this.prepareTrendData();
    if (this.isMobile && data.length > 7) {
      return data.filter((_, i) => i % 2 === 0);
    }
    return data;
  }

  prepareTrendData() {
    if (!this.incidents || this.incidents.length === 0) return [];
    
    // Grouper les incidents par date et calculer la moyenne
    const trendsMap = new Map<string, number[]>();
    
    this.incidents.forEach(incident => {
      const date = new Date(incident.startTime).toLocaleDateString();
      const resolutionTime = this.getResolutionTime(incident);
      
      if (resolutionTime > 0) {
        if (!trendsMap.has(date)) {
          trendsMap.set(date, []);
        }
        trendsMap.get(date)?.push(resolutionTime);
      }
    });

    // Convertir en format pour le graphique
    return Array.from(trendsMap.entries()).map(([date, times]) => {
      const avg = times.reduce((a, b) => a + b, 0) / times.length;
      return {
        date,
        avgResolution: Math.round(avg)
      };
    });
  }
  
  async loadData(): Promise<void> {
    try {
      this.loading = true;
      this.incidents = await this.incidentService.fetchIncidents(this.timeRange);
      this.stats = this.incidentService.calculateStats(this.incidents);
      this.availability = this.incidentService.calculateAvailability(this.incidents, this.timeRange);
      this.error = null;
    } catch (err) {
      this.error = 'Échec du chargement des données';
      console.error(err);
    } finally {
      this.loading = false;
    }
  }

  changeTimeRange(range: string): void {
    this.timeRange = range;
    this.loadData();
  }

  getSeverityCount(severity: string): number {
    return this.stats?.incidentsBySeverity[severity] || 0;
  }

  getSeverityWidth(severity: string): number {
    if (!this.stats?.totalIncidents) return 0;
    return (this.getSeverityCount(severity) / this.stats.totalIncidents) * 100;
  }

  getTrendHeight(avgResolution: number): number {
    if (!this.stats?.maxResolutionTime) return 0;
    return (avgResolution / this.stats.maxResolutionTime) * 100;
  }

  getResolutionTime(incident: Incident): number {
    if (!incident.endTime) return 0;
    const start = new Date(incident.startTime).getTime();
    const end = new Date(incident.endTime).getTime();
    return Math.round((end - start) / 60000);
  }
}