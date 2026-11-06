package com.example.sys_sop.modele;

import java.time.LocalDateTime;

public class Notification {
    private String idNotification;
    private String idSop;
    private String matricule;
    private String typeDestinataire;
    private String titre;
    private String message;
    private LocalDateTime dateCreation;
    private String vue;
    private LocalDateTime dateVue;

    public Notification() {}

    public Notification(String idNotification, String idSop, String matricule, String typeDestinataire,
            String titre, String message, LocalDateTime dateCreation, String vue, LocalDateTime dateVue) {
        this.idNotification = idNotification;
        this.idSop = idSop;
        this.matricule = matricule;
        this.typeDestinataire = typeDestinataire;
        this.titre = titre;
        this.message = message;
        this.dateCreation = dateCreation;
        this.vue = vue;
        this.dateVue = dateVue;
    }

    public String getIdNotification() { return idNotification; }
    public String getIdSop() { return idSop; }
    public String getMatricule() { return matricule; }
    public String getTypeDestinataire() { return typeDestinataire; }
    public String getTitre() { return titre; }
    public String getMessage() { return message; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public String getVue() { return vue; }
    public LocalDateTime getDateVue() { return dateVue; }
}
