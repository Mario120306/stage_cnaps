import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Incident } from './incidentRay.model';

@Injectable({
  providedIn: 'root'
})
export class IncidentRayService {
  private apiUrl = 'api/incidents'; // Remplacez par votre endpoint API
  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

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
    statut: {id: 1, libelle:'nouveau'}
  };
  constructor(private http: HttpClient) {}

  /**
   * Récupère tous les incidents
   */
  getIncidents(): Observable<Incident[]> {
    return this.http.get<Incident[]>(this.apiUrl)
      .pipe(
        tap(_ => console.log('Incidents récupérés')),
        catchError(this.handleError<Incident[]>('getIncidents', []))
      );
  }

  /**
   * Récupère un incident par son ID
   */
  getIncidentById(id: string): Incident {
//   getIncidentById(id: string): Observable<Incident> {
    // const url = `${this.apiUrl}/${id}`;
    // return this.http.get<Incident>(url)
    //   .pipe(
    //     tap(_ => console.log(`Incident récupéré id=${id}`)),
    //     catchError(this.handleError<Incident>(`getIncident id=${id}`))
    //   );
    return this.incident;
  }

  /**
   * Crée un nouvel incident
   */
  createIncident(incident: Incident): Observable<Incident> {
    return this.http.post<Incident>(this.apiUrl, incident, this.httpOptions)
      .pipe(
        tap((newIncident: Incident) => console.log(`Incident créé id=${newIncident.id}`)),
        catchError(this.handleError<Incident>('createIncident'))
      );
  }

  /**
   * Met à jour un incident existant
   */
  updateIncident(incident: Incident): Observable<any> {
    const url = `${this.apiUrl}/${incident.id}`;
    return this.http.put(url, incident, this.httpOptions)
      .pipe(
        tap(_ => console.log(`Incident mis à jour id=${incident.id}`)),
        catchError(this.handleError<any>('updateIncident'))
      );
  }

  /**
   * Supprime un incident
   */
  deleteIncident(id: string): Observable<Incident> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.delete<Incident>(url, this.httpOptions)
      .pipe(
        tap(_ => console.log(`Incident supprimé id=${id}`)),
        catchError(this.handleError<Incident>('deleteIncident'))
      );
  }

  /**
   * Récupère les types d'incident disponibles
   */
  getIncidentTypes(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/types`)
      .pipe(
        catchError(this.handleError<any[]>('getIncidentTypes', []))
      );
  }

  /**
   * Récupère les catégories disponibles
   */
  getCategories(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/categories`)
      .pipe(
        catchError(this.handleError<any[]>('getCategories', []))
      );
  }

  /**
   * Gestion centralisée des erreurs
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(`${operation} a échoué: ${error.message}`);
      // Vous pouvez envoyer l'erreur à un service de logging
      // returnErrorToService(error);
      
      // Laissez l'application continuer en retournant un résultat vide
      return of(result as T);
    };
  }
}