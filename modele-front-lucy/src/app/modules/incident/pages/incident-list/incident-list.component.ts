import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { IncidentRayService } from '../../model/incidentRay.service';

@Component({
  selector: 'app-incident-list',
  templateUrl: './incident-list.component.html',
  styleUrls: ['./incident-list.component.scss']
})
export class IncidentListComponent implements OnInit {
  incidents: any[] = [];
  isLoading = true;

  constructor(
    private incidentService: IncidentRayService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadIncidents();
  }

  loadIncidents(): void {
    this.incidents = [
        {
            id: 'INC-001',
            demandeur: { matricule: 'EMP001', nom: 'John Doe' },
            servicesAffecte: [{ id: 'SVC001', nom: 'IT Support' }],
            description: 'Problème de serveur',
            type: { id: 'TYPE001', libelle: 'Matériel deffectueux' , categorie:{id: 'CAT001', libelle: 'Technique'}},
            dateCreation: '2023-05-15T10:30:00',
            dateConstatation: '2023-05-15T11:00:00',
            origineIncident: null,
            origineMalveillance: null,
            categorie: null,
            statut: {id: 1, libelle:'nouveau'}
        },
        {
            id: 'INC-002',
            demandeur: { matricule: 'EMP001', nom: 'John Doe' },
            servicesAffecte: [{ id: 'SVC001', nom: 'IT Support' }],
            description: 'Problème de serveur',
            type: { id: 'TYPE001', libelle: 'Matériel deffectueux' , categorie:{id: 'CAT001', libelle: 'Technique'}},
            dateCreation: '2023-05-15T10:30:00',
            dateConstatation: '2023-05-15T11:00:00',
            origineIncident: null,
            origineMalveillance: null,
            categorie: null,
            statut: {id: 1, libelle:'nouveau'}
        }
    ];
    this.isLoading = false;
    // this.incidentService.getIncidents().subscribe({
    //   next: (data) => {
    //     this.incidents = data;
    //     this.isLoading = false;
    //   },
    //   error: (err) => {
    //     console.error(err);
    //     this.isLoading = false;
    //   }
    // });
  }

  viewDetail(id: string): void {
    this.router.navigate(['/general/incidents', id]);
  }

  truncateText(text: string, limit: number = 50): string {
     if (!text) return '';
     return text.length > limit ? text.substring(0, limit) + '...' : text;
  }

  formatDate(date: string | Date): string {
        if (!date) return 'Date inconnue';
        
        const d = new Date(date);
        return `${this.padZero(d.getDate())}/${this.padZero(d.getMonth()+1)}/${d.getFullYear()}`;
        }

        private padZero(num: number): string {
        return num < 10 ? `0${num}` : `${num}`;
    }
    getStatusClass(statusLibelle: string): string {
    const defaultClass = 'badge-secondary';
    if (!statusLibelle) return defaultClass;
    
    switch(statusLibelle.toLowerCase()) {
        case 'nouveau': return 'badge-primary';
        case 'en cours': return 'badge-warning';
        case 'résolu': return 'badge-success';
        case 'clôturé': return 'badge-info';
        default: return defaultClass;
    }
  }
}