import { Component, OnInit } from "@angular/core";
import { ActivatedRoute,Router } from "@angular/router";
import { ToastrService} from "ngx-toastr";
import { Utilisateur} from "../../model/utilisateur.model";
import { AccountService } from "../../services/account.service";
import { SopService, SopCreationPayload, SopPieceJointeCreation } from "../../services/sop.service";
import { UtilisateurService } from "../../services/utilisateur.service";
import { VersionService } from "../../services/version.service";
import { Flowchart } from "../../model/flowchart.model";
import { DetailFlowchart, DetailFlowchartAffiche } from "../../model/detail-flowchart.model";
import { AnnexeDraft } from "../../model/annexe.model";
import { Service as ServiceModel } from "../../model/service.model";
import { ServiceService } from "../../services/service.service";


@Component({
    selector: 'app-creationSOP',
    templateUrl: './creation-sop.component.html',
    styleUrls: ['./creation-sop.component.scss'],
})
export class CreationSopComponent implements OnInit {

    idSop: string | null = null;
    isModification = false;
    enteteValidee = false;
    destinatairesValides = false;
    equipeProjetValides=false;
    glossaireValides=false;
    titreDocument = "";
    services: ServiceModel[] = [];
    codeServiceSelectionne = "";
    numeroVersion: number | null = 1;
    soumissionEnCours = false;
    soumissionTerminee = false;
    rechercheEmail = "";
    rechercheEquipe="";
    acronymeClossaire="";
    termesGlossaire="";
    actionDestinataire = "Approbation final";
    glossaire:{acronyme:string;termes:string} []=[];
    agentsEmailSuggerees: Utilisateur[] = [];
    destinatairesSuggereesBackend: Utilisateur[] = [];
    equipeSuggereesBackend: Utilisateur[] = [];
    private rechercheEmailId = 0;
    private rechercheDestinataireId = 0;
    private rechercheEquipeId = 0;
    agentsSelectionnes: Utilisateur[]=[];
    equipeProjet:Utilisateur[]=[];
    matriculeDestinataire = "";
    destinataireSelectionne: Utilisateur | null = null;
    destinataires: { agent: Utilisateur; action: string }[] = [];
    flowcharts: Flowchart[] = [];
    flowchartsValides = false;
    titreFlowchart = "";
    informationFlowchart = "";
    fichierFlowchart: File | null = null;
    private prochainFlowchartId = 1;
    private prochainDetailId = 1;
    flowchartDetailId: number | null = null;
    informationsDetailFlowchart = "";
    niveauDecalageDetail: number | null = null;
    detailsFlowchart: DetailFlowchart[] = [];
    detailsFlowchartValides = false;
    niveauxDecalage = [
        { valeur: 1, libelle: "Niveau 1 (Ex : 1.1)" },
        { valeur: 2, libelle: "Niveau 2 (Ex : 1.1.1)" },
        { valeur: 3, libelle: "Niveau 3 (Ex : 1.1.1.1)" },
        { valeur: 4, libelle: "Niveau 4 (Ex : 1.1.1.1.1)" }
    ];
    detailFlowchartOuvert = false;
    detailFlowchartSelectionne: Flowchart | null = null;

    // Références légales et réglementaires
    referencesLegales = "";
    referencesValidees = false;

    // Annexes
    annexes: AnnexeDraft[] = [];
    annexesValidees = false;
    titreAnnexe = "";
    fichierAnnexe: File | null = null;
    private prochaineAnnexeId = 1;

    constructor(
        private toastr: ToastrService,
        private route: ActivatedRoute,
        private versionService: VersionService,
        private utilisateurService: UtilisateurService,
        private accountService: AccountService,
        private sopService: SopService,
        private serviceService: ServiceService,
        private router: Router,
    ) {}

    async ngOnInit(): Promise<void> {
        try {
            this.services = await this.serviceService.getAll();
        } catch (error) {
            console.error('Erreur lors du chargement des services', error);
            this.toastr.error('Impossible de charger la liste des services.', 'Erreur');
        }

        this.idSop = this.route.snapshot.queryParamMap.get('idSop');
        if (!this.idSop) {
            return;
        }

        this.isModification = true;
        this.numeroVersion = null;
        try {
            this.numeroVersion = await this.versionService.countVersionByIdSop(this.idSop) + 1;
        } catch (error) {
            console.error('Erreur lors du chargement du numéro de version', error);
            this.toastr.error('Impossible de charger le numéro de version du SOP.', 'Erreur');
        }
    }

