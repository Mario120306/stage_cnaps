package com.example.sys_sop.dto;

public class EquipeProjetDto {
    private String idMembre, idSop, matricule, nom, fonction, telephone, email;
    private Integer ordre;

    public EquipeProjetDto() {}
    public EquipeProjetDto(String idMembre, String idSop, String matricule, String nom, String fonction,
            String telephone, String email, Integer ordre) {
        this.idMembre = idMembre; this.idSop = idSop; this.matricule = matricule; this.nom = nom;
        this.fonction = fonction; this.telephone = telephone; this.email = email; this.ordre = ordre;
    }
    public String getIdMembre() { return idMembre; } public void setIdMembre(String v) { idMembre = v; }
    public String getIdSop() { return idSop; } public void setIdSop(String v) { idSop = v; }
    public String getMatricule() { return matricule; } public void setMatricule(String v) { matricule = v; }
    public String getNom() { return nom; } public void setNom(String v) { nom = v; }
    public String getFonction() { return fonction; } public void setFonction(String v) { fonction = v; }
    public String getTelephone() { return telephone; } public void setTelephone(String v) { telephone = v; }
    public String getEmail() { return email; } public void setEmail(String v) { email = v; }
    public Integer getOrdre() { return ordre; } public void setOrdre(Integer v) { ordre = v; }
}
