import { Injectable } from "@angular/core";
import { Sop } from "../model/sop.model";

export interface SopFlowchart {
    idFlowchart: string;
    idSop: string;
    titre: string | null;
    information: string | null;
    ordre: number | null;
    idPieceJointe: string | null;
}

export interface SopFlowchartEtape {
    idEtape: string;
    idFlowchart: string;
    information: string | null;
    niveau: number | null;
    modeNumerotation: string | null;
    ordre: number | null;
}

export interface SopPage {
    content: Sop[];
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
}

export interface SopDetails {
    sop: Sop;
    createur: boolean;
    contacts: Array<{ idContact?: string; matricule: string | null; email: string | null; ordre: number | null }>;
    destinataires: Array<{ idDestinataire?: string; nom: string | null; organisation: string | null; action: string | null; matricule: string | null; ordre: number | null }>;
    equipeProjet: Array<{ idMembre?: string; nom: string | null; fonction: string | null; telephone: string | null; email: string | null; matricule?: string | null; ordre: number | null }>;
    glossaire: Array<{ idGlossaire?: string; acronyme: string | null; terme: string | null; ordre: number | null }>;
    references: Array<{ idReference?: string; contenu: string | null; ordre: number | null }>;
    versions: Array<{ numeroVersion: string; dateEntree: string | null; libelleModification: string | null }>;
    flowcharts: SopFlowchart[];
    flowchartEtapes: SopFlowchartEtape[];
    piecesJointes: Array<{
        idPieceJointe: string;
        nomFichier: string;
        typeMime: string | null;
        usage: string | null;
        contenu?: string | null;
    }>;
    annexes: Array<{ titre: string | null; idPieceJointe: string | null; ordre: number | null }>;
}

export type SopPieceJointeCreation = {
    idPieceJointe: string;
    usage: "FLOWCHART" | "ANNEXE";
    nomFichier: string;
    typeMime: string | null;
    taille: number;
    contenu: string;
};

export type SopCreationPayload = Partial<Sop> & {
    contacts: Array<{ matricule: string; email: string; ordre: number }>;
    destinataires: Array<{
        matricule: string;
        nom: string;
        organisation: string;
        action: string;
        ordre: number;
    }>;
    equipeProjet: Array<{
        matricule: string;
        nom: string;
        fonction: string;
        telephone: string;
        email: string;
        ordre: number;
    }>;
    glossaire: Array<{ acronyme: string; terme: string; ordre: number }>;
    references: Array<{ contenu: string; ordre: number }>;
    piecesJointes: SopPieceJointeCreation[];
    annexes: Array<{ titre: string; idPieceJointe: string; ordre: number }>;
    flowcharts: Array<{
        idFlowchart: string;
        titre: string;
        information: string;
        ordre: number;
        idPieceJointe: string;
    }>;
    flowchartEtapes: Array<{
        idFlowchart: string;
        information: string;
        niveau: number;
        modeNumerotation: "AUTO" | "MANUEL";
        ordre: number;
    }>;
};

export type SopUpdatePayload = Partial<Sop> & {
    contacts: Array<{ idContact?: string; matricule: string; email: string; ordre: number }>;
    destinataires: Array<{ idDestinataire?: string; matricule: string; nom: string; organisation: string; action: string; ordre: number }>;
    equipeProjet: Array<{ idMembre?: string; matricule: string; nom: string; fonction: string; telephone: string; email: string; ordre: number }>;
    glossaire: Array<{ idGlossaire?: string; acronyme: string; terme: string; ordre: number }>;
    references: Array<{ idReference?: string; contenu: string; ordre: number }>;
    flowcharts: Array<{ idFlowchart: string; information: string }>;
    flowchartEtapes: Array<{ idEtape: string; idFlowchart: string; information: string; niveau: number; modeNumerotation: "AUTO" | "MANUEL"; ordre: number }>;
};

@Injectable({ providedIn: "root" })
export class SopService {
    private readonly api = new Sop("","","","","","",0,"","","","","","","");

    /**
     * Request page of SOPs from backend. params may include page, size, titre, direction, service, auteur, dateFrom, dateTo
     */
    getAll(params?: Record<string, string | number | boolean>): Promise<SopPage> {
        return this.api.getPage<SopPage>(params);
    }

    getById(id: string): Promise<Sop> {
        return this.api.getById<Sop>(id);
    }

    create(sop: SopCreationPayload): Promise<Sop> {
        return this.api.create<Sop>(sop);
    }

    update(id: string, sop: SopUpdatePayload): Promise<Sop> {
        return this.api.update<Sop>(id, sop);
    }

    softDelete(id: string): Promise<void> {
        return this.api.delete(id);
    }

    async getDetails(idSop: string, matricule?: string | null): Promise<SopDetails> {
        const suffix = matricule ? `?matricule=${encodeURIComponent(matricule)}` : "";
        return this.api.getDetails<SopDetails>(idSop, suffix);
    }
}
