import { Injectable } from "@angular/core";
import { Reference } from "../model/reference.model";

@Injectable({ providedIn: "root" })
export class ReferenceService {
    private readonly api = new Reference();

    getAll(params?: Record<string, string | number | boolean>): Promise<Reference[]> {
        return this.api.getAll<Reference>(params);
    }

    getById(id: string): Promise<Reference> {
        return this.api.getById<Reference>(id);
    }

    create(reference: Partial<Reference>): Promise<Reference> {
        return this.api.create<Reference>(reference);
    }

    update(id: string, reference: Partial<Reference>): Promise<Reference> {
        return this.api.update<Reference>(id, reference);
    }

    delete(id: string): Promise<void> {
        return this.api.delete(id);
    }
}
