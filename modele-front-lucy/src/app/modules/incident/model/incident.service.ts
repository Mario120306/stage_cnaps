import { Injectable } from '@angular/core';
import { Incident, IncidentStats, AvailabilityData } from './incident.model';
import { CommonModule } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class IncidentService {
  async fetchIncidents(timeRange: string): Promise<Incident[]> {
    // Implémentation réelle irait chercher les données d'une API
    // Voici une implémentation simulée
    return this.generateMockIncidents(timeRange);
  }

  calculateStats(incidents: Incident[]): IncidentStats {
    const resolvedIncidents = incidents.filter(i => i.endTime);
    
    const totalResolutionTime = resolvedIncidents.reduce((sum, incident) => {
      return sum + ((new Date(incident.endTime!).getTime() - new Date(incident.startTime).getTime()));
    }, 0);
    
    const averageResolutionTime = resolvedIncidents.length > 0 
      ? Math.round(totalResolutionTime / resolvedIncidents.length / 60000)
      : 0;
    
    const maxResolutionTime = resolvedIncidents.length > 0
      ? Math.max(...resolvedIncidents.map(incident => 
          (new Date(incident.endTime!).getTime() - new Date(incident.startTime).getTime()) / 60000
        ))
      : 0;
    
    const incidentsBySeverity = incidents.reduce((acc, incident) => {
      acc[incident.severity] = (acc[incident.severity] || 0) + 1;
      return acc;
    }, {} as Record<string, number>);
    
    const resolutionTrend = incidents.reduce((acc, incident) => {
      const date = new Date(incident.startTime).toLocaleDateString();
      const existingEntry = acc.find(e => e.date === date);
      
      const resolutionTime = incident.endTime 
        ? (new Date(incident.endTime).getTime() - new Date(incident.startTime).getTime()) / 60000
        : 0;
      
      if (existingEntry) {
        existingEntry.avgResolution = 
          (existingEntry.avgResolution * existingEntry.count + resolutionTime) / (existingEntry.count + 1);
        existingEntry.count++;
      } else {
        acc.push({
          date,
          avgResolution: resolutionTime,
          count: 1
        });
      }
      
      return acc;
    }, [] as { date: string; avgResolution: number; count: number }[])
    .map(({ date, avgResolution }) => ({ date, avgResolution: Math.round(avgResolution) }))
    .sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime());
    
    return {
      totalIncidents: incidents.length,
      averageResolutionTime,
      maxResolutionTime,
      incidentsBySeverity,
      resolutionTrend
    };
  }

  calculateAvailability(incidents: Incident[], timeRange: string): AvailabilityData {
    let totalPeriodMinutes: number;
    switch (timeRange) {
      case '24h': totalPeriodMinutes = 24 * 60; break;
      case '7days': totalPeriodMinutes = 7 * 24 * 60; break;
      case '30days': totalPeriodMinutes = 30 * 24 * 60; break;
      default: totalPeriodMinutes = 30 * 24 * 60;
    }
    
    const totalDowntimeMinutes = incidents.reduce((sum, incident) => {
      if (!incident.endTime) {
        const downtimeMs = new Date().getTime() - new Date(incident.startTime).getTime();
        return sum + Math.round(downtimeMs / 60000);
      } else {
        const downtimeMs = new Date(incident.endTime).getTime() - new Date(incident.startTime).getTime();
        return sum + Math.round(downtimeMs / 60000);
      }
    }, 0);
    
    const uptimeMinutes = totalPeriodMinutes - totalDowntimeMinutes;
    const availabilityPercentage = Math.round((uptimeMinutes / totalPeriodMinutes) * 100 * 100) / 100;
    
    return {
      availabilityPercentage,
      totalDowntimeMinutes,
      uptimeMinutes,
      incidentsAffectingAvailability: incidents.length
    };
  }

  private generateMockIncidents(timeRange: string): Incident[] {
    // Implémentation de génération de données fictives
    // Similaire à l'exemple précédent
    const now = new Date();
    const data: Incident[] = [];
    let daysToGenerate = 30;
    
    if (timeRange === '24h') daysToGenerate = 1;
    else if (timeRange === '7days') daysToGenerate = 7;
    
    for (let i = 0; i < daysToGenerate; i++) {
      const date = new Date(now);
      date.setDate(date.getDate() - i);
      
      const incidentsCount = Math.floor(Math.random() * 4);
      
      for (let j = 0; j < incidentsCount; j++) {
        const severityRoll = Math.random();
        let severity: 'Critical' | 'High' | 'Medium' | 'Low';
        if (severityRoll < 0.1) severity = 'Critical';
        else if (severityRoll < 0.3) severity = 'High';
        else if (severityRoll < 0.6) severity = 'Medium';
        else severity = 'Low';
        
        const startTime = new Date(date);
        startTime.setHours(Math.floor(Math.random() * 24));
        startTime.setMinutes(Math.floor(Math.random() * 60));
        
        const endTime = new Date(startTime);
        const resolutionMinutes = 5 + Math.floor(Math.random() * 475);
        endTime.setMinutes(endTime.getMinutes() + resolutionMinutes);
        
        const isOngoing = Math.random() < 0.1;
        
        data.push({
          id: `INC-${date.getDate()}${i}${j}`,
          title: `Incident sur service ${severity}`,
          severity,
          startTime: startTime.toISOString(),
          endTime: isOngoing ? null : endTime.toISOString(),
          status: isOngoing ? 'Ongoing' : 'Resolved',
          serviceAffected: ['API', 'Database', 'Frontend', 'Backend'][Math.floor(Math.random() * 4)],
        });
      }
    }
    
    return data;
  }
}