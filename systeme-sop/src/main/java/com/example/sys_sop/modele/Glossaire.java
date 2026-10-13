package com.example.sys_sop.modele;

public class Glossaire {
    private String idGlossaire, idSop, acronyme, terme;
    private Integer ordre;

    public Glossaire() {}
    public Glossaire(String idGlossaire, String idSop, String acronyme, String terme, Integer ordre) {
        this.idGlossaire = idGlossaire; this.idSop = idSop; this.acronyme = acronyme; this.terme = terme; this.ordre = ordre;
    }
    public String getIdGlossaire() { return idGlossaire; } 
    public void setIdGlossaire(String v) { idGlossaire = v; }
    public String getIdSop() { return idSop; } 
    public void setIdSop(String v) { idSop = v; }
    public String getAcronyme() { return acronyme; } 
    public void setAcronyme(String v) { acronyme = v; }
    public String getTerme() { return terme; } 
    public void setTerme(String v) { terme = v; }
    public Integer getOrdre() { return ordre; } 
    public void setOrdre(Integer v) { ordre = v; }
}
