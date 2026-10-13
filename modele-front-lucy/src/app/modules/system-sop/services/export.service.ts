import { Injectable } from "@angular/core";
import { Export } from "../model/export.model";

@Injectable({ providedIn: "root" })
export class ExportService {
    private readonly api = new Export();

    getAll(params?: Record<string, string | number | boolean>): Promise<Export[]> {
        return this.api.getAll<Export>(params);
    }

    getById(id: string): Promise<Export> {
        return this.api.getById<Export>(id);
    }

    create(exportData: Partial<Export>): Promise<Export> {
        return this.api.create<Export>(exportData);
    }

    update(id: string, exportData: Partial<Export>): Promise<Export> {
        return this.api.update<Export>(id, exportData);
    }

    delete(id: string): Promise<void> {
        return this.api.delete(id);
    }
}
