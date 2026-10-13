package com.example.sys_sop.dto;


public class DirectionResponseDto {

    private String codeDirection;
    private String libelleDirection;
    private String codeAbrev;

    public DirectionResponseDto() {}

    public DirectionResponseDto(String codeDirection, String libelleDirection, String codeAbrev) {
        this.codeDirection = codeDirection;
        this.libelleDirection = libelleDirection;
        this.codeAbrev = codeAbrev;
    }

    public String getCodeDirection() { return codeDirection; }
    public void setCodeDirection(String codeDirection) { this.codeDirection = codeDirection; }

    public String getLibelleDirection() { return libelleDirection; }
    public void setLibelleDirection(String libelleDirection) { this.libelleDirection = libelleDirection; }

    public String getCodeAbrev() { return codeAbrev; }
    public void setCodeAbrev(String codeAbrev) { this.codeAbrev = codeAbrev; }
}
