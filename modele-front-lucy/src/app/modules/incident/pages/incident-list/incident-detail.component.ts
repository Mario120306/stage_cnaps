import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IncidentRayService } from '../../model/incidentRay.service';
import { FileService } from '../../model/file.service';

@Component({
  selector: 'app-incident-detail',
  templateUrl: './incident-detail.component.html',
  styleUrls: ['./incident-detail.component.scss']
})
export class IncidentDetaileComponent implements OnInit {
  incident: any;
  files: any[] = [];
  isLoading = true;

  constructor(
    private route: ActivatedRoute,
    private incidentService: IncidentRayService,
    private fileService: FileService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadIncident(id);
    //   this.loadFiles(id);
    }
  }

  loadIncident(id: string): void {
    this.incident=this.incidentService.getIncidentById(id)
    this.isLoading = false;
  }

  loadFiles(incidentId: string): void {
    this.fileService.getFilesByIncident(incidentId).subscribe({
      next: (files) => {
        this.files = files;
      },
      error: (err) => console.error(err)
    });
  }

  downloadFile(fileId: string): void {
    this.fileService.downloadFile(fileId).subscribe(blob => {
      // Implémentez le téléchargement du fichier
    });
  }

  getFileIconClass(fileType: string): string {
    const type = fileType?.toLowerCase() || '';
    
    // Mapping des types MIME aux classes FontAwesome
    const iconMap: {[key: string]: string} = {
        'application/pdf': 'fas fa-file-pdf',
        'application/msword': 'fas fa-file-word',
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document': 'fas fa-file-word',
        'application/vnd.ms-excel': 'fas fa-file-excel',
        'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': 'fas fa-file-excel',
        'image/jpeg': 'fas fa-file-image',
        'image/png': 'fas fa-file-image',
        'text/plain': 'fas fa-file-alt',
        'application/zip': 'fas fa-file-archive'
    };

    return iconMap[type] || 'fas fa-file'; // Icône par défaut
    }
    formatFileSize(bytes: number = 0): string {
        if (bytes === 0) return '0 Bytes';
        
        const k = 1024;
        const sizes = ['Bytes', 'KB', 'MB', 'GB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        
        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    }
    formatDate(date: string | Date): string {
        if (!date) return 'Date inconnue';
        
        const d = new Date(date);
        return `${this.padZero(d.getDate())}/${this.padZero(d.getMonth()+1)}/${d.getFullYear()}`;
        }

        private padZero(num: number): string {
        return num < 10 ? `0${num}` : `${num}`;
    }
    
}