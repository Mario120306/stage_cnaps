import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileService {
  private apiUrl = 'api/files';

  constructor(private http: HttpClient) {}

  getFilesByIncident(incidentId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}?incidentId=${incidentId}`);
  }

  downloadFile(fileId: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${fileId}/download`, {
      responseType: 'blob'
    });
  }

  getFileUrl(fileId: string): string {
    return `${this.apiUrl}/${fileId}/download`;
  }
}