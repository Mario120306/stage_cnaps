package com.example.sys_sop.modele;

public class Annexe {
    private String idAnnexe, idSop, titre, idPieceJointe;
    private Integer ordre;

    public Annexe() {}
    public Annexe(String idAnnexe, String idSop, String titre, String idPieceJointe, Integer ordre) {
        this.idAnnexe = idAnnexe; this.idSop = idSop; this.titre = titre; this.idPieceJointe = idPieceJointe; this.ordre = ordre;
    }
    public String getIdAnnexe() { return idAnnexe; } 
    public void setIdAnnexe(String v) { idAnnexe = v; }
    public String getIdSop() { return idSop; } 
    public void setIdSop(String v) { idSop = v; }
    public String getTitre() { return titre; } 
    public void setTitre(String v) { titre = v; }
    public String getIdPieceJointe() { return idPieceJointe; } 
    public void setIdPieceJointe(String v) { idPieceJointe = v; }
    public Integer getOrdre() { return ordre; } 
    public void setOrdre(Integer v) { ordre = v; }
}
