package com.example.sys_sop.dto;

import java.time.LocalDateTime;

public class SopDto {
    private String idSop, numeroSop, titre, versionCourante, codeService, auteurMatricule, codeStatut, supprime,libelleService,libelleDirection;
    private Integer sectionValidee;
    private LocalDateTime dateSuppression, dateCreation, dateModification;
    public SopDto() {
    }
    public SopDto(String idSop, String numeroSop, String titre, String versionCourante, String codeService,
            String auteurMatricule,Integer sectionValidee,String codeStatut, String supprime,
            LocalDateTime dateSuppression, LocalDateTime dateCreation, LocalDateTime dateModification,
            String libelleService, String libelleDirection) {
        this.idSop = idSop;
        this.numeroSop = numeroSop;
        this.titre = titre;
        this.versionCourante = versionCourante;
        this.codeService = codeService;
        this.auteurMatricule = auteurMatricule;
        this.codeStatut = codeStatut;
        this.supprime = supprime;
        this.sectionValidee = sectionValidee;
        this.dateSuppression = dateSuppression;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.libelleService=libelleService;
        this.libelleDirection=libelleDirection;
    }
    public String getIdSop() {return idSop;}
    public void setIdSop(String idSop) {this.idSop = idSop;}
    public String getNumeroSop() {return numeroSop;}
    public void setNumeroSop(String numeroSop) {this.numeroSop = numeroSop;}
    public String getTitre() {return titre;}
    public void setTitre(String titre) {this.titre = titre;}
    public String getVersionCourante() {return versionCourante;}
    public void setVersionCourante(String versionCourante) {this.versionCourante = versionCourante;}
    public String getCodeService() {return codeService;}
    public void setCodeService(String codeService) {this.codeService = codeService;}
    public String getAuteurMatricule() {return auteurMatricule;}
    public void setAuteurMatricule(String auteurMatricule) {this.auteurMatricule = auteurMatricule;}
    public String getCodeStatut() {return codeStatut;}
    public void setCodeStatut(String codeStatut) {this.codeStatut = codeStatut;}
    public String getSupprime() {return supprime;}
    public void setSupprime(String supprime) {this.supprime = supprime;}
    public Integer getSectionValidee() {return sectionValidee;}
    public void setSectionValidee(Integer sectionValidee) {this.sectionValidee = sectionValidee;}
    public LocalDateTime getDateSuppression() {return dateSuppression;}
    public void setDateSuppression(LocalDateTime dateSuppression) {this.dateSuppression = dateSuppression;}
    public LocalDateTime getDateCreation() {return dateCreation;}
    public void setDateCreation(LocalDateTime dateCreation) {this.dateCreation = dateCreation;}
    public LocalDateTime getDateModification() {return dateModification;}
    public void setDateModification(LocalDateTime dateModification) {this.dateModification = dateModification;}
       public String getLibelleService() {return libelleService;}
    public void setLibelleService(String libelleService) {this.libelleService = libelleService;}
    public String getLibelleDirection() {return libelleDirection;}
    public void setLibelleDirection(String libelleDirection) {this.libelleDirection = libelleDirection;}
}
