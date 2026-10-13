import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { ActivatedRoute, Router } from '@angular/router';

interface Utilisateur {
  matricule: string;
  nom: string;
}

interface Service {
  id: string;
  nom: string;
}

interface TypeIncident {
  id: string;
  libelle: string;
}

interface OrigineIncident {
  id: string;
  libelle: string;
}

interface OrigineMalveillance {
  id: string;
  libelle: string;
}

interface Impact {
  id: string;
  niveau: string;
}

interface PlanAction {
  id: string;
  libelle: string;
}

interface Incident {
  id: string;
  demandeur: Utilisateur;
  traiteur?: Utilisateur;
  servicesAffecte: Service[];
  description: string;
  type: TypeIncident;
  origineIncident?: OrigineIncident;
  origineMalveillance?: OrigineMalveillance;
  cause?: string;
  actifImpact?: string;
  niveauImpact?: Impact;
  planAction?: PlanAction;
  resolutionInterne?: boolean;
  observationComplementaire?: string;
  observateur?: Utilisateur;
  observation?: string;
  dateCreation: string;
  dateConstatation?: string;
  dateValidation?: string;
  dateCloture?: string;
}

@Component({
  selector: 'app-incident-dash',
  templateUrl: './incident-dash.component.html',
  styleUrls: ['./incident-dash.component.scss']
})
export class IncidentDashComponent implements OnInit {
  @ViewChild('typeChart') typeChartRef!: ElementRef;
  @ViewChild('monthlyChart') monthlyChartRef!: ElementRef;
  incidents: Incident[] = [];
  filteredIncidents: Incident[] = [];
  isModalOpen = false;
  currentIncident: Incident | null = null;
  searchTerm = '';
  loading = false;
  showForm = false;

  utilisateurs: Utilisateur[] = [
    { matricule: 'EMP001', nom: 'Jean Dupont' },
    { matricule: 'EMP002', nom: 'Marie Martin' },
    { matricule: 'EMP003', nom: 'Pierre Durand' },
    { matricule: 'EMP004', nom: 'Sophie Lambert' }
  ];

  services: Service[] = [
    { id: 'SVC001', nom: 'Réseau et Infrastructure' },
    { id: 'SVC002', nom: 'Systèmes d\'Information' },
    { id: 'SVC003', nom: 'Sécurité Informatique' },
    { id: 'SVC004', nom: 'Support Utilisateurs' }
  ];

  typesIncident: TypeIncident[] = [
    { id: 'TYPE001', libelle: 'Incident Réseau' },
    { id: 'TYPE002', libelle: 'Problème Matériel' },
    { id: 'TYPE003', libelle: 'Erreur Logicielle' },
    { id: 'TYPE004', libelle: 'Demande d\'Assistance' }
  ];

  originesIncident: OrigineIncident[] = [
    { id: 'ORG001', libelle: 'Interne' },
    { id: 'ORG002', libelle: 'Externe' },
    { id: 'ORG003', libelle: 'Maintenance' },
    { id: 'ORG004', libelle: 'Mise à Jour' }
  ];

  originesMalveillance: OrigineMalveillance[] = [
    { id: 'MAL001', libelle: 'Hameçonnage' },
    { id: 'MAL002', libelle: 'Malware' },
    { id: 'MAL003', libelle: 'Accès Non Autorisé' },
    { id: 'MAL004', libelle: 'Attaque DDoS' }
  ];

  niveauxImpact: Impact[] = [
    { id: 'IMP001', niveau: 'Critique' },
    { id: 'IMP002', niveau: 'Élevé' },
    { id: 'IMP003', niveau: 'Moyen' },
    { id: 'IMP004', niveau: 'Faible' }
  ];

  plansAction: PlanAction[] = [
    { id: 'PLN001', libelle: 'Plan de Secours Réseau' },
    { id: 'PLN002', libelle: 'Procédure de Rétablissement' },
    { id: 'PLN003', libelle: 'Contingence Système' },
    { id: 'PLN004', libelle: 'Plan de Continuité' }
  ];

  newIncident: Partial<Incident> = {
    dateCreation: new Date().toISOString().slice(0, 16),
    servicesAffecte: []
  };

  stats = {
    totalIncidents: 0,
    openIncidents: 0,
    closedIncidents: 0,
    avgResolutionTime: 0
  };

  constructor(private router: Router,) {
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    this.fetchIncidents();
    this.calculateStats();
    setTimeout(() => {
      this.initCharts();
    }, 100);
  }

  calculateStats(): void {
    // Exemple de données - remplacer par vos calculs réels
    this.stats = {
      totalIncidents: 42,
      openIncidents: 15,
      closedIncidents: 27,
      avgResolutionTime: 4.5
    };
  }

