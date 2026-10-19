import { Component, HostListener, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { environment } from 'src/environments/environment';

interface FlowchartStep {
    titre: string;
    pieceJointe: string;
    informations: string;
    niveau: string;
}

@Component({
    selector:'app-vueSOP',
    templateUrl:'./vue-sop.component.html',
    styleUrls:['./vue-sop.component.scss'],
})
export class VueSopComponent implements OnInit{
    idSop: string | null = null;
    detailFlowchartSelectionne: FlowchartStep | null = null;
    flowchartEtapes: FlowchartStep[] = [
        {
            titre: 'Ordre de Virement / Avis de Débit / Avis de Virement CCP [1h26mn]',
            pieceJointe: 'Ordre_virment.png',
            informations: 'Vérifier les informations du virement et joindre les documents nécessaires.',
            niveau: 'Niveau 1 (Ex : 1.1)'
        },
        {
            titre: 'Réception des Chèques et Mandats CCP [2h51mn]',
            pieceJointe: 'Reception_cheque.jpg',
            informations: 'Contrôler la réception des chèques et des mandats CCP.',
            niveau: 'Niveau 2 (Ex : 1.1.1)'
        }
    ];

    constructor(
        private toastr:ToastrService,
        private router:Router,
        private route:ActivatedRoute
    ){}
    ngOnInit(): void {
        this.idSop = this.route.snapshot.queryParamMap.get('idSop');
    }

    ouvrirDetailFlowchart(etape: FlowchartStep): void {
        this.detailFlowchartSelectionne = etape;
    }

    fermerDetailFlowchart(): void {
        this.detailFlowchartSelectionne = null;
    }

    @HostListener('document:keydown.escape')
    fermerDetailFlowchartAvecEchap(): void {
        this.fermerDetailFlowchart();
    }

    info():void{
        this.toastr.info('Message /notification INFO','');
    }
    success(): void {
        this.toastr.success('Message / notification SUCCESS', '');
    }

    warning(): void {
        this.toastr.warning('Message / notification WARNING', '');
    }

    error(): void {
        this.toastr.error('Message / notification ERROR', '');
    }

    annuler(): void {
        this.router.navigate(['/system-sop/liste-sop']);
    }

    modifier(): void {
        this.router.navigate(['/system-sop/creation-sop'], {
            queryParams: this.idSop ? { idSop: this.idSop } : {}
        });
    }

    supprimer(): void {
        if (window.confirm('Voulez-vous supprimer ce SOP ?')) {
            this.router.navigate(['/system-sop/liste-sop']);
        }
    }

    async telechargerPdf(): Promise<void> {
        if (!this.idSop) {
            this.toastr.error('Aucun SOP sélectionné pour le téléchargement.', 'Téléchargement PDF');
            return;
        }

        try {
            const response = await fetch(`${environment.apiUrl}/exports/last/${this.idSop}/download`, {
                method: 'GET',
                headers: {
                    Accept: 'application/pdf'
                }
            });

            if (!response.ok) {
                const text = await response.text();
                throw new Error(text || 'Aucun PDF exporté pour ce SOP.');
            }

            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            const backendFileName = response.headers.get('X-Pdf-Filename');
            const disposition = response.headers.get('Content-Disposition');
            const fileNameMatch = disposition?.match(/filename\*?=(?:UTF-8'')?"?([^";]+)"?/i);
            const fileName = fileNameMatch
                ? decodeURIComponent(fileNameMatch[1])
                : null;
            const finalFileName = backendFileName || fileName;
            if (!finalFileName) {
                throw new Error('Nom du fichier PDF indisponible dans la réponse backend.');
            }

            link.href = url;
            link.download = finalFileName;
            document.body.appendChild(link);
            link.click();
            link.remove();
            window.URL.revokeObjectURL(url);
            this.toastr.success('Téléchargement du PDF terminé.', 'PDF');
        } catch (error: any) {
            console.error('Erreur téléchargement PDF', error);
            this.toastr.error(error?.message || 'Le PDF n’a pas pu être téléchargé.', 'Téléchargement PDF');
        }
    }
}    