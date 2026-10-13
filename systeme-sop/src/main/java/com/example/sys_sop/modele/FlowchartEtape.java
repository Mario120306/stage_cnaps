package com.example.sys_sop.modele;

public class FlowchartEtape {
    private String idEtape, idFlowchart, information, modeNumerotation;
    private Integer niveau, ordre;

    public FlowchartEtape() {}
    public FlowchartEtape(String idEtape, String idFlowchart, String information, Integer niveau,
            String modeNumerotation, Integer ordre) {
        this.idEtape = idEtape; this.idFlowchart = idFlowchart; this.information = information;
        this.niveau = niveau; this.modeNumerotation = modeNumerotation; this.ordre = ordre;
    }
    public String getIdEtape() { return idEtape; } 
    public void setIdEtape(String v) { idEtape = v; }
    public String getIdFlowchart() { return idFlowchart; } 
    public void setIdFlowchart(String v) { idFlowchart = v; }
    public String getInformation() { return information; } 
    public void setInformation(String v) { information = v; }
    public Integer getNiveau() { return niveau; } 
    public void setNiveau(Integer v) { niveau = v; }
    public String getModeNumerotation() { return modeNumerotation; } 
    public void setModeNumerotation(String v) { modeNumerotation = v; }
    public Integer getOrdre() { return ordre; } 
    public void setOrdre(Integer v) { ordre = v; }
}
