package com.example.sys_sop.dto;

import java.time.LocalDateTime;

public class VersionDto {
    private String idVersion, idSop, numeroVersion, libelleModification, auteurModification, codeStatut;
    private LocalDateTime dateEntree;

    public VersionDto() {}
    public VersionDto(String idVersion, String idSop, String numeroVersion, LocalDateTime dateEntree,
            String libelleModification, String auteurModification, String codeStatut) {
        this.idVersion = idVersion; this.idSop = idSop; this.numeroVersion = numeroVersion; this.dateEntree = dateEntree;
        this.libelleModification = libelleModification; this.auteurModification = auteurModification; this.codeStatut = codeStatut;
    }
    public String getIdVersion() { return idVersion; } public void setIdVersion(String v) { idVersion = v; }
    public String getIdSop() { return idSop; } public void setIdSop(String v) { idSop = v; }
    public String getNumeroVersion() { return numeroVersion; } public void setNumeroVersion(String v) { numeroVersion = v; }
    public LocalDateTime getDateEntree() { return dateEntree; } public void setDateEntree(LocalDateTime v) { dateEntree = v; }
    public String getLibelleModification() { return libelleModification; } public void setLibelleModification(String v) { libelleModification = v; }
    public String getAuteurModification() { return auteurModification; } public void setAuteurModification(String v) { auteurModification = v; }
    public String getCodeStatut() { return codeStatut; } public void setCodeStatut(String v) { codeStatut = v; }
}
