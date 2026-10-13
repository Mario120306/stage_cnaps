import { Injectable } from "@angular/core";
import { Direction } from "../model/direction.model";

@Injectable({providedIn:'root'})
export class DirectionService{
    private readonly api =new Direction('','','');
    
    getAll():Promise<Direction[]>{
        return this.api.getAll<Direction>();
    }
    getById(id:string):Promise<Direction>{
        return this.api.getById<Direction>(id);
    }
}