    get agentsSuggerees(): Utilisateur[] {
        const recherche = this.rechercheEmail.trim().toLocaleLowerCase();
        if (!recherche || this.enteteValidee) {
            return [];
        }

        return this.agentsEmailSuggerees
            .filter(agent =>
                agent.mail &&
                !this.agentsSelectionnes.some(
                    selectionne => selectionne.mail.toLocaleLowerCase() === agent.mail.toLocaleLowerCase()
                ) &&
                `${agent.mail} ${agent.nom} ${agent.prenoms}`.toLocaleLowerCase().includes(recherche)
            )
            .slice(0, 8);
    }

    get destinatairesSuggerees(): Utilisateur[] {
        const recherche = this.matriculeDestinataire.trim().toLocaleLowerCase();
        if (this.destinatairesValides ||
            !recherche ||
            (this.destinataireSelectionne &&
                this.destinataireSelectionne.matricule.trim().toLocaleLowerCase() === recherche)) {
            return [];
        }

        return this.destinatairesSuggereesBackend
            .filter(agent =>
                agent.matricule &&
                !this.destinataires.some(
                    destinataire => destinataire.agent.matricule === agent.matricule
                ) &&
                agent.matricule.toLocaleLowerCase().includes(recherche)
            )
            .slice(0, 8);
    }
    get equipeSuggeree():Utilisateur[]{
        const recherche =this.rechercheEquipe.trim().toLocaleLowerCase();
        if(!recherche){
            return[];
        }
        return this.equipeSuggereesBackend
                .filter(agent => 
                    agent.matricule && 
                    !this.equipeProjet.some(
                        membre => membre.matricule.toLocaleLowerCase() === agent.matricule.toLocaleLowerCase()
                    ) &&
                    `${agent.matricule} ${agent.nom} ${agent.prenoms} ${agent.mail}`
                        .toLocaleLowerCase()
                        .includes(recherche)
                )
                .slice(0,8);
    }

    async rechercherParEmail(email: string): Promise<void> {
        const recherche = email.trim();
        const rechercheId = ++this.rechercheEmailId;
        if (!recherche || this.enteteValidee) {
            this.agentsEmailSuggerees = [];
            return;
        }
        try {
            const resultats = await this.utilisateurService.searchByMail(recherche);
            if (rechercheId === this.rechercheEmailId) {
                this.agentsEmailSuggerees = resultats;
            }
        } catch (error) {
            console.error('Erreur lors de la recherche des agents par email', error);
            if (rechercheId === this.rechercheEmailId) {
                this.agentsEmailSuggerees = [];
            }
        }
    }

    async rechercherDestinataire(matricule: string): Promise<void> {
        const recherche = matricule.trim();
        const rechercheId = ++this.rechercheDestinataireId;
        if (!recherche || this.destinatairesValides) {
            this.destinatairesSuggereesBackend = [];
            return;
        }
        try {
            const resultats = await this.utilisateurService.searchByMatricule(recherche);
            if (rechercheId === this.rechercheDestinataireId) {
                this.destinatairesSuggereesBackend = resultats;
            }
        } catch (error) {
            console.error('Erreur lors de la recherche des agents par matricule', error);
            if (rechercheId === this.rechercheDestinataireId) {
                this.destinatairesSuggereesBackend = [];
            }
        }
    }

    async rechercherEquipeParMatricule(matricule: string): Promise<void> {
        const recherche = matricule.trim();
        const rechercheId = ++this.rechercheEquipeId;
        if (!recherche) {
            this.equipeSuggereesBackend = [];
            return;
        }
        try {
            const resultats = await this.utilisateurService.searchByMatricule(recherche);
            if (rechercheId === this.rechercheEquipeId) {
                this.equipeSuggereesBackend = resultats;
            }
        } catch (error) {
            console.error('Erreur lors de la recherche des agents par matricule', error);
            if (rechercheId === this.rechercheEquipeId) {
                this.equipeSuggereesBackend = [];
            }
        }
    }
    selectionnerDestinataire(agent: Utilisateur): void {
        if (this.destinatairesValides) {
            return;
        }
        this.matriculeDestinataire = agent.matricule;
        this.destinataireSelectionne = agent;
    }

