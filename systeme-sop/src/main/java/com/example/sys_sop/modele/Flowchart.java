package com.example.sys_sop.modele;

public class Flowchart {
    private String idFlowchart, idSop, titre, information, idPieceJointe;
    private Integer ordre;

    public Flowchart() {}
    public Flowchart(String idFlowchart, String idSop, String titre, String information, Integer ordre, String idPieceJointe) {
        this.idFlowchart = idFlowchart; this.idSop = idSop; this.titre = titre; this.information = information; this.ordre = ordre; this.idPieceJointe = idPieceJointe;
    }
    public String getIdFlowchart() { return idFlowchart; } 
    public void setIdFlowchart(String v) { idFlowchart = v; }
    public String getIdSop() { return idSop; } 
    public void setIdSop(String v) { idSop = v; }
    public String getTitre() { return titre; } 
    public void setTitre(String v) { titre = v; }
    public String getInformation() { return information; }
    public void setInformation(String v) { information = v; }
    public Integer getOrdre() { return ordre; } 
    public void setOrdre(Integer v) { ordre = v; }
    public String getIdPieceJointe() { return idPieceJointe; } 
    public void setIdPieceJointe(String v) { idPieceJointe = v; }
}
