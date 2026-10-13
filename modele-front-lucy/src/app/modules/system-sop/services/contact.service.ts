import { Injectable } from "@angular/core";
import { Contact } from "../model/contact.model";

@Injectable({ providedIn: "root" })
export class ContactService {
    private readonly api = new Contact();

    getAll(params?: Record<string, string | number | boolean>): Promise<Contact[]> {
        return this.api.getAll<Contact>(params);
    }

    getById(id: string): Promise<Contact> {
        return this.api.getById<Contact>(id);
    }

    create(contact: Partial<Contact>): Promise<Contact> {
        return this.api.create<Contact>(contact);
    }

    update(id: string, contact: Partial<Contact>): Promise<Contact> {
        return this.api.update<Contact>(id, contact);
    }

    delete(id: string): Promise<void> {
        return this.api.delete(id);
    }
}
