import { environment } from "src/environments/environment";
import { Mere } from "./mere.model";

export class Version extends Mere {
    public idVersion: string;
    public idSop: string;
    public numeroVersion: string;
    public dateEntree: string | null;
    public libelleModification: string | null;
    public auteurModification: string | null;
    public codeStatut: string | null;

    constructor(
        idVersion = "",
        idSop = "",
        numeroVersion = "",
        dateEntree: string | null = null,
        libelleModification: string | null = null,
        auteurModification: string | null = null,
        codeStatut: string | null = null
    ) {
        super("versions", "version", environment.apiUrl);
        this.idVersion = idVersion;
        this.idSop = idSop;
        this.numeroVersion = numeroVersion;
        this.dateEntree = dateEntree;
        this.libelleModification = libelleModification;
        this.auteurModification = auteurModification;
        this.codeStatut = codeStatut;
    }
}
