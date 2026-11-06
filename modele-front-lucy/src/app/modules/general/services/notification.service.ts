import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Notification } from '../model/notification';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private readonly url = `${environment.apiUrl}/notifications`;
  private readonly unreadSubject = new BehaviorSubject<number>(0);
  readonly unreadCount$ = this.unreadSubject.asObservable();
  constructor(private http: HttpClient) {}

  find(matricule: string): Observable<Notification[]> {
    return this.http.get<Notification[]>(this.url, { params: new HttpParams().set('matricule', matricule) });
  }
  countUnread(matricule: string): Observable<number> {
    return this.http.get<number>(`${this.url}/count`, { params: new HttpParams().set('matricule', matricule) });
  }
  setUnreadCount(count: number): void {
    this.unreadSubject.next(Math.max(0, count));
  }
  decrementUnread(): void {
    this.setUnreadCount(this.unreadSubject.value - 1);
  }
  markRead(id: string, matricule: string): Observable<void> {
    return this.http.patch<void>(`${this.url}/${id}/lue`, null, { params: new HttpParams().set('matricule', matricule) });
  }
  markAllRead(matricule: string): Observable<void> {
    return this.http.patch<void>(`${this.url}/lues`, null, {
      params: new HttpParams().set('matricule', matricule),
    });
  }
  remove(id: string, matricule: string): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`, { params: new HttpParams().set('matricule', matricule) });
  }
}
