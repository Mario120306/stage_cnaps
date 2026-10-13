package com.example.sys_sop.modele;

import java.time.LocalDate;

public class Utilisateur {
    
    private String      matricule;
    private String      nom;
    private String      prenoms;
    private LocalDate   dateNaissaince;
    private String      lieu;
    private String      codeDirection;
    private String      libelleDirection;
    private String      codeService;
    private String      libelleService;
    private String      codeFonction;
    private String      libelleFonction;
    private String      mail;
    private String      telGsm;
    
    public Utilisateur() {
    }
    public Utilisateur(String matricule, String nom, String prenoms, LocalDate dateNaissaince, String lieu,
            String codeDirection, String libelleDirection, String codeService, String libelleService,
            String codeFonction, String libelleFonction, String mail, String telGsm) {
        this.matricule = matricule;
        this.nom = nom;
        this.prenoms = prenoms;
        this.dateNaissaince = dateNaissaince;
        this.lieu = lieu;
        this.codeDirection = codeDirection;
        this.libelleDirection = libelleDirection;
        this.codeService = codeService;
        this.libelleService = libelleService;
        this.codeFonction = codeFonction;
        this.libelleFonction = libelleFonction;
        this.mail = mail;
        this.telGsm = telGsm;
    }
    public String getMatricule() {
        return matricule;
    }
    public String getNom() {
        return nom;
    }
    public String getPrenoms() {
        return prenoms;
    }
    public LocalDate getDateNaissaince() {
        return dateNaissaince;
    }
    public String getLieu() {
        return lieu;
    }
    public String getCodeDirection() {
        return codeDirection;
    }
    public String getLibelleDirection() {
        return libelleDirection;
    }
    public String getCodeService() {
        return codeService;
    }
    public String getLibelleService() {
        return libelleService;
    }
    public String getCodeFonction() {
        return codeFonction;
    }
    public String getLibelleFonction() {
        return libelleFonction;
    }
    public String getMail() {
        return mail;
    }
    public String getTelGsm() {
        return telGsm;
    }
}
