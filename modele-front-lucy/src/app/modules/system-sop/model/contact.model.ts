import { environment } from "src/environments/environment";
import { Mere } from "./mere.model";

export class Contact extends Mere {
    public idContact: string;
    public idSop: string;
    public matricule: string | null;
    public email: string | null;
    public ordre: number | null;

    constructor(
        idContact = "",
        idSop = "",
        matricule: string | null = null,
        email: string | null = null,
        ordre: number | null = null
    ) {
        super("contacts", "contact", environment.apiUrl);
        this.idContact = idContact;
        this.idSop = idSop;
        this.matricule = matricule;
        this.email = email;
        this.ordre = ordre;
    }
}
