import { Component, HostListener, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";

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

    telechargerPdf(): void {
        window.print();
    }
}    