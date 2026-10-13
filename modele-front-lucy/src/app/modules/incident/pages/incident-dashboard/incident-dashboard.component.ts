import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface Incident {
  id: string;
  title: string;
  severity: 'low' | 'medium' | 'high' | 'critical';
  status: 'open' | 'in_progress' | 'resolved' | 'closed';
  startTime: Date;
  endTime: Date | null;
  resolutionTime: number | null;
}

interface AvailabilityStats {
  totalUptime: number;
  totalDowntime: number;
  availabilityPercentage: number;
  averageResolutionTime: number;
  incidentsCount: number;
}

@Component({
  selector: 'app-incident-dashboard',
  templateUrl: './incident-dashboard.component.html',
  styleUrls: ['./incident-dashboard.component.scss'],
})
export class IncidentDashboardComponent implements OnInit {
  incidents: Incident[] = [];
  stats: AvailabilityStats | null = null;
  isLoading = true;
  timeRange: '24h' | '7d' | '30d' | 'all' = '7d';

  ngOnInit(): void {
    this.loadData();
  }

  setTimeRange(range: '24h' | '7d' | '30d' | 'all'): void {
    this.timeRange = range;
    this.loadData();
  }

  get uptimeDisplay(): string {
    const minutes = this.stats?.totalUptime;
    if (minutes == null) return 'N/A uptime';
    
    if (minutes < 60) return `${minutes} min uptime`;
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return `${hours}h${mins > 0 ? ` ${mins}min` : ''} uptime`;
  }

  get downtimeHours(): string {
    if (!this.stats?.totalDowntime) return '0 heures';
    const hours = this.stats.totalDowntime / 60;
    return `${hours.toFixed(1)} heures`; // Formatage manuel équivalent à number:'1.1-1'
  }

  getResolvedIncidentsCount(): number {
    return this.incidents.filter(incident => incident.endTime !== null).length;
  }

  get resolutionTimeDisplay(): string {
  return this.formatDuration(this.stats?.averageResolutionTime);
  }

  get downtimeDisplay(): string {
    return this.formatDuration(this.stats?.totalDowntime);
  }

  formatDuration(minutes: number | null | undefined): string {
    if (minutes == null) return 'N/A'; // Gère à la fois null et undefined
    if (minutes < 60) return `${minutes} min`;
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return `${hours}h${mins > 0 ? ` ${mins}min` : ''}`;
  }

  filterIncidentsByStatus(status: string): number {
    return this.incidents.filter(i => i.status === status).length;
  }

  filterResolvedIncidents(): number {
    return this.incidents.filter(i => i.endTime !== null).length;
  }

  formatStatus(status: string): string {
    return status.replace('_', ' ');
  }

  private loadData(): void {
    this.isLoading = true;
    
    // Simulation de données
    const simulatedIncidents: Incident[] = [
      {
        id: 'INC-001',
        title: 'Serveur API indisponible',
        severity: 'high',
        status: 'closed',
        startTime: new Date(Date.now() - 86400000 * 2),
        endTime: new Date(Date.now() - 86400000 * 2 + 3600000 * 2),
        resolutionTime: 120,
      },
      {
        id: 'INC-002',
        title: 'Lenteur base de données',
        severity: 'medium',
        status: 'resolved',
        startTime: new Date(Date.now() - 86400000 * 1),
        endTime: new Date(Date.now() - 86400000 * 1 + 3600000 * 0.5),
        resolutionTime: 30,
      },
      {
        id: 'INC-003',
        title: 'Erreurs de validation',
        severity: 'low',
        status: 'open',
        startTime: new Date(Date.now() - 3600000 * 3),
        endTime: null,
        resolutionTime: null,
      },
      {
        id: 'INC-004',
        title: 'Panne complète',
        severity: 'critical',
        status: 'closed',
        startTime: new Date(Date.now() - 86400000 * 5),
        endTime: new Date(Date.now() - 86400000 * 5 + 3600000 * 6),
        resolutionTime: 360,
      },
    ];

    // Filtrage selon la période
    this.incidents = simulatedIncidents.filter(incident => {
      const now = new Date();
      switch (this.timeRange) {
        case '24h': return incident.startTime > new Date(now.getTime() - 86400000);
        case '7d': return incident.startTime > new Date(now.getTime() - 86400000 * 7);
        case '30d': return incident.startTime > new Date(now.getTime() - 86400000 * 30);
        default: return true;
      }
    });

    this.calculateStats();
    this.isLoading = false;
  }

  public calculateStats(): void {
    const now = new Date();
    const totalPeriod = this.timeRange === '24h' ? 86400000 : 
                      this.timeRange === '7d' ? 86400000 * 7 : 
                      this.timeRange === '30d' ? 86400000 * 30 : 
                      this.incidents.reduce((acc, incident) => {
                        const start = incident.startTime.getTime();
                        const end = incident.endTime?.getTime() || now.getTime();
                        return Math.max(acc, end - start);
                      }, 0);

    const resolvedIncidents = this.incidents.filter(i => i.endTime !== null);
    
    const totalDowntime = resolvedIncidents.reduce((acc, incident) => {
      return acc + ((incident.endTime!.getTime() - incident.startTime.getTime()) / 60000);
    }, 0);

    const totalUptime = (totalPeriod / 60000) - totalDowntime;
    const availabilityPercentage = (totalUptime / (totalPeriod / 60000)) * 100;

    const averageResolutionTime = resolvedIncidents.length > 0 
      ? resolvedIncidents.reduce((acc, incident) => acc + (incident.resolutionTime || 0), 0) / resolvedIncidents.length
      : 0;

    this.stats = {
      totalUptime,
      totalDowntime,
      availabilityPercentage,
      averageResolutionTime,
      incidentsCount: this.incidents.length,
    };
  }

  getSeverityColor(severity: string): string {
    switch (severity) {
      case 'critical': return '#ff3860';
      case 'high': return '#ff7043';
      case 'medium': return '#ffb74d';
      case 'low': return '#66bb6a';
      default: return '#9e9e9e';
    }
  }

  // Dans votre composant TS
    get availabilityPercentage(): string {
    return this.stats?.availabilityPercentage 
        ? `${this.stats.availabilityPercentage.toFixed(2)}%` 
        : 'N/A';
    }
    
  getStatusColor(status: string): string {
    switch (status) {
      case 'open': return '#ff3860';
      case 'in_progress': return '#2196f3';
      case 'resolved': return '#66bb6a';
      case 'closed': return '#9e9e9e';
      default: return '#9e9e9e';
    }
  }
}