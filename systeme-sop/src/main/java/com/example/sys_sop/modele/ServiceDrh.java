package com.example.sys_sop.modele;

public class ServiceDrh {
    
    private String codeService;
    private String libelleService;
    private String codeDirection;
    
    public ServiceDrh() {
    }
    public ServiceDrh(String codeService, String libelleService, String codeDirection) {
        this.codeService = codeService;
        this.libelleService = libelleService;
        this.codeDirection = codeDirection;
    }
    public String getCodeService() {return codeService;}
    public String getLibelleService() {return libelleService;}
    public String getCodeDirection() {return codeDirection;} 
}
