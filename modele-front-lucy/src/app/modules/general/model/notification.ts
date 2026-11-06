export interface Notification {
  idNotification: string;
  idSop: string;
  matricule: string;
  typeDestinataire: string;
  titre: string;
  message: string;
  dateCreation: string;
  vue: string;
  dateVue?: string;
}
