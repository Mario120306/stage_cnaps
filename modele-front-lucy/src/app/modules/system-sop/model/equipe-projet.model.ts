import { environment } from "src/environments/environment";
import { Mere } from "./mere.model";

export class EquipeProjet extends Mere {
    public idMembre: string;
    public idSop: string;
    public matricule: string | null;
    public nom: string | null;
    public fonction: string | null;
    public telephone: string | null;
    public email: string | null;
    public ordre: number | null;

    constructor(
        idMembre = "",
        idSop = "",
        matricule: string | null = null,
        nom: string | null = null,
        fonction: string | null = null,
        telephone: string | null = null,
        email: string | null = null,
        ordre: number | null = null
    ) {
        super("equipes-projet", "equipe-projet", environment.apiUrl);
        this.idMembre = idMembre;
        this.idSop = idSop;
        this.matricule = matricule;
        this.nom = nom;
        this.fonction = fonction;
        this.telephone = telephone;
        this.email = email;
        this.ordre = ordre;
    }
}
