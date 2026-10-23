import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { Utilisateur } from "../../model/utilisateur.model";
import { UtilisateurService } from "../../services/utilisateur.service";
import { SopDetails, SopFlowchartEtape, SopService, SopUpdatePayload } from "../../services/sop.service";

interface EditableFlowchart {
    idFlowchart: string;
    titre: string;
    numero: number;
    information: string;
    etapes: Array<SopFlowchartEtape & { information: string; numero: string }>;
}

@Component({
    selector: "app-modificationSOP",
    templateUrl: "./modification-sop.component.html",
    styleUrls: ["./modification-sop.component.scss"]
})
export class ModificationSopComponent implements OnInit {
    idSop: string | null = null;
    details: SopDetails | null = null;
    titre = "";
    references = "";
    flowcharts: EditableFlowchart[] = [];
    chargement = true;
    sauvegardeEnCours = false;
    rechercheForm = new FormGroup({
        contact: new FormControl(""),
        destinataire: new FormControl(""),
        equipe: new FormControl("")
    });
    contactsSuggerees: Utilisateur[] = [];
    destinatairesSuggerees: Utilisateur[] = [];
    actionDestinataire = "Approbation final";
    equipeSuggeree: Utilisateur[] = [];
    private rechercheContactId = 0;
    private rechercheDestinataireId = 0;
    private rechercheEquipeId = 0;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private sopService: SopService,
        private utilisateurService: UtilisateurService,
        private toastr: ToastrService
    ) {}

    async ngOnInit(): Promise<void> {
        this.idSop = this.route.snapshot.queryParamMap.get("idSop");
        if (!this.idSop) {
            this.chargement = false;
            this.toastr.error("Aucun SOP sélectionné.", "Modification");
            return;
        }
        try {
            this.details = await this.sopService.getDetails(this.idSop);
            this.titre = this.details.sop.titre || "";
            this.references = this.details.references.map(reference => reference.contenu || "").join("\n\n");
            let numeroTitre = 0;
            const compteurs = [0, 0, 0, 0];
            this.flowcharts = this.details.flowcharts
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
                        .sort((a, b) => (a.ordre || 0) - (b.ordre || 0))
                        .map(etape => {
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
                                information: etape.information || "",
                                numero: [numeroTitre, ...compteurs.slice(0, profondeur + 1)].join(".")
                            };
                        });

                    return {
                        idFlowchart: flowchart.idFlowchart,
                        titre,
                        numero: numeroTitre,
                        information: flowchart.information || "",
                        etapes
                    };
                });
        } catch (error) {
            console.error("Erreur lors du chargement du SOP à modifier", error);
            this.toastr.error("Les données du SOP n'ont pas pu être chargées.", "Modification");
        } finally {
            this.chargement = false;
        }
    }

    async rechercherContact(email: string): Promise<void> {
        const id = ++this.rechercheContactId;
        if (!email.trim()) {
            this.contactsSuggerees = [];
            return;
        }
        try {
            const resultats = await this.utilisateurService.searchByMail(email.trim());
            if (id === this.rechercheContactId) this.contactsSuggerees = resultats;
        } catch (error) {
            console.error("Erreur lors de la recherche des contacts", error);
            this.contactsSuggerees = [];
        }
    }

    ajouterContact(agent: Utilisateur): void {
        if (!this.details || this.details.contacts.some(contact =>
            contact.matricule?.toLowerCase() === agent.matricule.toLowerCase())) return;
        this.details.contacts.push({
            matricule: agent.matricule, email: agent.mail, ordre: this.details.contacts.length + 1
        });
        this.rechercheForm.controls.contact.setValue("");
        this.contactsSuggerees = [];
    }

    retirerContact(index: number): void {
        this.details?.contacts.splice(index, 1);
    }

    async rechercherDestinataire(matricule: string): Promise<void> {
        const id = ++this.rechercheDestinataireId;
        if (!matricule.trim()) {
            this.destinatairesSuggerees = [];
            return;
        }
        try {
            const resultats = await this.utilisateurService.searchByMatricule(matricule.trim());
            if (id === this.rechercheDestinataireId) this.destinatairesSuggerees = resultats;
        } catch (error) {
            console.error("Erreur lors de la recherche des destinataires", error);
            this.destinatairesSuggerees = [];
        }
    }

    ajouterDestinataire(agent: Utilisateur): void {
        if (!this.details || this.details.destinataires.some(item =>
            item.matricule?.toLowerCase() === agent.matricule.toLowerCase())) return;
        this.details.destinataires.push({
            matricule: agent.matricule, nom: `${agent.nom} ${agent.prenoms}`.trim(),
            organisation: agent.libelleService || "CNaPS", action: this.actionDestinataire,
            ordre: this.details.destinataires.length + 1
        });
        this.rechercheForm.controls.destinataire.setValue("");
        this.destinatairesSuggerees = [];
        this.actionDestinataire = "Approbation final";
    }

    retirerDestinataire(index: number): void {
        this.details?.destinataires.splice(index, 1);
    }

    async rechercherEquipeParMatricule(matricule: string): Promise<void> {
        const id = ++this.rechercheEquipeId;
        if (!matricule.trim()) {
            this.equipeSuggeree = [];
            return;
        }
        try {
            const resultats = await this.utilisateurService.searchByMatricule(matricule.trim());
            if (id === this.rechercheEquipeId) this.equipeSuggeree = resultats;
        } catch (error) {
            console.error("Erreur lors de la recherche de l'équipe", error);
            this.equipeSuggeree = [];
        }
    }

    ajouterMembre(agent: Utilisateur): void {
        if (!this.details || this.details.equipeProjet.some(item =>
            item.matricule?.toLowerCase() === agent.matricule.toLowerCase())) return;
        this.details.equipeProjet.push({
            matricule: agent.matricule, nom: `${agent.nom} ${agent.prenoms}`.trim(),
            fonction: agent.libelleFonction || "", telephone: agent.telGsm || "",
            email: agent.mail || "", ordre: this.details.equipeProjet.length + 1
        });
        this.rechercheForm.controls.equipe.setValue("");
        this.equipeSuggeree = [];
    }

    retirerMembre(index: number): void {
        this.details?.equipeProjet.splice(index, 1);
    }

    ajouterTerme(): void {
        this.details?.glossaire.push({ acronyme: "", terme: "", ordre: this.details.glossaire.length + 1 });
    }

    retirerTerme(index: number): void {
        this.details?.glossaire.splice(index, 1);
    }

    async enregistrer(): Promise<void> {
        if (!this.idSop || !this.details || this.sauvegardeEnCours) return;
        if (!this.titre.trim()) {
            this.toastr.warning("Le titre du SOP est obligatoire.", "Modification");
            return;
        }
        this.sauvegardeEnCours = true;
        try {
            const payload: SopUpdatePayload = {
                titre: this.titre.trim(),
                contacts: this.details.contacts.map((item, index) => ({
                    idContact: item.idContact, matricule: item.matricule || "", email: item.email || "", ordre: index + 1
                })),
                destinataires: this.details.destinataires.map((item, index) => ({
                    idDestinataire: item.idDestinataire, matricule: item.matricule || "", nom: item.nom || "",
                    organisation: item.organisation || "CNaPS", action: item.action || "", ordre: index + 1
                })),
                equipeProjet: this.details.equipeProjet.map((item, index) => ({
                    idMembre: item.idMembre, matricule: item.matricule || "", nom: item.nom || "",
                    fonction: item.fonction || "", telephone: item.telephone || "", email: item.email || "", ordre: index + 1
                })),
                glossaire: this.details.glossaire.map((item, index) => ({
                    idGlossaire: item.idGlossaire, acronyme: item.acronyme || "", terme: item.terme || "", ordre: index + 1
                })),
                references: [{ idReference: this.details.references[0]?.idReference, contenu: this.references, ordre: 1 }],
                flowcharts: this.flowcharts.map(flowchart => ({
                    idFlowchart: flowchart.idFlowchart,
                    information: flowchart.information || ""
                })),
                flowchartEtapes: this.flowcharts.reduce<SopUpdatePayload["flowchartEtapes"]>((all, flowchart) => all.concat(
                    flowchart.etapes.map((etape, index) => ({
                        idEtape: etape.idEtape,
                        idFlowchart: flowchart.idFlowchart,
                        information: etape.information || "",
                        niveau: etape.niveau || 1,
                        modeNumerotation: (etape.modeNumerotation === "MANUEL" ? "MANUEL" : "AUTO") as "AUTO" | "MANUEL",
                        ordre: index + 1
                    }))
                ), [])
            };
            await this.sopService.update(this.idSop, payload);
            this.toastr.success("Le SOP a été mis à jour et une nouvelle version a été créée.", "Modification");
            this.router.navigate(["/system-sop/vue-sop"], { queryParams: { idSop: this.idSop } });
        } catch (error) {
            console.error("Erreur lors de la mise à jour du SOP", error);
            this.toastr.error("Le SOP n'a pas pu être mis à jour.", "Modification");
        } finally {
            this.sauvegardeEnCours = false;
        }
    }

    annuler(): void {
        this.router.navigate(["/system-sop/vue-sop"], { queryParams: this.idSop ? { idSop: this.idSop } : {} });
    }
}
