package com.example.sys_sop.dto;

import java.util.List;

public class SopCreationDto extends SopDto {
    private List<ContactDto> contacts;
    private List<DestinataireDto> destinataires;
    private List<EquipeProjetDto> equipeProjet;
    private List<GlossaireDto> glossaire;
    private List<ReferenceDto> references;
    private List<PieceJointeDto> piecesJointes;
    private List<AnnexeDto> annexes;
    private List<FlowchartDto> flowcharts;
    private List<FlowchartEtapeDto> flowchartEtapes;

    public List<ContactDto> getContacts() { return contacts; }
    public void setContacts(List<ContactDto> contacts) { this.contacts = contacts; }
    public List<DestinataireDto> getDestinataires() { return destinataires; }
    public void setDestinataires(List<DestinataireDto> destinataires) { this.destinataires = destinataires; }
    public List<EquipeProjetDto> getEquipeProjet() { return equipeProjet; }
    public void setEquipeProjet(List<EquipeProjetDto> equipeProjet) { this.equipeProjet = equipeProjet; }
    public List<GlossaireDto> getGlossaire() { return glossaire; }
    public void setGlossaire(List<GlossaireDto> glossaire) { this.glossaire = glossaire; }
    public List<ReferenceDto> getReferences() { return references; }
    public void setReferences(List<ReferenceDto> references) { this.references = references; }
    public List<PieceJointeDto> getPiecesJointes() { return piecesJointes; }
    public void setPiecesJointes(List<PieceJointeDto> piecesJointes) { this.piecesJointes = piecesJointes; }
    public List<AnnexeDto> getAnnexes() { return annexes; }
    public void setAnnexes(List<AnnexeDto> annexes) { this.annexes = annexes; }
    public List<FlowchartDto> getFlowcharts() { return flowcharts; }
    public void setFlowcharts(List<FlowchartDto> flowcharts) { this.flowcharts = flowcharts; }
    public List<FlowchartEtapeDto> getFlowchartEtapes() { return flowchartEtapes; }
    public void setFlowchartEtapes(List<FlowchartEtapeDto> flowchartEtapes) { this.flowchartEtapes = flowchartEtapes; }
}
