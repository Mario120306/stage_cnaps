import { Injectable } from "@angular/core";
import { EquipeProjet } from "../model/equipe-projet.model";

@Injectable({ providedIn: "root" })
export class EquipeProjetService {
    private readonly api = new EquipeProjet();

    getAll(params?: Record<string, string | number | boolean>): Promise<EquipeProjet[]> {
        return this.api.getAll<EquipeProjet>(params);
    }

    getById(id: string): Promise<EquipeProjet> {
        return this.api.getById<EquipeProjet>(id);
    }

    create(membre: Partial<EquipeProjet>): Promise<EquipeProjet> {
        return this.api.create<EquipeProjet>(membre);
    }

    update(id: string, membre: Partial<EquipeProjet>): Promise<EquipeProjet> {
        return this.api.update<EquipeProjet>(id, membre);
    }

    delete(id: string): Promise<void> {
        return this.api.delete(id);
    }
}
