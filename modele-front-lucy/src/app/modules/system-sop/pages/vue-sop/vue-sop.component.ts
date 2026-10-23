import { Component, HostListener, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { environment } from "src/environments/environment";
import { Sop } from "../../model/sop.model";
import { SopDetails, SopFlowchartEtape } from "../../services/sop.service";
import { SopService } from "../../services/sop.service";

interface FlowchartStep {
    idFlowchart: string;
    titre: string;
    numero: number;
    pieceJointe: string;
    informations: string;
    niveau: string;
    etapes: NumberedFlowchartEtape[];
    idPieceJointe: string | null;
}

interface NumberedFlowchartEtape extends SopFlowchartEtape {
    numero: string;
}

@Component({
    selector: "app-vueSOP",
    templateUrl: "./vue-sop.component.html",
    styleUrls: ["./vue-sop.component.scss"]
})
export class VueSopComponent implements OnInit {
    idSop: string | null = null;
    sop: Sop | null = null;
    details: SopDetails | null = null;
    flowchartEtapes: FlowchartStep[] = [];
    detailFlowchartSelectionne: FlowchartStep | null = null;
    confirmationSuppression = false;
    confirmationEnFermeture = false;
    suppressionEnCours = false;
    chargement = true;

    constructor(
        private toastr: ToastrService,
        private router: Router,
        private route: ActivatedRoute,
        private sopService: SopService
    ) {}

    async ngOnInit(): Promise<void> {
        this.idSop = this.route.snapshot.queryParamMap.get("idSop");
        if (!this.idSop) {
            this.chargement = false;
            this.toastr.error("Aucun SOP sélectionné.", "Erreur");
            return;
        }

        try {
            this.details = await this.sopService.getDetails(this.idSop);
            this.sop = this.details.sop;
            let numeroTitre = 0;
            const compteurs = [0, 0, 0, 0];
            this.flowchartEtapes = this.details.flowcharts
                .slice()
                .sort((a, b) => (a.ordre || 0) - (b.ordre || 0))
                .map(flowchart => {
                    const titre = flowchart.titre || "";
                    if (titre.trim()) {
                        numeroTitre++;
                        compteurs.fill(0);
                    } else if (numeroTitre === 0) {
                        numeroTitre = 1;
                    }

                    const etapes = this.details!.flowchartEtapes
                        .filter(etape => etape.idFlowchart === flowchart.idFlowchart)
                        .sort((a, b) => (a.ordre || 0) - (b.ordre || 0));
                    const etapesNumerotees = etapes.map(etape => {
                        const profondeur = Math.max(0, Math.min(compteurs.length - 1, (etape.niveau || 1) - 1));
                        for (let index = 0; index < profondeur; index++) {
                            if (compteurs[index] === 0) {
                                compteurs[index] = 1;
                            }
                        }
                        compteurs[profondeur]++;
                        for (let index = profondeur + 1; index < compteurs.length; index++) {
                            compteurs[index] = 0;
                        }

                        return {
                            ...etape,
                            numero: [numeroTitre, ...compteurs.slice(0, profondeur + 1)].join(".")
                        };
                    });
                    const pieceJointe = this.details!.piecesJointes.find(
                        piece => piece.idPieceJointe === flowchart.idPieceJointe
                    );
                    return {
                        idFlowchart: flowchart.idFlowchart,
                        titre,
                        numero: numeroTitre,
                        pieceJointe: pieceJointe?.nomFichier || "Aucune pièce jointe",
                        informations: flowchart.information || "",
                        niveau: etapesNumerotees.map(etape => `Niveau ${etape.niveau ?? "-"}`).join(", "),
                        etapes: etapesNumerotees,
                        idPieceJointe: flowchart.idPieceJointe
                    };
                });
        } catch (error) {
            console.error("Erreur lors du chargement du SOP", error);
            this.toastr.error("Les données du SOP n'ont pas pu être chargées.", "Erreur");
        } finally {
            this.chargement = false;
        }
    }

    ouvrirDetailFlowchart(etape: FlowchartStep): void {
        this.detailFlowchartSelectionne = etape;
    }

    fermerDetailFlowchart(): void {
        this.detailFlowchartSelectionne = null;
    }

    @HostListener("document:keydown.escape")
    fermerDetailFlowchartAvecEchap(): void {
        this.fermerDetailFlowchart();
        if (!this.suppressionEnCours) {
            this.fermerConfirmationSuppression();
        }
    }

    pieceJointeNom(idPieceJointe: string | null): string {
        return this.details?.piecesJointes.find(piece => piece.idPieceJointe === idPieceJointe)?.nomFichier
            || "Aucune pièce jointe";
    }

    pieceJointeImage(idPieceJointe: string | null): string | null {
        const piece = this.details?.piecesJointes.find(item => item.idPieceJointe === idPieceJointe);
        if (!piece?.contenu) {
            return null;
        }
        if (piece.contenu.startsWith("data:")) {
            return piece.contenu;
        }
        return `data:${piece.typeMime || "image/*"};base64,${piece.contenu}`;
    }

    get referencesText(): string {
        return this.details?.references?.length
            ? this.details.references.map(reference => reference.contenu || "").join("\n\n")
            : "Aucune référence";
    }

    annuler(): void {
        this.router.navigate(["/system-sop/liste-sop"]);
    }

    modifier(): void {
        this.router.navigate(["/system-sop/modification-sop"], {
            queryParams: this.idSop ? { idSop: this.idSop } : {}
        });
    }

    ouvrirConfirmationSuppression(): void {
        if (!this.idSop) {
            this.toastr.error("Aucun SOP sélectionné pour la suppression.", "Suppression");
            return;
        }
        this.confirmationSuppression = true;
    }

    fermerConfirmationSuppression(): void {
        if (!this.confirmationSuppression || this.confirmationEnFermeture) {
            return;
        }
        // Laisse jouer l'animation de sortie avant de retirer le modal du DOM
        this.confirmationEnFermeture = true;
        setTimeout(() => {
            this.confirmationSuppression = false;
            this.confirmationEnFermeture = false;
        }, 150);
    }

    async supprimer(): Promise<void> {
        if (!this.idSop) {
            this.toastr.error("Aucun SOP sélectionné pour la suppression.", "Suppression");
            return;
        }

        this.suppressionEnCours = true;
        try {
            await this.sopService.softDelete(this.idSop);
            this.toastr.success("Le SOP a été supprimé avec succès.", "Suppression");
            this.confirmationSuppression = false;
            this.router.navigate(["/system-sop/liste-sop"]);
        } catch (error: any) {
            console.error("Erreur lors de la suppression du SOP", error);
            this.toastr.error(error?.message || "La suppression du SOP a échoué.", "Suppression");
        } finally {
            this.suppressionEnCours = false;
        }
    }

    async telechargerPdf(): Promise<void> {
        if (!this.idSop) {
            this.toastr.error("Aucun SOP sélectionné pour le téléchargement.", "Téléchargement PDF");
            return;
        }
        try {
            const response = await fetch(`${environment.apiUrl}/exports/last/${this.idSop}/download`, {
                headers: { Accept: "application/pdf" }
            });
            if (!response.ok) {
                throw new Error((await response.text()) || "Aucun PDF exporté pour ce SOP.");
            }
            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement("a");
            const fileName = response.headers.get("X-Pdf-Filename") || `sop-${this.sop?.numeroSop || this.idSop}.pdf`;
            link.href = url;
            link.download = fileName;
            document.body.appendChild(link);
            link.click();
            link.remove();
            window.URL.revokeObjectURL(url);
            this.toastr.success("Téléchargement du PDF terminé.", "PDF");
        } catch (error: any) {
            console.error("Erreur téléchargement PDF", error);
            this.toastr.error(error?.message || "Le PDF n'a pas pu être téléchargé.", "Téléchargement PDF");
        }
    }
}