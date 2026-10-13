import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";
import { Version } from "../model/version.model";

@Injectable({ providedIn: "root" })
export class VersionService {
    private readonly api = new Version();

    constructor(private http: HttpClient) {}

    getAll(params?: Record<string, string | number | boolean>): Promise<Version[]> {
        return this.api.getAll<Version>(params);
    }

    getById(id: string): Promise<Version> {
        return this.api.getById<Version>(id);
    }

    create(version: Partial<Version>): Promise<Version> {
        return this.api.create<Version>(version);
    }

    async countVersionByIdSop(idSop: string): Promise<number> {
        const count = await this.http
            .get<number>(`${environment.apiUrl}/versions/count/${encodeURIComponent(idSop)}`)
            .toPromise();

        if (count === undefined || count === null) {
            throw new Error("L'API n'a pas retourné le nombre de versions du SOP.");
        }
        return count;
    }
}
