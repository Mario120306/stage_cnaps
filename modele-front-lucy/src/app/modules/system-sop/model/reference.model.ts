import { environment } from "src/environments/environment";
import { Mere } from "./mere.model";

export class Reference extends Mere {
    public idReference: string;
    public idSop: string;
    public contenu: string | null;
    public ordre: number | null;

    constructor(
        idReference = "",
        idSop = "",
        contenu: string | null = null,
        ordre: number | null = null
    ) {
        super("references", "reference", environment.apiUrl);
        this.idReference = idReference;
        this.idSop = idSop;
        this.contenu = contenu;
        this.ordre = ordre;
    }
}
