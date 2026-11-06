import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { interval, Subscription } from 'rxjs';
import { AccountService } from '../../../system-sop/services/account.service';
import { NotificationService } from '../../services/notification.service';

declare interface RouteInfo {
  path: string;
  title: string;
  icon: string;
  class: string;
}
export const ROUTES: RouteInfo[] = [
  {
    path: '/system-sop/liste-sop',
    title: 'CONSULTER HISTORIQUES',
    icon: 'education_agenda-bookmark',
    class: '',
  },
  {
    path: '/system-sop/creation-sop',
    title: 'CREER NOUVEAU SOP',
    icon: 'files_paper',
    class: '',
  },
];

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css'],
})
export class SidebarComponent implements OnDestroy, OnInit {
  menuItems: any[] = [];
  config: any;
  unreadNotifications = 0;
  private notificationSubscription?: Subscription;
  private notificationPollingSubscription?: Subscription;
  constructor(private httpClient: HttpClient, private account: AccountService, private notificationService: NotificationService) {}

  ngOnInit() {
    const headers = new HttpHeaders()
      .set('Cache-Control', 'no-cache')
      .set('Pragma', 'no-cache');

    this.httpClient
      .get<any>('/assets/config.json', { headers })
      .subscribe((config) => {
        this.config = config;
      });
    this.menuItems = ROUTES.filter((menuItem) => menuItem);
    this.notificationSubscription = this.notificationService.unreadCount$.subscribe(
      count => this.unreadNotifications = count
    );
    const matricule = this.account.getMatricule();
    if (matricule) {
      this.refreshUnreadCount(matricule);
      this.notificationPollingSubscription = interval(30000).subscribe(() => {
        this.refreshUnreadCount(matricule);
      });
    }
  }

  ngOnDestroy(): void {
    this.notificationSubscription?.unsubscribe();
    this.notificationPollingSubscription?.unsubscribe();
  }

  private refreshUnreadCount(matricule: string): void {
    this.notificationService.countUnread(matricule).subscribe({
      next: count => this.notificationService.setUnreadCount(count),
      error: error => console.error('Erreur compteur notifications', error),
    });
  }
}
