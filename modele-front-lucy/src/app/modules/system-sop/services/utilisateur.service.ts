import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";
import { Utilisateur  } from "../model/utilisateur.model";

@Injectable({ providedIn: "root" })
export class UtilisateurService {
    private readonly api=new Utilisateur("","","","","","","","","","","","","");
     getAll():Promise<Utilisateur[]>{
        return this.api.getAll<Utilisateur>();
     }

     searchByMail(mail: string): Promise<Utilisateur[]> {
        return this.api.getAll<Utilisateur>({ mail });
     }

     searchByMatricule(matricule: string): Promise<Utilisateur[]> {
        return this.api.getAll<Utilisateur>({ matricule });
     }
}
