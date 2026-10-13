export interface Incident {
  id: string;
  title: string;
  severity: 'Critical' | 'High' | 'Medium' | 'Low';
  startTime: string;
  endTime: string | null;
  status: 'Resolved' | 'Ongoing' | 'Investigating';
  serviceAffected?: string;
}

export interface IncidentStats {
  totalIncidents: number;
  averageResolutionTime: number;
  maxResolutionTime: number;
  incidentsBySeverity: Record<string, number>;
  resolutionTrend: {
    date: string;
    avgResolution: number;
  }[];
}

export interface AvailabilityData {
  availabilityPercentage: number;
  totalDowntimeMinutes: number;
  uptimeMinutes: number;
  incidentsAffectingAvailability: number;
}