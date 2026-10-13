import { environment } from "src/environments/environment";
import { Mere } from "./mere.model";

export interface AnnexeDraft {
    id: number;
    titre: string;
    fichier: File;
}

export class Annexe extends Mere {
    public idAnnexe: string;
    public idSop: string;
    public titre: string | null;
    public idPieceJointe: string | null;
    public ordre: number | null;

    constructor(
        idAnnexe = "",
        idSop = "",
        titre: string | null = null,
        idPieceJointe: string | null = null,
        ordre: number | null = null
    ) {
        super("annexes", "annexe", environment.apiUrl);
        this.idAnnexe = idAnnexe;
        this.idSop = idSop;
        this.titre = titre;
        this.idPieceJointe = idPieceJointe;
        this.ordre = ordre;
    }
}
