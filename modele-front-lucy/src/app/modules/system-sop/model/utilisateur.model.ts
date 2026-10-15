import { environment } from "src/environments/environment";
import { Mere } from "./mere.model";

export class Utilisateur extends Mere{
    public matricule: string;
    public nom: string;
    public prenoms: string;
    public dateNaissance:string;
    public lieu:string;
    public codeDirection:string;
    public codeService:string;
    public libelleDirection:string;
    public libelleService:string;
    public codeFonction:string;
    public libelleFonction:string;
    public mail: string;
    public telGsm:string;

    constructor(
        matricule: string,
        nom: string,
        prenoms: string,
        dateNaissance:string,
        lieu:string,
        codeDirection:string,
        codeService:string,
        libelleDirection:string,
        libelleService:string,
        codeFonction:string,
        libelleFonction:string,
        mail: string,
        telGsm:string,
    ){
        super("utilisateurs","utilisateur",environment.apiUrl);
        this.matricule=matricule;
        this.nom=nom;
        this.prenoms=prenoms;
        this.dateNaissance=dateNaissance;
        this.lieu=lieu;
        this.codeDirection=codeDirection;
        this.codeService=codeService;
        this.libelleDirection=libelleDirection;
        this.libelleService=libelleService;
        this.codeFonction=codeFonction;
        this.libelleFonction=libelleFonction;
        this.mail=mail;
        this.telGsm=telGsm;
    }
}