    modifierMatriculeDestinataire(matricule: string): void {
        if (this.destinatairesValides) {
            return;
        }
        this.matriculeDestinataire = matricule;
        if (this.destinataireSelectionne &&
            this.destinataireSelectionne.matricule !== matricule) {
            this.destinataireSelectionne = null;
        }
    }
    ajouterMembreEquipe(agent:Utilisateur):void{
        if(this.equipeProjet.some(
            membre=>membre.matricule.toLocaleLowerCase()===agent.matricule.toLocaleLowerCase()
        )){
            return;
        }
        this.equipeProjet=[...this.equipeProjet,agent];
        this.rechercheEquipe="";
    }
    ajouterAgent(agent: Utilisateur): void {
        if (this.enteteValidee || !agent.mail || this.agentsSelectionnes.some(
            selectionne => selectionne.mail.toLocaleLowerCase() === agent.mail.toLocaleLowerCase()
        )) {
            return;
        }

        this.agentsSelectionnes = [...this.agentsSelectionnes, agent];
        this.rechercheEmail = "";
    }
    ajouterDestinataire(): void {
        if (this.destinatairesValides) {
            return;
        }
        const matricule = this.matriculeDestinataire.trim().toLocaleLowerCase();
        const action = this.actionDestinataire.trim();

        if (!matricule || !action) {
            this.toastr.warning('Saisissez le matricule et l’action du destinataire.', 'Information manquante');
            return;
        }

        const agent = this.destinataireSelectionne &&
            this.destinataireSelectionne.matricule.trim().toLocaleLowerCase() === matricule
            ? this.destinataireSelectionne
            : this.destinatairesSuggereesBackend.find(
            utilisateur => utilisateur.matricule.trim().toLocaleLowerCase() === matricule
        );
        if (!agent) {
            this.toastr.warning('Aucun agent ne correspond à ce matricule.', 'Destinataire introuvable');
            return;
        }

        if (this.destinataires.some(
            destinataire => destinataire.agent.matricule.trim().toLocaleLowerCase() === matricule
        )) {
            this.toastr.warning('Cet agent figure déjà dans la liste des destinataires.', 'Destinataire déjà ajouté');
            return;
        }

        this.destinataires = [...this.destinataires, { agent, action }];
        this.matriculeDestinataire = "";
        this.destinataireSelectionne = null;
        this.destinatairesSuggereesBackend = [];
        this.actionDestinataire = "Approbation final";
    }
    private estImageValide(fichier: File): boolean {
        const typesAcceptes = [
            'image/png',
            'image/jpeg',
            'image/jpg',
            'image/gif',
            'image/webp',
            'image/svg+xml',
            'image/bmp',
            'application/pdf'
        ];
        const tailleMax = 5 * 1024 * 1024; // 5 Mo

        if (!typesAcceptes.includes(fichier.type)) {
            this.toastr.warning(
                `Le fichier "${fichier.name}" n'est pas une image valide (formats acceptés : PNG, JPG, GIF, WEBP, SVG, BMP,PDF).`,
                'Fichier invalide'
            );
            return false;
        }

        if (fichier.size > tailleMax) {
            this.toastr.warning(
                `Le fichier "${fichier.name}" dépasse la taille maximale de 5 Mo.`,
                'Fichier trop volumineux'
            );
            return false;
        }

        return true;
    }
    selectionnerFichierFlowchart(event: Event): void {
        const input = event.target as HTMLInputElement;
        const fichier = input.files?.[0] ?? null;

        if (!fichier) {
            this.fichierFlowchart = null;
            return;
        }

        if (!this.estImageValide(fichier)) {
            input.value = ''; // reset le input
            this.fichierFlowchart = null;
            return;
        }

        this.fichierFlowchart = fichier;
    }

