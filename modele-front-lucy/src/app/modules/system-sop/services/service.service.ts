import { Injectable } from "@angular/core";
import { Service } from "../model/service.model";

@Injectable({providedIn:'root'})
export class ServiceService{
    private readonly api =new Service('','','');
    
    getAll():Promise<Service[]>{
        return this.api.getAll<Service>();
    }
    getById(id:string):Promise<Service>{
        return this.api.getById<Service>(id);
    }
}