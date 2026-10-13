package com.example.sys_sop.modele;

import java.time.LocalDateTime;

public class PieceJointe {
    private String idPieceJointe, idSop, usage, nomFichier, typeMime, auteurMatricule;
    private Long taille;
    private byte[] contenu;
    private LocalDateTime dateInsert;

    public PieceJointe() {}
    public PieceJointe(String idPieceJointe, String idSop, String usage, String nomFichier, String typeMime,
            Long taille, byte[] contenu, String auteurMatricule, LocalDateTime dateInsert) {
        this.idPieceJointe = idPieceJointe; this.idSop = idSop; this.usage = usage; this.nomFichier = nomFichier;
        this.typeMime = typeMime; this.taille = taille; this.contenu = contenu;
        this.auteurMatricule = auteurMatricule; this.dateInsert = dateInsert;
    }
    public String getIdPieceJointe() { return idPieceJointe; } 
    public void setIdPieceJointe(String v) { idPieceJointe = v; }
    public String getIdSop() { return idSop; } 
    public void setIdSop(String v) { idSop = v; }
    public String getUsage() { return usage; } 
    public void setUsage(String v) { usage = v; }
    public String getNomFichier() { return nomFichier; } 
    public void setNomFichier(String v) { nomFichier = v; }
    public String getTypeMime() { return typeMime; } 
    public void setTypeMime(String v) { typeMime = v; }
    public Long getTaille() { return taille; } 
    public void setTaille(Long v) { taille = v; }
    public byte[] getContenu() { return contenu; } 
    public void setContenu(byte[] v) { contenu = v; }
    public String getAuteurMatricule() { return auteurMatricule; } 
    public void setAuteurMatricule(String v) { auteurMatricule = v; }
    public LocalDateTime getDateInsert() { return dateInsert; } 
    public void setDateInsert(LocalDateTime v) { dateInsert = v; }
}