  initCharts(): void {
    new Chart(this.typeChartRef.nativeElement, {
      type: 'pie',
      data: {
        labels: ['Matériel', 'Réseau', 'Logiciel', 'Sécurité'],
        datasets: [{
          data: [12, 19, 8, 3],
          backgroundColor: [
            '#FF6384',
            '#36A2EB',
            '#FFCE56',
            '#4BC0C0'
          ]
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'right',
            labels: {
              boxWidth: 20,
              padding: 15,
              usePointStyle: true,
              pointStyle: 'circle'
            }
          }
        },
        layout: {
          padding: {
            left: 20,
            right: 20,
            top: 20,
            bottom: 20
          }
        }
      }
    });
    // Graphique en barres (évolution mensuelle)
    new Chart(this.monthlyChartRef.nativeElement, {
      type: 'bar',
      data: {
        labels: ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin'],
        datasets: [{
          label: 'Incidents',
          data: [12, 19, 3, 5, 2, 3],
          backgroundColor: '#4BC0C0'
        }]
      },
      options: {
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  }

  fetchIncidents(): void {
    this.loading = true;
    // Simulation de données
    setTimeout(() => {
      this.incidents = [
        {
          id: 'INC-001',
          demandeur: { matricule: 'EMP001', nom: 'John Doe' },
          servicesAffecte: [{ id: 'SVC001', nom: 'IT Support' }],
          description: 'Problème de serveur',
          type: { id: 'TYPE001', libelle: 'Matériel' },
          dateCreation: '2023-05-15T10:30:00',
          dateConstatation: '2023-05-15T11:00:00'
        },
        {
          id: 'INC-002',
          demandeur: { matricule: 'EMP002', nom: 'Jane Smith' },
          traiteur: { matricule: 'EMP003', nom: 'Mike Johnson' },
          servicesAffecte: [{ id: 'SVC002', nom: 'Réseau' }],
          description: 'Problème de connexion WiFi',
          type: { id: 'TYPE002', libelle: 'Réseau' },
          dateCreation: '2023-05-16T09:15:00',
          dateCloture: '2023-05-16T14:30:00'
        }
      ];
      this.filteredIncidents = [...this.incidents];
      this.loading = false;
    }, 1000);
  }

  onSubmit(): void {
    // Génération d'un ID aléatoire
    this.newIncident.id = 'INC-' + Math.floor(Math.random() * 10000);
    
    // Conversion des dates
    if (this.newIncident.dateCreation) {
      this.newIncident.dateCreation = new Date(this.newIncident.dateCreation).toISOString();
    }

    // Ajout à la liste
    this.incidents.unshift(this.newIncident as Incident);
    this.filteredIncidents = [...this.incidents];
    
    // Réinitialisation
    this.resetForm();
    this.calculateStats();
  }

  resetForm(): void {
    this.newIncident = {
      dateCreation: new Date().toISOString().slice(0, 16),
      servicesAffecte: []
    };
    this.showForm = false;
  }

  onSearchChange(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.searchTerm = target.value.toLowerCase();
    this.filterIncidents();
  }

  filterIncidents(): void {
    this.filteredIncidents = this.incidents.filter(incident =>
      incident.id.toLowerCase().includes(this.searchTerm) ||
      incident.demandeur.nom.toLowerCase().includes(this.searchTerm) ||
      incident.description.toLowerCase().includes(this.searchTerm)
    );
  }

  handleRowClick(event: MouseEvent, incident: Incident): void {
    // Vérifie si le clic vient d'un élément interactif (bouton, lien, etc.)
    const target = event.target as HTMLElement;
    if (target.closest('button, a, [role="button"]')) {
      return;
    }
    
    // Navigation vers le détail
    this.router.navigate(['/general/incident-detail', incident.id]);
  }


  handleCreate(): void {
    this.currentIncident = null;
    this.isModalOpen = true;
    this.router.navigate(['general/incident-form']);
  }

  handleList(): void {
    this.router.navigate(['general/incidents']);
  }
  handleEdit(incident: Incident): void {
    this.currentIncident = {...incident};
    this.isModalOpen = true;
  }

  handleDelete(id: string): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet incident?')) {
      this.incidents = this.incidents.filter(inc => inc.id !== id);
      this.filterIncidents();
    }
  }

  handleSubmit(event: Event): void {
    event.preventDefault();
    // Logique de sauvegarde
    this.closeModal();
  }

  closeModal(): void {
    this.isModalOpen = false;
  }

  formatDate(dateString?: string): string {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('fr-FR') + ' ' + date.toLocaleTimeString('fr-FR');
  }

  getStatusClass(incident: Incident): string {
    let statusClass = 'status-badge ';
    if (incident.dateCloture) {
      statusClass += 'closed';
    } else if (incident.dateValidation) {
      statusClass += 'validated';
    } else {
      statusClass += 'new';
    }
    return statusClass;
  }

  getStatusText(incident: Incident): string {
    if (incident.dateCloture) return 'Clôturé';
    if (incident.dateValidation) return 'Validé';
    return 'Nouveau';
  }
}