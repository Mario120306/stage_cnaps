import { environment } from "src/environments/environment";
import { Mere } from "./mere.model";

export class Sop extends Mere {
    public idSop:string;
    public numeroSop:string;
    public titre:string;
    public versionCourante:string;
    public codeService:string;
    public auteurMatricule:string;
    public sectionValidee:number;
    public codeStatut:string;
    public supprime:string;
    public dateSuppression:string;
    public dateCreation:string;
    public dateModification:string;
    public libelleService:string;
    public libelleDirection:string;

    constructor(
        idSop:string,
        numeroSop:string,
        titre:string,
        versionCourante:string,
        codeService:string,
        auteurMatricule:string,
        sectionValidee:number,
        codeStatut:string,
        supprime:string,
        dateSuppression:string,
        dateCreation:string,
        dateModification:string,
        libelleService:string,
        libelleDirection:string
    ) {
        super("sops", "sop", environment.apiUrl);
        this.idSop=idSop;
        this.numeroSop=numeroSop;
        this.titre=titre;
        this.versionCourante=versionCourante;
        this.codeService=codeService;
        this.auteurMatricule=auteurMatricule;
        this.sectionValidee=sectionValidee;
        this.codeStatut=codeStatut;
        this.supprime=supprime;
        this.dateSuppression=dateSuppression;
        this.dateCreation=dateCreation;
        this.dateModification=dateModification;
        this.libelleService=libelleService;
        this.libelleDirection=libelleDirection;
    }
}