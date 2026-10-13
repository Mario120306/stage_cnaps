import { environment } from "src/environments/environment";
import {Mere} from './mere.model';

export class Service extends Mere{
    public codeService:string;
    public libelleService:string;
    public codeDirection:string;
    
    constructor(
        codeService:string,
        libelleService:string,
        codeDirection:string
    ){
        super(
            "services",
            "service",
            environment.apiUrl
        );
        this.codeService=codeService;
        this.libelleService=libelleService;
        this.codeDirection=codeDirection;
    }
}