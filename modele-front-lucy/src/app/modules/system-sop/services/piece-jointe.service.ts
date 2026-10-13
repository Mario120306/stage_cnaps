import { Injectable } from "@angular/core";
import { PieceJointe } from "../model/piece-jointe.model";

@Injectable({ providedIn: "root" })
export class PieceJointeService {
    private readonly api = new PieceJointe();

    getAll(params?: Record<string, string | number | boolean>): Promise<PieceJointe[]> {
        return this.api.getAll<PieceJointe>(params);
    }

    getById(id: string): Promise<PieceJointe> {
        return this.api.getById<PieceJointe>(id);
    }

    create(pieceJointe: Partial<PieceJointe>): Promise<PieceJointe> {
        return this.api.create<PieceJointe>(pieceJointe);
    }

    update(id: string, pieceJointe: Partial<PieceJointe>): Promise<PieceJointe> {
        return this.api.update<PieceJointe>(id, pieceJointe);
    }

    delete(id: string): Promise<void> {
        return this.api.delete(id);
    }
}
