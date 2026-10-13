import { environment } from "src/environments/environment";
import { Mere } from "./mere.model";

export type UsagePieceJointe = "FLOWCHART" | "ANNEXE";

export class PieceJointe extends Mere {
    public idPieceJointe: string;
    public idSop: string;
    public usage: UsagePieceJointe | null;
    public nomFichier: string;
    public typeMime: string | null;
    public taille: number | null;
    public contenu: string | null;
    public auteurMatricule: string | null;
    public dateInsert: string | null;

    constructor(
        idPieceJointe = "",
        idSop = "",
        usage: UsagePieceJointe | null = null,
        nomFichier = "",
        typeMime: string | null = null,
        taille: number | null = null,
        contenu: string | null = null,
        auteurMatricule: string | null = null,
        dateInsert: string | null = null
    ) {
        super("pieces-jointes", "piece-jointe", environment.apiUrl);
        this.idPieceJointe = idPieceJointe;
        this.idSop = idSop;
        this.usage = usage;
        this.nomFichier = nomFichier;
        this.typeMime = typeMime;
        this.taille = taille;
        this.contenu = contenu;
        this.auteurMatricule = auteurMatricule;
        this.dateInsert = dateInsert;
    }
}
