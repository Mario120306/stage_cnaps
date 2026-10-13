import { Component, OnInit } from '@angular/core';
import { Incident, Utilisateur, TypeIncident, OrigineIncident, 
         OrigineMalveillance, Impact, PlanAction, Service , Categorie} from '../../model/incidentRay.model';

@Component({
  selector: 'app-incident-form',
  templateUrl: './incident-form.component.html',
  styleUrls: ['./incident-form.component.scss']
})
export class IncidentFormComponent implements OnInit {
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

  incidentk: {
    origineIncident: OrigineIncident | null;
    origineMalveillance: OrigineMalveillance | null;
  } = {
    origineIncident: null,
    origineMalveillance: null
  };

  // Données pour les selects
  utilisateurs: Utilisateur[] = [
    { matricule: 'EMP001', nom: 'John Doe' },
    { matricule: 'EMP002', nom: 'Jane Smith' }
  ];

  categories: any[]= [
    { id: 'CAT001', libelle: 'Technique' },
    { id: 'CAT002', libelle: 'Sécurité' },
    { id: 'CAT003', libelle: 'Réseau' }
  ];

  typesIncident: any[] = [
    { id: 'TYPE001', libelle: 'Matériel', categorie:{id: 'CAT001', libelle: 'Technique'}},
    { id: 'TYPE002', libelle: 'Réseau', categorie:{id: 'CAT001', libelle: 'Technique'} },
    { id: 'TYPE003', libelle: 'Hameçonnage', categorie: { id: 'CAT002', libelle: 'Sécurité' } },
    { id: 'TYPE004', libelle: 'Intrusion', categorie: { id: 'CAT002', libelle: 'Sécurité' } },
    { id: 'TYPE005', libelle: 'Perte de données', categorie: { id: 'CAT002', libelle: 'Sécurité' } },
    { id: 'TYPE006', libelle: 'Problème de connexion', categorie: { id: 'CAT003', libelle: 'Réseau' } },
    { id: 'TYPE007', libelle: 'Débit lent', categorie: { id: 'CAT003', libelle: 'Réseau' } }
  ];

  originesIncident: OrigineIncident[] = [
    { id: 'ORG001', libelle: 'Malveillance' },
    { id: 'ORG002', libelle: 'Externe' }
  ];

  originesMalveillance: OrigineMalveillance[] = [
    { id: 'MAL001', libelle: 'Interne' },
    { id: 'MAL002', libelle: 'Externe' }
  ];

  niveauxImpact: Impact[] = [
    { id: 'IMP001', niveau: 'Faible' },
    { id: 'IMP002', niveau: 'Moyen' }
  ];

  plansAction: PlanAction[] = [
    { id: 'PLN001', libelle: 'Plan A' },
    { id: 'PLN002', libelle: 'Plan B' }
  ];

  services: Service[] = [
    { id: 'SVC001', nom: 'IT Support' },
    { id: 'SVC002', nom: 'Réseau' }
  ];

  selectedCategory: any = null;
  filteredTypes: TypeIncident[] = [];
  showMalveillanceField : boolean = false;;

  // Méthode appelée quand la catégorie change
  onCategoryChange(): void {
    // Filtre les types selon la catégorie sélectionnée
    this.filteredTypes = this.typesIncident.filter(
      type => type.categorie && type.categorie.id === this.selectedCategory?.id
    );

    // Réinitialise le type si la catégorie change
    if (this.incident.type && !this.filteredTypes.some(t => t.id === this.incident.type?.id)) {
      this.incident.type = null;
    }
  }

  onCategorieChange() {
    if (!this.incident.type?.categorie) {
      this.filteredTypes = [];
      this.incident.type = null; // Réinitialiser le type sélectionné
      return;
    }

    // Filtrer les types par catégorie
    this.filteredTypes = this.typesIncident.filter(
      type => type.categorie.id === this.incident.categorie?.id
    );

    // Réinitialiser le type si incompatible avec la nouvelle catégorie
    // if (this.incident.type && this.incident.type.categorieId !== this.incident.categorie.id) {
    //   this.incident.type = null;
    // }
  }
  
  // Méthode appelée quand l'origine change
  onOriginChange(): void {
    this.showMalveillanceField = this.incident.origineIncident?.libelle?.toLowerCase().includes('malveillance') ?? false;
    if (!this.showMalveillanceField) {
      this.incident.origineMalveillance = null;
    }
  }

  constructor() { }

  ngOnInit(): void {
    // Initialisation supplémentaire si nécessaire
  }

  onSubmit(): void {
    console.log('Incident à enregistrer:', this.incident);
    // Ici, vous ajouteriez la logique pour sauvegarder l'incident
    // this.incidentService.create(this.incident).subscribe(...);
  }

  onCancel(): void {
    // Logique pour annuler et retourner à la liste
  }

  onTypeSelected() {
    // La catégorie est disponible via incident.type.categorie
    console.log('Catégorie du type sélectionné :', this.incident.type?.categorie);
  }

  getFilteredTypes() {
    if (!this.typesIncident) return [];
    
    const selectedCategoryId = this.incident.type?.categorie?.id;
    if (!selectedCategoryId) return this.typesIncident;

    return this.typesIncident.filter(type => type.categorie?.id === selectedCategoryId);
  }

//   onFileChange(event: any): void {
//     const files = event.target.files;
//     if (files && files.length > 0) {
//       // Traiter les fichiers ici
//     }
//   }
  uploadedFiles: File[] = [];

    onFileChange(event: any): void {
    const files: FileList = event.target.files;
    if (files && files.length > 0) {
        // Convert FileList to Array and filter by size/type if needed
        this.uploadedFiles = Array.from(files).filter(file => 
        file.size <= 10 * 1024 * 1024 // 10MB max
        );
        
        // Optionally show error for files that were too large
        const invalidFiles = Array.from(files).filter(file => 
        file.size > 10 * 1024 * 1024
        );
        
        if (invalidFiles.length > 0) {
        alert(`Certains fichiers dépassent la taille maximale de 10MB`);
        }
    }
    }

removeFile(index: number): void {
  this.uploadedFiles.splice(index, 1);
}

formatFileSize(bytes: number): string {
  if (bytes === 0) return '0 Bytes';
  const k = 1024;
  const sizes = ['Bytes', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + ' ' + sizes[i];
}

// Ajoutez cette méthode à votre onSubmit()
prepareFormData(): FormData {
  const formData = new FormData();
  
  // Ajoutez les données du formulaire
  formData.append('incident', JSON.stringify(this.incident));
  
  // Ajoutez les fichiers
  this.uploadedFiles.forEach(file => {
    formData.append('attachments', file, file.name);
  });
  
  return formData;
}

}