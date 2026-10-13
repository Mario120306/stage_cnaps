package com.example.sys_sop.modele;

import java.time.LocalDate;

public class TextStatique {
    
    private String      codeText;
    private String      contenu;
    private LocalDate   dateMaj;
    private String      actif;
    
    public TextStatique() {
    }
    public TextStatique(String codeText, String contenu, LocalDate dateMaj, String actif) {
        this.codeText = codeText;
        this.contenu = contenu;
        this.dateMaj = dateMaj;
        this.actif = actif;
    }
    public String getCodeText() {
    return codeText;}
    public String getContenu() {return contenu;}
    public LocalDate getDateMaj() {return dateMaj;}
    public String getActif() {return actif;}
}
