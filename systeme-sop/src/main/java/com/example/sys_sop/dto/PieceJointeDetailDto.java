package com.example.sys_sop.dto;

public class PieceJointeDetailDto {
    private String idPieceJointe;
    private String nomFichier;
    private String typeMime;
    private String usage;

    public PieceJointeDetailDto() {}

    public PieceJointeDetailDto(String idPieceJointe, String nomFichier, String typeMime, String usage) {
        this.idPieceJointe = idPieceJointe;
        this.nomFichier = nomFichier;
        this.typeMime = typeMime;
        this.usage = usage;
    }

    public String getIdPieceJointe() { return idPieceJointe; }
    public String getNomFichier() { return nomFichier; }
    public String getTypeMime() { return typeMime; }
    public String getUsage() { return usage; }
}
