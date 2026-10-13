import { environment } from "src/environments/environment";
import { Mere } from "./mere.model";

export class Destinataire extends Mere {
    public idDestinataire: string;
    public idSop: string;
    public matricule: string | null;
    public nom: string | null;
    public organisation: string | null;
    public action: string | null;
    public dateAction: string | null;
    public ordre: number | null;

    constructor(
        idDestinataire = "",
        idSop = "",
        matricule: string | null = null,
        nom: string | null = null,
        organisation: string | null = null,
        action: string | null = null,
        dateAction: string | null = null,
        ordre: number | null = null
    ) {
        super("destinataires", "destinataire", environment.apiUrl);
        this.idDestinataire = idDestinataire;
        this.idSop = idSop;
        this.matricule = matricule;
        this.nom = nom;
        this.organisation = organisation;
        this.action = action;
        this.dateAction = dateAction;
        this.ordre = ordre;
    }
}
