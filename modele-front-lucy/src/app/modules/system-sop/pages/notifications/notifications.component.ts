import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Notification } from '../../../general/model/notification';
import { NotificationService } from '../../../general/services/notification.service';
import { AccountService } from '../../services/account.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss'],
})
export class NotificationsComponent implements OnInit {
  notifications: Notification[] = [];
  loading = false;
  constructor(
    private service: NotificationService,
    private account: AccountService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    const matricule = this.account.getMatricule();
    if (!matricule) return;

    this.service.markAllRead(matricule).subscribe({
      next: () => {
        this.service.setUnreadCount(0);
        this.load(matricule);
      },
      error: error => {
        console.error('Erreur lecture des notifications', error);
        this.toastr.error('Les notifications n’ont pas pu être marquées comme lues.');
        this.load(matricule);
      },
    });
  }
  load(matricule: string): void {
    this.loading = true;
    this.service.find(matricule).subscribe({
      next: result => {
        this.notifications = result;
        this.service.setUnreadCount(result.filter(notification => notification.vue === 'N').length);
        this.loading = false;
      },
      error: error => { console.error('Erreur chargement notifications', error); this.loading = false; },
    });
  }
  open(notification: Notification): void {
    const matricule = this.account.getMatricule();
    if (!matricule) return;
    const openSop = () => this.router.navigate(['/system-sop/vue-sop'], { queryParams: { idSop: notification.idSop } });
    if (notification.vue === 'N') {
      this.service.markRead(notification.idNotification, matricule).subscribe({
        next: () => {
          notification.vue = 'O';
          this.refreshUnread(matricule);
          openSop();
        },
        error: error => {
          console.error('Erreur lecture notification', error);
          this.toastr.error('La notification n’a pas pu être marquée comme lue.');
        },
      });
    } else openSop();
  }
  remove(notification: Notification, event: Event): void {
    event.stopPropagation();
    const matricule = this.account.getMatricule(); 
    if (!matricule) return;
    this.service.remove(notification.idNotification, matricule).subscribe({
      next: () => {
        this.notifications = this.notifications.filter(item => item.idNotification !== notification.idNotification);
        this.refreshUnread(matricule);
        this.toastr.success('Notification supprimée.');
      },
      error: error => {
        console.error('Erreur suppression notification', error);
        this.toastr.error('La notification n’a pas pu être supprimée.');
      },
    });
  }

  private refreshUnread(matricule: string): void {
    this.service.countUnread(matricule).subscribe({
      next: count => this.service.setUnreadCount(count),
      error: error => console.error('Erreur mise à jour du compteur', error),
    });
  }
  roleLabel(type: string): string {
    const labels: { [key: string]: string } = {
      DESTINATAIRE: 'Destinataire',
      CONTACT_COMMENTAIRE: 'Agent commentaire',
      EQUIPE_PROJET: 'Équipe projet',
    };
    return type
      .split(',')
      .map(role => labels[role.trim()] || role.trim())
      .filter(Boolean)
      .join(' / ');
  }
}