    ajouterFlowchart(input: HTMLInputElement): void {
        if (this.flowchartsValides) {
            return;
        }

        const titre = this.titreFlowchart.trim();
        if (!this.fichierFlowchart) {
            this.toastr.warning(
                "choisissez une pièce jointe.",
                "Information manquante"
            );
            return;
        }

        const flowchart = new Flowchart(
            this.prochainFlowchartId++,
            titre,
            this.informationFlowchart.trim(),
            this.fichierFlowchart
        );
        this.flowcharts = [...this.flowcharts, flowchart];
        this.flowchartDetailId = flowchart.id;

        this.titreFlowchart = "";
        this.informationFlowchart = "";
        this.fichierFlowchart = null;
        input.value = "";
    }

    retirerFlowchart(id: number): void {
        if (this.flowchartsValides) {
            return;
        }

        this.flowcharts = this.flowcharts.filter(flowchart => flowchart.id !== id);
        this.detailsFlowchart = this.detailsFlowchart.filter(
            detail => detail.flowchartId !== id
        );
        if (this.flowchartDetailId === id) {
            this.flowchartDetailId = this.flowcharts[0]?.id ?? null;
        }
        if (this.detailFlowchartSelectionne?.id === id) {
            this.fermerDetailFlowchart();
        }
    }

    ajouterDetailFlowchart(): void {
        if (this.detailsFlowchartValides) {
            return;
        }
        if (
            this.flowchartDetailId === null ||
            this.niveauDecalageDetail === null
        ) {
            this.toastr.warning(
                "Sélectionnez un flowchart et renseignez les informations et le niveau.",
                "Information manquante"
            );
            return;
        }

        const flowchart = this.flowcharts.find(
            element => element.id === this.flowchartDetailId
        );
        if (!flowchart) {
            this.toastr.error(
                "Le flowchart sélectionné est introuvable.",
                "Erreur"
            );
            return;
        }

        this.detailsFlowchart = [
            ...this.detailsFlowchart,
            new DetailFlowchart(
                this.prochainDetailId++,
                flowchart.id,
                this.informationsDetailFlowchart.trim(),
                this.niveauDecalageDetail
            )
        ];
        this.informationsDetailFlowchart = "";
        this.niveauDecalageDetail = null;
    }

    retirerDetailFlowchart(idDetail: number): void {
        if (this.detailsFlowchartValides) {
            return;
        }
        this.detailsFlowchart = this.detailsFlowchart.filter(
            element => element.id !== idDetail
        );
    }
    validerReferences(): void {
        if (!this.referencesLegales.trim()) {
            this.toastr.warning(
                "Saisissez les références légales et réglementaires avant de valider.",
                "Aucune référence"
            );
            return;
        }
        this.referencesValidees = true;
    }

    selectionnerFichierAnnexe(event: Event): void {
        const input = event.target as HTMLInputElement;
        const fichier = input.files?.[0] ?? null;

        if (!fichier) {
            this.fichierAnnexe = null;
            return;
        }

        if (!this.estImageValide(fichier)) {
            input.value = '';
            this.fichierAnnexe = null;
            return;
        }

        this.fichierAnnexe = fichier;
    }

    ajouterAnnexe(input: HTMLInputElement): void {
        if (this.annexesValidees) {
            return;
        }

        const titre = this.titreAnnexe.trim();
        if (!titre || !this.fichierAnnexe) {
            this.toastr.warning(
                "Saisissez le titre du document et choisissez une pièce jointe.",
                "Information manquante"
            );
            return;
        }

        this.annexes = [
            ...this.annexes,
            {
                id: this.prochaineAnnexeId++,
                titre,
                fichier: this.fichierAnnexe
            }
        ];
        this.titreAnnexe = "";
        this.fichierAnnexe = null;
        input.value = "";
    }

    retirerAnnexe(id: number): void {
        if (this.annexesValidees) {
            return;
        }
        this.annexes = this.annexes.filter(annexe => annexe.id !== id);
    }

    validerAnnexes(): void {
        this.annexesValidees = true;
    }

