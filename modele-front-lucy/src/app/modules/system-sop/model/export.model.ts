import { environment } from "src/environments/environment";
import { Mere } from "./mere.model";

export class Export extends Mere {
    public idExport: string;
    public idSop: string;
    public idVersion: string | null;
    public libelleExport: string | null;
    public nbPages: number | null;
    public dateExport: string | null;
    public nomFichierPdf: string | null;
    public generePar: string | null;

    constructor(
        idExport = "",
        idSop = "",
        idVersion: string | null = null,
        libelleExport: string | null = null,
        nbPages: number | null = null,
        dateExport: string | null = null,
        nomFichierPdf: string | null = null,
        generePar: string | null = null
    ) {
        super("exports", "export", environment.apiUrl);
        this.idExport = idExport;
        this.idSop = idSop;
        this.idVersion = idVersion;
        this.libelleExport = libelleExport;
        this.nbPages = nbPages;
        this.dateExport = dateExport;
        this.nomFichierPdf = nomFichierPdf;
        this.generePar = generePar;
    }
}
