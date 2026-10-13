import { environment } from "src/environments/environment";
import {Mere} from './mere.model';

export class Direction extends Mere{
    public codeDirection:string;
    public libelleDirection:string;
    public codeAbrev:string;
    
    constructor(
        codeDirection:string,
        libelleDirection:string,
        codeAbrev:string
    ){
        super(
            "directions",
            "direction",
            environment.apiUrl
        );
        this.codeDirection=codeDirection;
        this.libelleDirection=libelleDirection;
        this.codeAbrev=codeAbrev;
    }
}