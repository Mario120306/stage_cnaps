package com.example.sys_sop.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.sys_sop.dto.AnnexeDto;
import com.example.sys_sop.dto.ContactDto;
import com.example.sys_sop.dto.DestinataireDto;
import com.example.sys_sop.dto.EquipeProjetDto;
import com.example.sys_sop.dto.FlowchartDto;
import com.example.sys_sop.dto.FlowchartEtapeDto;
import com.example.sys_sop.dto.GlossaireDto;
import com.example.sys_sop.dto.PieceJointeDto;
import com.example.sys_sop.dto.ReferenceDto;
import com.example.sys_sop.dto.SopCreationDto;
import com.example.sys_sop.modele.Annexe;
import com.example.sys_sop.modele.Contact;
import com.example.sys_sop.modele.Destinataire;
import com.example.sys_sop.modele.EquipeProjet;
import com.example.sys_sop.modele.Flowchart;
import com.example.sys_sop.modele.FlowchartEtape;
import com.example.sys_sop.modele.Glossaire;
import com.example.sys_sop.modele.PieceJointe;
import com.example.sys_sop.modele.Reference;
import com.example.sys_sop.modele.Sop;
import com.example.sys_sop.modele.Version;
import com.example.sys_sop.modele.Export;
import com.example.sys_sop.repository.AnnexeRepository;
import com.example.sys_sop.repository.ContactRepository;
import com.example.sys_sop.repository.DestinataireRepository;
import com.example.sys_sop.repository.EquipeProjetRepository;
import com.example.sys_sop.repository.FlowchartEtapeRepository;
import com.example.sys_sop.repository.FlowchartRepository;
import com.example.sys_sop.repository.GlossaireRepository;
import com.example.sys_sop.repository.PieceJointeRepository;
import com.example.sys_sop.repository.ReferenceRepository;
import com.example.sys_sop.repository.SopRepository;
import com.example.sys_sop.repository.VersionRepository;
import com.example.sys_sop.repository.ExportRepository;

@Service
public class SopService {
    private final SopRepository repository;
    private final VersionRepository versionRepository;
    private final ContactRepository contactRepository;
    private final DestinataireRepository destinataireRepository;
    private final EquipeProjetRepository equipeProjetRepository;
    private final GlossaireRepository glossaireRepository;
    private final ReferenceRepository referenceRepository;
    private final PieceJointeRepository pieceJointeRepository;
    private final AnnexeRepository annexeRepository;
    private final FlowchartRepository flowchartRepository;
    private final FlowchartEtapeRepository flowchartEtapeRepository;
    private final ExportRepository exportRepository;
    private final PdfExportService pdfExportService;

    public SopService(
            SopRepository repository,
            VersionRepository versionRepository,
            ContactRepository contactRepository,
            DestinataireRepository destinataireRepository,
            EquipeProjetRepository equipeProjetRepository,
            GlossaireRepository glossaireRepository,
            ReferenceRepository referenceRepository,
            PieceJointeRepository pieceJointeRepository,
            AnnexeRepository annexeRepository,
            FlowchartRepository flowchartRepository,
            FlowchartEtapeRepository flowchartEtapeRepository,
            ExportRepository exportRepository,
            PdfExportService pdfExportService) {
        this.repository = repository;
        this.versionRepository = versionRepository;
        this.contactRepository = contactRepository;
        this.destinataireRepository = destinataireRepository;
        this.equipeProjetRepository = equipeProjetRepository;
        this.glossaireRepository = glossaireRepository;
        this.referenceRepository = referenceRepository;
        this.pieceJointeRepository = pieceJointeRepository;
        this.annexeRepository = annexeRepository;
        this.flowchartRepository = flowchartRepository;
        this.flowchartEtapeRepository = flowchartEtapeRepository;
        this.exportRepository = exportRepository;
        this.pdfExportService = pdfExportService;
    }
    public List<Sop> findAll() { 
        return repository.findAll(); 
    }
    public Sop findById(String id) { 
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SOP introuvable : " + id)); 
    }
    public Sop create(Sop entity) { 
        repository.create(entity); return entity; 
    }

