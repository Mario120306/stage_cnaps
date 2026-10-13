package com.example.sys_sop.dto;

public class ServiceDrhResponseDto {
    
    private String codeService;
    private String libelleService;
    private String codeDirection;
    
    public ServiceDrhResponseDto() {
    }
    public ServiceDrhResponseDto(String codeService, String libelleService, String codeDirection) {
        this.codeService = codeService;
        this.libelleService = libelleService;
        this.codeDirection = codeDirection;
    }
    public String getCodeService() {return codeService;}
    public void setCodeService(String codeService) {this.codeService = codeService;}
    public String getLibelleService() {return libelleService;}
    public void setLibelleService(String libelleService) {this.libelleService = libelleService;}
    public String getCodeDirection() {return codeDirection;}
    public void setCodeDirection(String codeDirection) {this.codeDirection = codeDirection;}
}
