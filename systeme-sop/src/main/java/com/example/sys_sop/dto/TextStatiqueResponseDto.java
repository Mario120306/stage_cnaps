package com.example.sys_sop.dto;

import java.time.LocalDate;

public class TextStatiqueResponseDto {

	private String codeText;
	private String contenu;
	private LocalDate dateMaj;
	private String actif;

	public TextStatiqueResponseDto() {}

	public TextStatiqueResponseDto(String codeText, String contenu, LocalDate dateMaj, String actif) {
		this.codeText = codeText;
		this.contenu = contenu;
		this.dateMaj = dateMaj;
		this.actif = actif;
	}
	public String getCodeText() { return codeText; }
	public void setCodeText(String codeText) { this.codeText = codeText; }
	public String getContenu() { return contenu; }
	public void setContenu(String contenu) { this.contenu = contenu; }
	public LocalDate getDateMaj() { return dateMaj; }
	public void setDateMaj(LocalDate dateMaj) { this.dateMaj = dateMaj; }
	public String getActif() { return actif; }
	public void setActif(String actif) { this.actif = actif; }
}
