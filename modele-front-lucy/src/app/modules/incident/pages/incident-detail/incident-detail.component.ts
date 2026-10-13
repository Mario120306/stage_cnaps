import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IncidentRayService } from '../../model/incidentRay.service';
import { Incident } from '../../model/incidentRay.model';

@Component({
  selector: 'app-incident-details',
  templateUrl: './incident-detail.component.html',
  styleUrls: ['./incident-detail.component.scss']
})
export class IncidentDetailComponent implements OnInit {
  incident: Incident = {
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
  };
  canEdit: boolean = false;
  isLoading = true;
  errorMessage = '';
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private incidentRayService: IncidentRayService,
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    
    if (!id) {
      this.handleInvalidId();
      return;
    }
    this.loadIncident(id);
  }

  private handleInvalidId(): void {
    this.errorMessage = 'ID d\'incident invalide';
    this.isLoading = false;
    // Redirection après 3 secondes
    setTimeout(() => {
      this.router.navigate(['/incidents']);
    }, 3000);
  }

  private loadIncident(id: string): void {
    this.isLoading = true;
    this.incident=this.incidentRayService.getIncidentById(id)
    this.isLoading = false;
    // this.incidentRayService.getIncidentById(id).subscribe({
    //   next: (data) => {
    //     this.incident = data;
    //     this.isLoading = false;
    //   },
    //   error: (err) => {
    //     this.isLoading = false;
    //     this.errorMessage = 'Erreur lors du chargement de l\'incident';
    //     console.error(err);
    //   }
    // });
  }

  checkEditPermissions(): void {
    // Implémentez votre logique de permissions ici
    this.canEdit = true; // Temporaire - à adapter
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

  goBack(): void {
    this.router.navigate(['/general/incident-dash']);
  }

  editIncident(): void {
    this.router.navigate(['/incidents/edit', this.incident.id]);
  }

  deleteIncident(): void {
    if(confirm('Êtes-vous sûr de vouloir supprimer cet incident ?')) {
      this.incidentRayService.deleteIncident(this.incident.id).subscribe({
        next: () => {
          this.router.navigate(['/incidents']);
        },
        error: (err) => console.error('Erreur suppression', err)
      });
    }
  }
}