    ajouterTermeGlossaire():void{
        const acronyme = this.acronymeClossaire.trim();
        const termes=this.termesGlossaire.trim();

        if(!acronyme || !termes){
            this.toastr.warning(
                "Saisissez l'acronyme et les termes.",
                "Information manquante"
            );
            return;
        }
        if(this.glossaire.some(
            terme => terme.acronyme.toLocaleLowerCase()===acronyme.toLocaleLowerCase()
        )){
            this.toastr.warning(
                "cet  acronyme existe déjà dans le glossaire.",
                "Acronyme déjà ajouté"
            );
            return;
        }
        this.glossaire=[...this.glossaire,{acronyme,termes}];
        this.acronymeClossaire="";
        this.termesGlossaire="";
    }
    retirerDestinataire(agent: Utilisateur): void {
        if (this.destinatairesValides) {
            return;
        }
        this.destinataires = this.destinataires.filter(
            destinataire => destinataire.agent.matricule !== agent.matricule
        );
    }
    retirerAgent(agent: Utilisateur): void {
        if (this.enteteValidee) {
            return;
        }
        this.agentsSelectionnes = this.agentsSelectionnes.filter(
            selectionne => selectionne.matricule !== agent.matricule
        );
    }
    retirerMembreEquipe(agent:Utilisateur): void{
        this.equipeProjet =this.equipeProjet.filter(
            membre=>membre.matricule !==agent.matricule
        );
    }
    retirerTermeGlossaire(terme:{acronyme:string;termes:string}):void{
        this.glossaire=this.glossaire.filter(
            element=>element !==terme
        );
    }
    validerEntete(): void {
        if (!this.titreDocument || !this.codeServiceSelectionne || this.agentsSelectionnes.length === 0) {
            this.toastr.warning(
                "Renseignez le titre, le service et au moins un agent avant de valider.",
                "Information manquante"
            );
            return;
        }
        this.enteteValidee = true;
    }
    validerDestinataires(): void {
        this.destinatairesValides = true;
    }
    validerEquipes():void {
        this.equipeProjetValides=true;
    }
    validerGlossaires():void{
        this.glossaireValides=true;
    }
    validerFlowcharts(): void {
        if (this.flowcharts.length === 0) {
            this.toastr.warning(
                "Ajoutez au moins un flowchart avant de valider.",
                "Aucun flowchart"
            );
            return;
        }
        this.flowchartsValides = true;
    }
    validerDetailsFlowchart(): void {
        /*if (this.detailsFlowchart.length === 0) {
            this.toastr.warning(
                "Ajoutez au moins un détail avant de valider.",
                "Aucun détail"
            );
            return;
        }*/
        this.detailsFlowchartValides = true;
    }
    // 4 pages = 8 sections regroupées 2 par 2
    pages = [
        { id: 1, titre: 'ENTETE + DESTINATAIRES' },
        { id: 2, titre: 'EQUIPE + GLOSSAIRE' },
        { id: 3, titre: 'FLOWCHART + DETAILS ' },
        { id: 4, titre: 'REFERENCES + ANNEXES' }
    ];

    pageActuelle: number = 1;

    private pageEstValidee(page: number): boolean {
        switch (page) {
            case 1:
                return this.enteteValidee && this.destinatairesValides;
            case 2:
                return this.equipeProjetValides && this.glossaireValides;
            case 3:
                return this.flowchartsValides && this.detailsFlowchartValides;
            case 4:
                return this.referencesValidees && this.annexesValidees;
            default:
                return false;
        }
    }

    peutPasserPageActuelle(): boolean {
        return this.pageActuelle < this.pages.length && this.pageEstValidee(this.pageActuelle);
    }

    private premierePageNonValidee(jusqua: number): number | null {
        for (let page = 1; page < jusqua; page++) {
            if (!this.pageEstValidee(page)) {
                return page;
            }
        }
        return null;
    }

    suivant(): void {
        if (this.pageActuelle >= this.pages.length) {
            return;
        }

        if (!this.pageEstValidee(this.pageActuelle)) {
            this.toastr.warning(
                "Validez toutes les sections de cette page avant de continuer.",
                "Sections non validées"
            );
            return;
        }

        this.pageActuelle++;
    }

    precedent(): void {
        if (this.pageActuelle > 1) this.pageActuelle--;
    }

    allerA(id: number): void {
        if (id > this.pageActuelle) {
            const pageNonValidee = this.premierePageNonValidee(id);
            if (pageNonValidee !== null) {
                this.toastr.warning(
                    `Validez toutes les sections de la page ${pageNonValidee} avant de continuer.`,
                    "Sections non validées"
                );
                return;
            }
        }
        this.pageActuelle = id;
    }

