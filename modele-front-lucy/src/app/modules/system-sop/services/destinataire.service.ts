import { Injectable } from "@angular/core";
import { Destinataire } from "../model/destinataire.model";

@Injectable({ providedIn: "root" })
export class DestinataireService {
    private readonly api = new Destinataire();

    getAll(params?: Record<string, string | number | boolean>): Promise<Destinataire[]> {
        return this.api.getAll<Destinataire>(params);
    }

    getById(id: string): Promise<Destinataire> {
        return this.api.getById<Destinataire>(id);
    }

    create(destinataire: Partial<Destinataire>): Promise<Destinataire> {
        return this.api.create<Destinataire>(destinataire);
    }

    update(id: string, destinataire: Partial<Destinataire>): Promise<Destinataire> {
        return this.api.update<Destinataire>(id, destinataire);
    }

    delete(id: string): Promise<void> {
        return this.api.delete(id);
    }
}
