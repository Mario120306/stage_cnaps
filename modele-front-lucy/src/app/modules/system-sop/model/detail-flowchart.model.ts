export class DetailFlowchart {
    public id: number;
    public flowchartId: number;
    public informations: string;
    public niveau: number;

    constructor(id: number, flowchartId: number, informations: string, niveau: number) {
        this.id = id;
        this.flowchartId = flowchartId;
        this.informations = informations;
        this.niveau = niveau;
    }
}

export interface DetailFlowchartAffiche extends DetailFlowchart {
    numero: string;
}
