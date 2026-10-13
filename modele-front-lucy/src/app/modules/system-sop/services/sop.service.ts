import { Injectable } from "@angular/core";
import { Sop } from "../model/sop.model";

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

    update(id: string, sop: Partial<Sop>): Promise<Sop> {
        return this.api.update<Sop>(id, sop);
    }
}
