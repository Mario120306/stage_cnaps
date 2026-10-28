package com.example.sys_sop.dto;

import java.util.List;

public class SopDetailsDto {
    private final SopDto sop;
    private final List<ContactDto> contacts;
    private final List<DestinataireDto> destinataires;
    private final List<EquipeProjetDto> equipeProjet;
    private final List<GlossaireDto> glossaire;
    private final List<ReferenceDto> references;
    private final List<VersionDto> versions;
    private final List<FlowchartDto> flowcharts;
    private final List<FlowchartEtapeDto> flowchartEtapes;
    private final List<PieceJointeDetailDto> piecesJointes;
    private final List<AnnexeDto> annexes;
    private final boolean createur;

    public SopDetailsDto(SopDto sop, List<ContactDto> contacts, List<DestinataireDto> destinataires,
            List<EquipeProjetDto> equipeProjet, List<GlossaireDto> glossaire, List<ReferenceDto> references,
            List<VersionDto> versions, List<FlowchartDto> flowcharts, List<FlowchartEtapeDto> flowchartEtapes,
            List<PieceJointeDetailDto> piecesJointes, List<AnnexeDto> annexes, boolean createur) {
        this.sop = sop;
        this.contacts = contacts;
        this.destinataires = destinataires;
        this.equipeProjet = equipeProjet;
        this.glossaire = glossaire;
        this.references = references;
        this.versions = versions;
        this.flowcharts = flowcharts;
        this.flowchartEtapes = flowchartEtapes;
        this.piecesJointes = piecesJointes;
        this.annexes = annexes;
        this.createur = createur;
    }

    public SopDto getSop() { return sop; }
    public List<ContactDto> getContacts() { return contacts; }
    public List<DestinataireDto> getDestinataires() { return destinataires; }
    public List<EquipeProjetDto> getEquipeProjet() { return equipeProjet; }
    public List<GlossaireDto> getGlossaire() { return glossaire; }
    public List<ReferenceDto> getReferences() { return references; }
    public List<VersionDto> getVersions() { return versions; }
    public List<FlowchartDto> getFlowcharts() { return flowcharts; }
    public List<FlowchartEtapeDto> getFlowchartEtapes() { return flowchartEtapes; }
    public List<PieceJointeDetailDto> getPiecesJointes() { return piecesJointes; }
    public List<AnnexeDto> getAnnexes() { return annexes; }
    public boolean isCreateur() { return createur; }
}
