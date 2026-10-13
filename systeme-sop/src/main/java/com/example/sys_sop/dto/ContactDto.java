package com.example.sys_sop.dto;

public class ContactDto {
    private String idContact, idSop, matricule, email;
    private Integer ordre;

    public ContactDto() {}
    public ContactDto(String idContact, String idSop, String matricule, String email, Integer ordre) {
        this.idContact = idContact; this.idSop = idSop; this.matricule = matricule; this.email = email; this.ordre = ordre;
    }
    public String getIdContact() { return idContact; } public void setIdContact(String v) { idContact = v; }
    public String getIdSop() { return idSop; } public void setIdSop(String v) { idSop = v; }
    public String getMatricule() { return matricule; } public void setMatricule(String v) { matricule = v; }
    public String getEmail() { return email; } public void setEmail(String v) { email = v; }
    public Integer getOrdre() { return ordre; } public void setOrdre(Integer v) { ordre = v; }
}
