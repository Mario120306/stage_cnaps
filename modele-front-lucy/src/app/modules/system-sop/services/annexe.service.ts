import { Injectable } from "@angular/core";
import { Annexe } from "../model/annexe.model";

@Injectable({ providedIn: "root" })
export class AnnexeService {
    private readonly api = new Annexe();

    getAll(params?: Record<string, string | number | boolean>): Promise<Annexe[]> {
        return this.api.getAll<Annexe>(params);
    }

    getById(id: string): Promise<Annexe> {
        return this.api.getById<Annexe>(id);
    }

    create(annexe: Partial<Annexe>): Promise<Annexe> {
        return this.api.create<Annexe>(annexe);
    }

    update(id: string, annexe: Partial<Annexe>): Promise<Annexe> {
        return this.api.update<Annexe>(id, annexe);
    }

    delete(id: string): Promise<void> {
        return this.api.delete(id);
    }
}