    annulerCreation(): void {
        this.pageActuelle = 1;
    }

    async creerSop(): Promise<void> {
        if (this.soumissionEnCours || this.soumissionTerminee) {
            return;
        }
        if (!this.titreDocument.trim() || this.agentsSelectionnes.length === 0) {
            this.toastr.warning(
                "Saisissez le titre et sélectionnez au moins un utilisateur.",
                "Information manquante"
            );
            return;
        }
        if (!this.referencesLegales.trim()) {
            this.toastr.warning(
                "Saisissez les références légales et réglementaires avant de créer le SOP.",
                "Aucune référence"
            );
            return;
        }

        if (!this.codeServiceSelectionne) {
            this.toastr.warning(
                "Sélectionnez le service du SOP dans l'entête.",
                "Service manquant"
            );
            return;
        }

        // Matricule : celui de l'utilisateur connecté (token)
        /*const matricule = this.accountService.getMatricule();
        if (typeof matricule !== "string" || !matricule.trim()) {
            this.toastr.error("Impossible d'identifier l'utilisateur connecté.", "Création du SOP");
            return;
        }*/
        const matricule="000001";
        this.soumissionEnCours = true;
        try {
            const piecesJointes: SopPieceJointeCreation[] = await Promise.all<SopPieceJointeCreation>([
                ...this.flowcharts.map(async (flowchart): Promise<SopPieceJointeCreation> => ({
                    idPieceJointe: `FLOWCHART_${flowchart.id}`,
                    usage: "FLOWCHART",
                    nomFichier: flowchart.fichier.name,
                    typeMime: flowchart.fichier.type || null,
                    taille: flowchart.fichier.size,
                    contenu: await this.fichierEnBase64(flowchart.fichier)
                })),
                ...this.annexes.map(async (annexe): Promise<SopPieceJointeCreation> => ({
                    idPieceJointe: `ANNEXE_${annexe.id}`,
                    usage: "ANNEXE",
                    nomFichier: annexe.fichier.name,
                    typeMime: annexe.fichier.type || null,
                    taille: annexe.fichier.size,
                    contenu: await this.fichierEnBase64(annexe.fichier)
                }))
            ]);

            const payload: SopCreationPayload = {
                titre: this.titreDocument.trim(),
                codeService: this.codeServiceSelectionne,
                auteurMatricule: matricule.trim(),
                contacts: this.agentsSelectionnes.map((agent, index) => ({
                    matricule: agent.matricule,
                    email: agent.mail,
                    ordre: index + 1
                    
                })),
                destinataires: this.destinataires.map(({ agent, action }, index) => ({
                    matricule: agent.matricule,
                    nom: `${agent.nom} ${agent.prenoms}`.trim(),
                    organisation: agent.libelleService || "CNaPS",
                    action,
                    ordre: index + 1
                })),
                equipeProjet: this.equipeProjet.map((agent, index) => ({
                    matricule: agent.matricule,
                    nom: `${agent.nom} ${agent.prenoms}`.trim(),
                    fonction: agent.libelleFonction || "",
                    telephone: agent.telGsm || "",
                    email: agent.mail || "",
                    ordre: index + 1
                })),
                glossaire: this.glossaire.map((terme, index) => ({
                    acronyme: terme.acronyme,
                    terme: terme.termes,
                    ordre: index + 1
                })),
                references: [{ contenu: this.referencesLegales, ordre: 1 }],
                piecesJointes,
                annexes: this.annexes.map((annexe, index) => ({
                    titre: annexe.titre,
                    idPieceJointe: `ANNEXE_${annexe.id}`,
                    ordre: index + 1
                })),
                flowcharts: this.flowcharts.map((flowchart, index) => ({
                    idFlowchart: String(flowchart.id),
                    titre: flowchart.titre,
                    information: flowchart.information,
                    ordre: index + 1,
                    idPieceJointe: `FLOWCHART_${flowchart.id}`
                })),
                flowchartEtapes: this.detailsFlowchart.map((etape, index) => ({
                    idFlowchart: String(etape.flowchartId),
                    information: etape.informations,
                    niveau: etape.niveau,
                    modeNumerotation: "AUTO",
                    ordre: index + 1
                }))
            };

            await this.sopService.create(payload);
            this.soumissionTerminee = true;
            this.toastr.success('Le SOP a été créé avec succès.', 'Création du SOP');
            this.router.navigate(['/system-sop/liste-sop']);
        } catch (error) {
            console.error('Erreur lors de la création du SOP', error);
            this.toastr.error(
                "Le SOP n'a pas pu être enregistré. Vérifiez les informations et réessayez.",
                "Erreur de création"
            );
        } finally {
            this.soumissionEnCours = false;
        }
    }

