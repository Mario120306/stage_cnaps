package com.example.sys_sop.modele;

import java.time.LocalDateTime;

public class Destinataire {
    private String idDestinataire, idSop, matricule, nom, organisation, fonction, action;
    private LocalDateTime dateAction;
    private Integer ordre;

    public Destinataire() {}
    public Destinataire(String idDestinataire, String idSop, String matricule, String nom, String organisation,
            String fonction, String action, LocalDateTime dateAction, Integer ordre) {
        this.idDestinataire = idDestinataire; this.idSop = idSop; this.matricule = matricule; this.nom = nom;
        this.organisation = organisation; this.fonction = fonction; this.action = action;
        this.dateAction = dateAction; this.ordre = ordre;
    }
    public String getIdDestinataire() { return idDestinataire; } 
    public void setIdDestinataire(String v) { idDestinataire = v; }
    public String getIdSop() { return idSop; } 
    public void setIdSop(String v) { idSop = v; }
    public String getMatricule() { return matricule; } 
    public void setMatricule(String v) { matricule = v; }
    public String getNom() { return nom; } 
    public void setNom(String v) { nom = v; }
    public String getOrganisation() { return organisation; } 
    public void setOrganisation(String v) { organisation = v; }
    public String getFonction() { return fonction; }
    public void setFonction(String v) { fonction = v; }
    public String getAction() { return action; } 
    public void setAction(String v) { action = v; }
    public LocalDateTime getDateAction() { return dateAction; }
    public void setDateAction(LocalDateTime v) { dateAction = v; }
    public Integer getOrdre() { return ordre; } 
    public void setOrdre(Integer v) { ordre = v; }
}
