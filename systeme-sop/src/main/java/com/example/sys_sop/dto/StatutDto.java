package com.example.sys_sop.dto;

public class StatutDto {

    private String codeStatut;
    private String libelle;
    private int ordre;

    public StatutDto() {}

    public StatutDto(String codeStatut, String libelle, int ordre) {
        this.codeStatut = codeStatut;
        this.libelle = libelle;
        this.ordre = ordre;
    }

    public String getCodeStatut() { return codeStatut; }
    public void setCodeStatut(String codeStatut) { this.codeStatut = codeStatut; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public int getOrdre() { return ordre; }
    public void setOrdre(int ordre) { this.ordre = ordre; }
}