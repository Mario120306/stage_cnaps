package com.example.sys_sop.dto;

import java.time.LocalDateTime;

public class ExportDto {
    private String idExport, idSop, idVersion, libelleExport, nomFichierPdf, generePar;
    private Integer nbPages;
    private LocalDateTime dateExport;

    public ExportDto() {}
    public ExportDto(String idExport, String idSop, String idVersion, String libelleExport, Integer nbPages,
            LocalDateTime dateExport, String nomFichierPdf, String generePar) {
        this.idExport = idExport; this.idSop = idSop; this.idVersion = idVersion; this.libelleExport = libelleExport;
        this.nbPages = nbPages; this.dateExport = dateExport; this.nomFichierPdf = nomFichierPdf; this.generePar = generePar;
    }
    public String getIdExport() { return idExport; } 
    public void setIdExport(String v) { idExport = v; }
    public String getIdSop() { return idSop; } 
    public void setIdSop(String v) { idSop = v; }
    public String getIdVersion() { return idVersion; } 
    public void setIdVersion(String v) { idVersion = v; }
    public String getLibelleExport() { return libelleExport; } 
    public void setLibelleExport(String v) { libelleExport = v; }
    public Integer getNbPages() { return nbPages; } 
    public void setNbPages(Integer v) { nbPages = v; }
    public LocalDateTime getDateExport() { return dateExport; } 
    public void setDateExport(LocalDateTime v) { dateExport = v; }
    public String getNomFichierPdf() { return nomFichierPdf; } 
    public void setNomFichierPdf(String v) { nomFichierPdf = v; }
    public String getGenerePar() { return generePar; } 
    public void setGenerePar(String v) { generePar = v; }
}
