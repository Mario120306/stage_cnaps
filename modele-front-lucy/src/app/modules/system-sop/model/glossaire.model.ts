import { environment } from "src/environments/environment";
import { Mere } from "./mere.model";

export class Glossaire extends Mere {
    public idGlossaire: string;
    public idSop: string;
    public acronyme: string | null;
    public terme: string | null;
    public ordre: number | null;

    constructor(
        idGlossaire = "",
        idSop = "",
        acronyme: string | null = null,
        terme: string | null = null,
        ordre: number | null = null
    ) {
        super("glossaires", "glossaire", environment.apiUrl);
        this.idGlossaire = idGlossaire;
        this.idSop = idSop;
        this.acronyme = acronyme;
        this.terme = terme;
        this.ordre = ordre;
    }
}
