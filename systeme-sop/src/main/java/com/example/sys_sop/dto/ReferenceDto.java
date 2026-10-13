package com.example.sys_sop.dto;

public class ReferenceDto {
    private String idReference, idSop, contenu;
    private Integer ordre;

    public ReferenceDto() {}
    public ReferenceDto(String idReference, String idSop, String contenu, Integer ordre) {
        this.idReference = idReference; this.idSop = idSop; this.contenu = contenu; this.ordre = ordre;
    }
    public String getIdReference() { return idReference; } 
    public void setIdReference(String v) { idReference = v; }
    public String getIdSop() { return idSop; } 
    public void setIdSop(String v) { idSop = v; }
    public String getContenu() { return contenu; } 
    public void setContenu(String v) { contenu = v; }
    public Integer getOrdre() { return ordre; } 
    public void setOrdre(Integer v) { ordre = v; }
}
