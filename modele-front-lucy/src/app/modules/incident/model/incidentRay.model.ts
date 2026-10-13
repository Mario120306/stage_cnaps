export interface Utilisateur {
  matricule: string;
  nom: string;
}

 export interface Service {
  id: string;
  nom: string;
}

export interface Categorie {
  id: string;
  libelle: string;
}

export interface TypeIncident {
  id: string;
  libelle: string;
  categorie?: Categorie;
}

export interface OrigineIncident {
  id: string;
  libelle: string;
}

export interface OrigineMalveillance {
  id: string;
  libelle: string;
}

export interface Impact {
  id: string;
  niveau: string;
}

export interface PlanAction {
  id: string;
  libelle: string;
}

export interface Statut {
  id: Number;
  libelle: string;
}

export interface Incident {
  id: string;
  demandeur: Utilisateur;
  traiteur?: Utilisateur;
  statut?: Statut;
  servicesAffecte: Service[];
  description: string;
  type: TypeIncident | null;
  origineIncident?: OrigineIncident | null;
  origineMalveillance?: OrigineMalveillance | null;
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
  categorie: Categorie | null;
}