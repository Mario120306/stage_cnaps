import { Injectable } from "@angular/core";
import { Glossaire } from "../model/glossaire.model";

@Injectable({ providedIn: "root" })
export class GlossaireService {
    private readonly api = new Glossaire();

    getAll(params?: Record<string, string | number | boolean>): Promise<Glossaire[]> {
        return this.api.getAll<Glossaire>(params);
    }

    getById(id: string): Promise<Glossaire> {
        return this.api.getById<Glossaire>(id);
    }

    create(terme: Partial<Glossaire>): Promise<Glossaire> {
        return this.api.create<Glossaire>(terme);
    }

    update(id: string, terme: Partial<Glossaire>): Promise<Glossaire> {
        return this.api.update<Glossaire>(id, terme);
    }

    delete(id: string): Promise<void> {
        return this.api.delete(id);
    }
}
