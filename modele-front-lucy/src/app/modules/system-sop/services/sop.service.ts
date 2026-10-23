import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";
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

export interface SopDetails {
    sop: Sop;
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
        idSop: string;
        nomFichier: string;
        typeMime: string | null;
        contenu: string | null;
        usage: string | null;
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
    getAll(params?: Record<string, string | number | boolean>): Promise<Sop[]> {
        // backend returns { content, page, size, totalElements, totalPages }
        return this.api.getAll<any>(params).then((res: any) => res.content || res);
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

    async getDetails(idSop: string): Promise<SopDetails> {
        const resources = [
            "contacts", "destinataires", "equipes-projet", "glossaires", "references",
            "versions", "flowcharts", "flowchart-etapes", "pieces-jointes", "annexes"
        ] as const;
        const responses = await Promise.all(resources.map(resource =>
            fetch(`${this.apiBaseUrl}/${resource}`, { headers: { Accept: "application/json" } })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`Impossible de charger ${resource} (${response.status}).`);
                    }
                    return response.json();
                })
        ));
        const [contacts, destinataires, equipeProjet, glossaire, references, versions,
            flowcharts, flowchartEtapes, piecesJointes, annexes] = responses;
        const belongsToSop = (item: { idSop?: string }) => item.idSop === idSop;

        return {
            sop: await this.getById(idSop),
            contacts: contacts.filter(belongsToSop),
            destinataires: destinataires.filter(belongsToSop),
            equipeProjet: equipeProjet.filter(belongsToSop),
            glossaire: glossaire.filter(belongsToSop),
            references: references.filter(belongsToSop),
            versions: versions.filter(belongsToSop),
            flowcharts: flowcharts.filter(belongsToSop),
            flowchartEtapes,
            piecesJointes: piecesJointes.filter(belongsToSop),
            annexes: annexes.filter(belongsToSop)
        };
    }

    private get apiBaseUrl(): string {
        return environment.apiUrl;
    }
}
