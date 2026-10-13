export class Flowchart {
    public id: number;
    public titre: string;
    public information: string;
    public fichier: File;

    constructor(id: number, titre: string, information: string, fichier: File) {
        this.id = id;
        this.titre = titre;
        this.information = information;
        this.fichier = fichier;
    }
}