    private fichierEnBase64(fichier: File): Promise<string> {
        return new Promise((resolve, reject) => {
            const lecteur = new FileReader();
            lecteur.onload = () => {
                if (typeof lecteur.result !== "string") {
                    reject(new Error(`Impossible de lire le fichier ${fichier.name}.`));
                    return;
                }
                const separateur = lecteur.result.indexOf(",");
                if (separateur < 0) {
                    reject(new Error(`Format de fichier illisible : ${fichier.name}.`));
                    return;
                }
                resolve(lecteur.result.slice(separateur + 1));
            };
            lecteur.onerror = () => reject(
                lecteur.error || new Error(`Erreur de lecture du fichier ${fichier.name}.`)
            );
            lecteur.readAsDataURL(fichier);
        });
    }

    ouvrirDetailFlowchart(flowchart: Flowchart): void {
        this.detailFlowchartSelectionne = flowchart;
        this.detailFlowchartOuvert = true;
    }

    detailsPourFlowchart(flowchartId: number): DetailFlowchart[] {
        return this.detailsFlowchart.filter(detail => detail.flowchartId === flowchartId);
    }

    // Chaque titre ouvre une nouvelle séquence de premier niveau.
    numeroFlowchart(flowchartId: number): number {
        const index = this.flowcharts.findIndex(flowchart => flowchart.id === flowchartId);
        if (index < 0) {
            return 0;
        }

        return this.flowcharts
            .slice(0, index + 1)
            .filter(flowchart => flowchart.titre.trim())
            .length;
    }

    detailsNumerotes(flowchartId: number): DetailFlowchartAffiche[] {
        const indexFlowchart = this.flowcharts.findIndex(flowchart => flowchart.id === flowchartId);
        if (indexFlowchart < 0) {
            return [];
        }

        let numeroTitre = 0;
        const compteurs = [0, 0, 0, 0];
        const numerosParDetail = new Map<number, string>();

        this.flowcharts.slice(0, indexFlowchart + 1).forEach(flowchart => {
            if (flowchart.titre.trim()) {
                numeroTitre++;
                compteurs.fill(0);
            } else if (numeroTitre === 0) {
                numeroTitre = 1;
            }

            this.detailsPourFlowchart(flowchart.id).forEach(detail => {
                const profondeur = detail.niveau - 1;
                for (let index = 0; index < profondeur; index++) {
                    if (compteurs[index] === 0) {
                        compteurs[index] = 1;
                    }
                }
                compteurs[profondeur]++;
                for (let index = profondeur + 1; index < compteurs.length; index++) {
                    compteurs[index] = 0;
                }

                numerosParDetail.set(
                    detail.id,
                    [numeroTitre, ...compteurs.slice(0, profondeur + 1)].join(".")
                );
            });
        });

        return this.detailsPourFlowchart(flowchartId).map(detail => ({
            ...detail,
            numero: numerosParDetail.get(detail.id) || ""
        }));
    }

    fermerDetailFlowchart(): void {
        this.detailFlowchartOuvert = false;
        this.detailFlowchartSelectionne = null;
    }

    fermerAvecEchap(event: KeyboardEvent): void {
        if (event.key === 'Escape') {
            if (this.detailFlowchartOuvert) {
                this.fermerDetailFlowchart();
            }
        }
    }

    info(): void    { this.toastr.info('Message /notification INFO', ''); }
    success(): void { this.toastr.success('Message / notification SUCCESS', ''); }
    warning(): void { this.toastr.warning('Message / notification WARNING', ''); }
    error(): void   { this.toastr.error('Message / notification ERROR', ''); }
}