    @Transactional
    public Sop create(SopCreationDto request) {
        if (request == null || request.getTitre() == null || request.getTitre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le titre du SOP est obligatoire.");
        }
        if (request.getCodeService() == null || request.getCodeService().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le service du créateur est obligatoire.");
        }
        if (request.getAuteurMatricule() == null || request.getAuteurMatricule().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le matricule du créateur est obligatoire.");
        }

        long numero = repository.countSops() + 1;
        String numeroSop = String.format(java.util.Locale.ROOT, "SOP%03d", numero);
        Sop sop = new Sop(numeroSop, numeroSop, request.getTitre(), "1",
                request.getCodeService(), request.getAuteurMatricule(), 0,
                "STAT001", "N", null, null, null, null, null);
        repository.create(sop);

        String idVersion = Long.toString(versionRepository.countAll() + 1);
        versionRepository.create(new Version(idVersion, numeroSop, "1", null, null, null, "STAT001"));

        for (ContactDto contact : values(request.getContacts())) {
            contactRepository.create(new Contact(newId(), numeroSop, contact.getMatricule(),
                    contact.getEmail(), contact.getOrdre()));
        }
        for (DestinataireDto destinataire : values(request.getDestinataires())) {
            destinataireRepository.create(new Destinataire(newId(), numeroSop, destinataire.getMatricule(),
                    destinataire.getNom(), destinataire.getOrganisation(), destinataire.getAction(),
                    destinataire.getDateAction(), destinataire.getOrdre()));
        }
        for (EquipeProjetDto membre : values(request.getEquipeProjet())) {
            equipeProjetRepository.create(new EquipeProjet(newId(), numeroSop, membre.getMatricule(),
                    membre.getNom(), membre.getFonction(), membre.getTelephone(), membre.getEmail(), membre.getOrdre()));
        }
        for (GlossaireDto terme : values(request.getGlossaire())) {
            glossaireRepository.create(new Glossaire(newId(), numeroSop, terme.getAcronyme(),
                    terme.getTerme(), terme.getOrdre()));
        }
        for (ReferenceDto reference : values(request.getReferences())) {
            referenceRepository.create(new Reference(newId(), numeroSop, reference.getContenu(), reference.getOrdre()));
        }

        Map<String, String> idsPiecesJointes = new HashMap<>();
        for (PieceJointeDto piece : values(request.getPiecesJointes())) {
            String cle = requiredKey(piece.getIdPieceJointe(), "pièce jointe");
            String idPieceJointe = newId();
            idsPiecesJointes.put(cle, idPieceJointe);
            byte[] contenu = piece.getContenu();
            Long taille = piece.getTaille() == null && contenu != null
                    ? (long) contenu.length : piece.getTaille();
            pieceJointeRepository.create(new PieceJointe(idPieceJointe, numeroSop, piece.getUsage(),
                    piece.getNomFichier(), piece.getTypeMime(), taille, contenu,
                    request.getAuteurMatricule(), null));
        }

        for (AnnexeDto annexe : values(request.getAnnexes())) {
            annexeRepository.create(new Annexe(newId(), numeroSop, annexe.getTitre(),
                    mappedId(annexe.getIdPieceJointe(), idsPiecesJointes, "annexe"), annexe.getOrdre()));
        }

        Map<String, String> idsFlowcharts = new HashMap<>();
        for (FlowchartDto flowchart : values(request.getFlowcharts())) {
            String cle = requiredKey(flowchart.getIdFlowchart(), "flowchart");
            String idFlowchart = newId();
            idsFlowcharts.put(cle, idFlowchart);
            flowchartRepository.create(new Flowchart(idFlowchart, numeroSop, flowchart.getTitre(),
                    flowchart.getInformation(), flowchart.getOrdre(),
                    mappedId(flowchart.getIdPieceJointe(), idsPiecesJointes, "flowchart")));
        }

        for (FlowchartEtapeDto etape : values(request.getFlowchartEtapes())) {
            String idFlowchart = mappedId(etape.getIdFlowchart(), idsFlowcharts, "étape flowchart");
            String modeNumerotation = etape.getModeNumerotation() == null
                    ? "AUTO" : etape.getModeNumerotation();
            flowchartEtapeRepository.create(new FlowchartEtape(newId(), idFlowchart, etape.getInformation(),
                    etape.getNiveau(), modeNumerotation, etape.getOrdre()));
        }

        byte[] pdf = pdfExportService.generate(sop);
        String nomFichierPdf = numeroSop +"_"+ sop.getTitre()+"_"+ "CNaPS" + "_v" + sop.getVersionCourante() + ".pdf";
        exportRepository.create(new Export(newId(), numeroSop, idVersion, "Draft version", 1, null,
                nomFichierPdf, request.getAuteurMatricule()), pdf);

        return repository.findById(numeroSop).orElse(sop);
    }
    public boolean update(Sop entity) { 
        return repository.update(entity) > 0; 
    }
    public boolean deleteById(String id) { 
        return repository.deleteById(id) > 0; 
    }

    private static String newId() {
        return UUID.randomUUID().toString();
    }

    private static String requiredKey(String key, String type) {
        if (key == null || key.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identifiant manquant pour " + type + ".");
        }
        return key;
    }

    private static String mappedId(String key, Map<String, String> ids, String type) {
        if (key == null) {
            return null;
        }
        String mapped = ids.get(key);
        if (mapped == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Référence invalide pour " + type + ".");
        }
        return mapped;
    }

    private static <T> List<T> values(List<T> values) {
        return values == null ? Collections.emptyList() : values;
    }

    /**
     * Returns a paginated result with metadata.
     * response keys: content (List<Sop>), page (int), size (int), totalElements (int), totalPages (int)
     */
    public Map<String, Object> findPage(int page, int size, String titre, String codeDirection, String codeService, String auteur, LocalDate dateFrom, LocalDate dateTo) {
        int total = repository.countFiltered(titre, codeDirection, codeService, auteur, dateFrom, dateTo);
        List<Sop> content = repository.findPage(page, size, titre, codeDirection, codeService, auteur, dateFrom, dateTo);
        int totalPages = (int) Math.ceil((double) total / (double) size);

        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("page", page);
        result.put("size", size);
        result.put("totalElements", total);
        result.put("totalPages", totalPages);
        return result;
    }
}
