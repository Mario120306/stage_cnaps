package com.example.sys_sop.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

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
import com.example.sys_sop.modele.Utilisateur;
import com.example.sys_sop.modele.Version;
import com.example.sys_sop.repository.AnnexeRepository;
import com.example.sys_sop.repository.ContactRepository;
import com.example.sys_sop.repository.DestinataireRepository;
import com.example.sys_sop.repository.EquipeProjetRepository;
import com.example.sys_sop.repository.FlowchartEtapeRepository;
import com.example.sys_sop.repository.FlowchartRepository;
import com.example.sys_sop.repository.GlossaireRepository;
import com.example.sys_sop.repository.PieceJointeRepository;
import com.example.sys_sop.repository.ReferenceRepository;
import com.example.sys_sop.repository.ServiceDrhRepository;
import com.example.sys_sop.repository.UtilisateurRepository;
import com.example.sys_sop.repository.VersionRepository;

@Service
public class PdfExportService {

    // ===== Codes des textes statiques =====
    private static final String TXT_SOP = "TXT001";
    private static final String TXT_MANUEL = "TXT002";
    private static final String TXT_SLA = "TXT003";
    private static final String TXT_TITRE_DOC = "TXT004";
    private static final String TXT_NOTE_PUB = "TXT005";
    private static final String TXT_GUIDE_PUB = "TXT006";
    private static final String TXT_CAISSE = "TXT007";
    private static final String TXT_CONFIDENTIEL = "TXT008";
    private static final String TXT_MARQUE = "TXT009";
    private static final String TXT_MARQUES_RECONNUES = "TXT010";
    private static final String TXT_COMMENTAIRES = "TXT011";
    private static final String TXT_MERCI = "TXT012";
    private static final String TXT_ET = "TXT013";
    private static final String TXT_HISTORIQUE = "TXT014";
    private static final String TXT_DESTINATAIRES = "TXT015";
    private static final String TXT_EQUIPE = "TXT016";
    private static final String TXT_TABLE_MATIERES = "TXT017";
    private static final String TXT_GLOSSAIRE = "TXT018";
    private static final String TXT_REFERENCES = "TXT019";
    private static final String TXT_FLOWCHART = "TXT020";
    private static final String TXT_PROCEDURE = "TXT021";
    private static final String TXT_ETAPES = "TXT023";
    private static final String TXT_ANNEXES = "TXT024";
    private static final String TXT_ORGANISATION = "TXT025";
    private static final String ORGANISATION_DEFAUT = "CNaPS";

    // ===== Police standard PDF et tailles =====
    private static final PDType1Font F_REG = PDType1Font.HELVETICA;
    private static final PDType1Font F_BOLD = PDType1Font.HELVETICA_BOLD;
    private static final float SIZE_BODY = 11f;      // paragraphes, étapes, références
    private static final float SIZE_TABLE = 11f;     // contenu des tableaux
    private static final float SIZE_TOC = 11f;       // table des matières
    private static final float SIZE_SECTION = 14f;   // titre niveau 1 (bleu #2E74B5)
    private static final float SIZE_H2 = 13f;        // titre niveau 2 (bleu #2E74B5)
    private static final float SIZE_SUB = 11f;       // titre niveau 3 (bleu foncé #1F4E79)
    private static final float SIZE_TITRE_DOC = 11f;
    private static final float SIZE_HEADER = 9f;

    // ===== Couleurs =====
    private static final float[] COLOR_TITRE = {46 / 255f, 116 / 255f, 181 / 255f};     // #2E74B5
    private static final float[] COLOR_SOUS_TITRE = {31 / 255f, 78 / 255f, 121 / 255f}; // #1F4E79
    private static final float[] COLOR_LIEN = {5 / 255f, 99 / 255f, 193 / 255f};
    private static final float[] COLOR_NOIR = {0f, 0f, 0f};
    private static final float GREY_HEADER = 0.65f;

    // ===== Mise en page =====
    private static final float PAGE_W = PDRectangle.A4.getWidth();
    private static final float MARGIN_LEFT = 71;
    private static final float MARGIN_RIGHT = 69;
    private static final float CONTENT_WIDTH = PAGE_W - MARGIN_LEFT - MARGIN_RIGHT;
    private static final float RIGHT_EDGE = PAGE_W - MARGIN_RIGHT;
    private static final float CONTENT_TOP = 719;
    private static final float CONTENT_BOTTOM = 80;

    private static final float LINE_HEIGHT = 13f;
    private static final float REF_LINE_HEIGHT = 17f;
    private static final float TABLE_LINE_HEIGHT = 13f;
    private static final float TABLE_ROW_PADDING = 4f;
    private static final float TOC_LINE_HEIGHT = 13f;

    private static final float PARAGRAPH_SPACING = 5f;
    private static final float REF_PARAGRAPH_SPACING = 6f;
    private static final float STEP_SPACING = 1.5f;
    private static final float SECTION_SPACE_BEFORE = 11f;
    private static final float SECTION_SPACE_AFTER = 14f;
    private static final float H2_AFTER = 15f;
    private static final float H3_AFTER = 14f;
    private static final float HEADING_BEFORE_TABLE = 6f;
    private static final float TABLE_SPACE_AFTER = 36f;
    private static final float CELL_PAD = 4f;

    private static final float TOC_SPACE_AFTER = 5.5f;
    private static final float TOC_INDENT = 11f;

    // Retraits des étapes (x du numéro / x du texte) selon la profondeur 0..8
    // 0 = ligne de groupe ("1. Constatation des recettes"), 1 = "1.1.", 2 = "1.1.1." ...
    private static final float[] STEP_NUM_X = {71, 80, 129, 174, 224, 270, 310, 350, 390};
    private static final float[] STEP_TEXT_X = {97, 129, 174, 224, 270, 310, 350, 390, 430};

    private static final String BULLET_L1 = "\u2022\uF0B7\u2023";
    private static final String BULLET_L2 = "\u25E6\u25CB\u25AA\uF0A7";

    private static final Charset WIN_1252 = Charset.forName("windows-1252");
    private static final Charset CP850 = Charset.forName("IBM850");
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);

    private static final String[] UNITES = {"z\u00e9ro", "un", "deux", "trois", "quatre", "cinq", "six", "sept",
            "huit", "neuf", "dix", "onze", "douze", "treize", "quatorze", "quinze", "seize", "dix-sept",
            "dix-huit", "dix-neuf"};
    private static final String[] DIZAINES = {"", "", "vingt", "trente", "quarante", "cinquante", "soixante"};

    private final TextStatiqueService textStatiqueService;
    private final VersionRepository versionRepository;
    private final ContactRepository contactRepository;
    private final DestinataireRepository destinataireRepository;
    private final EquipeProjetRepository equipeRepository;
    private final GlossaireRepository glossaireRepository;
    private final ReferenceRepository referenceRepository;
    private final FlowchartRepository flowchartRepository;
    private final FlowchartEtapeRepository etapeRepository;
    private final AnnexeRepository annexeRepository;
    private final PieceJointeRepository pieceJointeRepository;
    private final ServiceDrhRepository serviceDrhRepository;
    private final UtilisateurRepository utilisateurRepository;

    public PdfExportService(TextStatiqueService textStatiqueService, VersionRepository versionRepository,
            ContactRepository contactRepository, DestinataireRepository destinataireRepository,
            EquipeProjetRepository equipeRepository, GlossaireRepository glossaireRepository,
            ReferenceRepository referenceRepository, FlowchartRepository flowchartRepository,
            FlowchartEtapeRepository etapeRepository, AnnexeRepository annexeRepository,
            PieceJointeRepository pieceJointeRepository, ServiceDrhRepository serviceDrhRepository,
            UtilisateurRepository utilisateurRepository) {
        this.textStatiqueService = textStatiqueService;
        this.versionRepository = versionRepository;
        this.contactRepository = contactRepository;
        this.destinataireRepository = destinataireRepository;
        this.equipeRepository = equipeRepository;
        this.glossaireRepository = glossaireRepository;
        this.referenceRepository = referenceRepository;
        this.flowchartRepository = flowchartRepository;
        this.etapeRepository = etapeRepository;
        this.annexeRepository = annexeRepository;
        this.pieceJointeRepository = pieceJointeRepository;
        this.serviceDrhRepository = serviceDrhRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    // ================= STRUCTURES INTERNES =================
    private static final class TocEntry {
        final String title;
        final int level;
        final int page;

        TocEntry(String title, int level, int page) {
            this.title = title;
            this.level = level;
            this.page = page;
        }
    }

    private static final class Span {
        final String text;
        final PDType1Font font;
        final float[] color;
        final boolean underline;
        final boolean highlight;

        Span(String text, PDType1Font font, float[] color, boolean underline, boolean highlight) {
            this.text = text;
            this.font = font;
            this.color = color;
            this.underline = underline;
            this.highlight = highlight;
        }

        static Span plain(String t) { return new Span(t, F_REG, COLOR_NOIR, false, false); }
        static Span bold(String t) { return new Span(t, F_BOLD, COLOR_NOIR, false, false); }
        static Span highlight(String t) { return new Span(t, F_BOLD, COLOR_NOIR, false, true); }
        static Span link(String t) { return new Span(t, F_REG, COLOR_LIEN, true, false); }
    }

    private static final class Word {
        final String text;
        final Span style;
        final float width;
        final boolean noSpaceBefore;

        Word(String text, Span style, float width, boolean noSpaceBefore) {
            this.text = text;
            this.style = style;
            this.width = width;
            this.noSpaceBefore = noSpaceBefore;
        }
    }

    private static final class Built {
        final byte[] bytes;
        final int tocPagesNeeded;

        Built(byte[] bytes, int tocPagesNeeded) {
            this.bytes = bytes;
            this.tocPagesNeeded = tocPagesNeeded;
        }
    }

    private static class PageContext {
        final PDDocument document;
        final List<TocEntry> toc = new ArrayList<>();
        PDPage page;
        PDPageContentStream content;
        float y;
        int currentPage;

        PageContext(PDDocument document) {
            this.document = document;
        }

        void newPage() throws IOException {
            if (content != null) content.close();
            page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            currentPage++;
            content = new PDPageContentStream(document, page);
            y = CONTENT_TOP;
        }

        void checkPageBreak(float height) throws IOException {
            if (y - height < CONTENT_BOTTOM) newPage();
        }

        void close() throws IOException {
            if (content != null) {
                content.close();
                content = null;
            }
        }
    }

    // ================= GÉNÉRATION =================
    public byte[] generate(Sop sop) {
        try {
            Built built = build(sop, 1);
            if (built.tocPagesNeeded > 1) {
                built = build(sop, built.tocPagesNeeded); // sommaire sur plusieurs pages
            }
            return built.bytes;
        } catch (IOException ex) {
            throw new IllegalStateException("Impossible de générer le PDF du SOP " + sop.getIdSop(), ex);
        }
    }

    private Built build(Sop sop, int tocPages) throws IOException {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            PDImageXObject logo = loadHeaderLogo(document);
            PageContext ctx = new PageContext(document);

            // Page 1 : garde + historique
            ctx.newPage();
            drawPageDeGarde(ctx, sop);
            drawHeading(ctx, staticText(TXT_HISTORIQUE), 0, HEADING_BEFORE_TABLE);
            drawHistorique(ctx, sop);

            // Page 2 : destinataires + équipe
            ctx.newPage();
            drawHeading(ctx, staticText(TXT_DESTINATAIRES), 0, HEADING_BEFORE_TABLE);
            drawDestinataires(ctx, sop);
            drawHeading(ctx, staticText(TXT_EQUIPE), 0, HEADING_BEFORE_TABLE);
            drawEquipe(ctx, sop);

            // Page(s) réservée(s) à la table des matières (remplie à la fin)
            List<PDPage> reserved = new ArrayList<>();
            for (int i = 0; i < tocPages; i++) {
                ctx.newPage();
                reserved.add(ctx.page);
            }

            ctx.newPage();
            drawHeading(ctx, staticText(TXT_GLOSSAIRE), 0, HEADING_BEFORE_TABLE);
            drawGlossaire(ctx, sop);

            ctx.newPage();
            drawHeading(ctx, staticText(TXT_REFERENCES), 0, SECTION_SPACE_AFTER);
            drawReferences(ctx, sop);

            ctx.newPage();
            drawHeading(ctx, staticText(TXT_FLOWCHART), 0, SECTION_SPACE_AFTER);
            drawFlowcharts(ctx, sop);

            ctx.newPage();
            drawHeading(ctx, staticText(TXT_ETAPES), 0, SECTION_SPACE_AFTER);
            drawEtapes(ctx, sop);

            ctx.newPage();
            drawHeading(ctx, staticText(TXT_ANNEXES), 0, SECTION_SPACE_AFTER);
            drawAnnexes(ctx, sop);
            ctx.close();

            int totalPages = document.getNumberOfPages();
            int needed = drawToc(document, reserved, ctx.toc, totalPages);

            for (int i = 0; i < totalPages; i++) {
                try (PDPageContentStream header = new PDPageContentStream(document, document.getPage(i),
                        PDPageContentStream.AppendMode.APPEND, true, true)) {
                    drawHeader(header, logo, sop, totalPages, i + 1);
                }
            }
            document.save(output);
            return new Built(output.toByteArray(), needed);
        }
    }

    // ================= PAGE DE GARDE =================
    private void drawPageDeGarde(PageContext ctx, Sop sop) throws IOException {
        drawCentered(ctx.content, staticText(TXT_TITRE_DOC), F_REG, SIZE_TITRE_DOC,
                MARGIN_LEFT + CONTENT_WIDTH / 2, ctx.y);
        ctx.y -= 40;

        // Ces trois titres figurent aussi dans la table des matières (comme la référence)
        drawHeading(ctx, staticText(TXT_NOTE_PUB), 0, SECTION_SPACE_AFTER, true);
        String service = serviceLabel(sop);
        drawRich(ctx, Arrays.asList(Span.plain(staticText(TXT_GUIDE_PUB)), Span.bold(service),
        Span.plain(staticText(TXT_CAISSE))), MARGIN_LEFT, CONTENT_WIDTH, true, null, 0, PARAGRAPH_SPACING);
        drawBodyText(ctx, staticText(TXT_CONFIDENTIEL));

        drawHeading(ctx, staticText(TXT_MARQUE), 0, SECTION_SPACE_AFTER, true);
        drawBodyText(ctx, staticText(TXT_MARQUES_RECONNUES));

        drawHeading(ctx, staticText(TXT_COMMENTAIRES), 0, SECTION_SPACE_AFTER, true);
        List<String> emails = contacts(sop).stream().map(Contact::getEmail)
                .filter(PdfExportService::notBlank).collect(Collectors.toList());
        List<Span> spans = new ArrayList<>();
        spans.add(Span.plain(staticText(TXT_MERCI)));
        for (int i = 0; i < emails.size(); i++) {
            boolean last = i == emails.size() - 1;
            boolean beforeLast = i == emails.size() - 2;
            String mail = emails.get(i).trim();
            spans.add(Span.link(last ? mail + "." : beforeLast ? mail : mail + ","));
            if (beforeLast) spans.add(Span.plain(staticText(TXT_ET)));
        }
        drawRich(ctx, spans, MARGIN_LEFT, CONTENT_WIDTH, true, null, 0, PARAGRAPH_SPACING);
    }

    // ================= SECTIONS À TABLEAUX =================
    private void drawHistorique(PageContext ctx, Sop sop) throws IOException {
        String[] headers = {"Date de sortie", "Version", "Modification"};
        float[] ratios = {1f / 3, 1f / 3, 1f / 3};
        boolean[] center = {true, true, false};
        List<Version> versions = versions(sop);
        List<String[]> rows;
        if (versions.isEmpty()) {
            rows = Collections.singletonList(
                    new String[] {"", value(sop.getVersionCourante(), ""), "Draft version"});
        } else {
            rows = versions.stream().map(v -> new String[] {
                    v.getDateEntree() == null ? "" : DATE_FORMAT.format(v.getDateEntree()),
                    value(v.getNumeroVersion(), ""),
                    value(v.getLibelleModification(), "Draft version")
            }).collect(Collectors.toList());
        }
        drawTable(ctx, headers, ratios, false, center, rows);
    }

    private void drawDestinataires(PageContext ctx, Sop sop) throws IOException {
        String[] headers = {"Nom", "Fonction", "Organisation", "Action"};
        float[] ratios = {220f / 704, 170f / 704, 155f / 704, 159f / 704}; // total = 704
        boolean[] center = {false, false, true, false};
        String organisation = staticTextOr(TXT_ORGANISATION, ORGANISATION_DEFAUT);
        Map<String, Utilisateur> utilisateurs = utilisateurRepository.findAll().stream()
                .filter(u -> notBlank(u.getMatricule()))
                .collect(Collectors.toMap(u -> u.getMatricule().trim(), u -> u,
                        (first, second) -> first));
        List<String[]> rows = destinataires(sop).stream().map(d -> new String[] {
                value(d.getNom(), d.getMatricule(), ""),
                fonctionDestinataire(d, utilisateurs),
                organisation,
                value(d.getAction(), "")
        }).collect(Collectors.toList());
        drawTable(ctx, headers, ratios, true, center, rows);
    }

    private String fonctionDestinataire(Destinataire destinataire, Map<String, Utilisateur> utilisateurs) {
        if (!notBlank(destinataire.getMatricule())) return "";
        Utilisateur utilisateur = utilisateurs.get(destinataire.getMatricule().trim());
        return utilisateur == null ? "" : value(utilisateur.getLibelleFonction(), "");
    }

    private void drawEquipe(PageContext ctx, Sop sop) throws IOException {
        String[] headers = {"Nom", "Fonction & Responsabilité dans le projet", "Numéro de téléphone", "e-mail"};
        // total = 704 (la colonne e-mail est élargie pour les longues adresses)
        float[] ratios = {190f / 704, 150f / 704, 125f / 704, 239f / 704};
        List<String[]> rows = equipe(sop).stream().map(e -> new String[] {
                value(e.getNom(), e.getMatricule(), ""), value(e.getFonction(), ""),
                value(e.getTelephone(), ""), value(e.getEmail(), "")
        }).collect(Collectors.toList());
        drawTable(ctx, headers, ratios, true, null, rows);
    }

    private void drawGlossaire(PageContext ctx, Sop sop) throws IOException {
        List<String[]> rows = glossaire(sop).stream().map(g -> new String[] {
                value(g.getAcronyme(), ""), value(g.getTerme(), "")
        }).collect(Collectors.toList());
        drawTable(ctx, new String[] {"Acronyme", "Termes"}, new float[] {0.27f, 0.73f}, false, null, rows);
    }

    // ================= RÉFÉRENCES / FLOWCHART / ÉTAPES / ANNEXES =================
    private void drawReferences(PageContext ctx, Sop sop) throws IOException {
        for (Reference reference : references(sop)) {
            for (String[] p : parseParagraphs(reference.getContenu())) {
                int level = Integer.parseInt(p[0]);
                if (level == 0) {
                    drawRich(ctx, Collections.singletonList(Span.plain(p[1])), MARGIN_LEFT, CONTENT_WIDTH, true,
                            null, 0, REF_PARAGRAPH_SPACING, REF_LINE_HEIGHT);
                } else {
                    float bx = MARGIN_LEFT + 14 * level;
                    float tx = bx + 14;
                    drawRich(ctx, Collections.singletonList(Span.plain(p[1])), tx, RIGHT_EDGE - tx, true,
                            level == 1 ? "\u2022" : "o", bx, 3f, REF_LINE_HEIGHT);
                }
            }
        }
    }

    /**
     * Section "Flowchart" : phrase d'intro, puis pour chaque flowchart
     *  - titre du groupe (ex. "Constatation des recettes", "Légendes") en titre niveau 2,
     *    affiché une seule fois par groupe ;
     *  - libellé du processus (ex. "Ordre de Virement ... [1h26mn]") en titre niveau 3 ;
     *  - l'image. Les deux titres alimentent la table des matières (niveaux 1 et 2).
     */
    private void drawFlowcharts(PageContext ctx, Sop sop) throws IOException {
        boolean first = true;
        String groupTitle = "";
        for (Flowchart flowchart : flowcharts(sop)) {
            String titre = value(flowchart.getTitre(), "");
            String info = value(flowchart.getInformation(), "");
            if (!first) ctx.newPage(); // une page par flowchart, comme la référence
            if (first) drawBodyText(ctx, staticText(TXT_PROCEDURE));

            if (notBlank(titre) && !titre.equalsIgnoreCase(groupTitle)) {
                groupTitle = titre;
                drawHeading(ctx, titre, 1, H2_AFTER, true);
            }
            if (notBlank(info)) drawHeading(ctx, info, 2, H3_AFTER, true);
            drawAttachmentImage(ctx, flowchart.getIdPieceJointe());
            first = false;
        }
    }

    /**
     * Section "Etapes" (même structure que la référence) :
     *   titre du groupe (niveau 2) → libellé du processus (niveau 3) →
     *   "1. Titre du groupe" (gras, une fois par groupe) → "1.k. Processus" (gras) → "1.k.j. étapes".
     * Les flowcharts sans étapes (ex. "Légendes") sont ignorés ici.
     */
    private void drawEtapes(PageContext ctx, Sop sop) throws IOException {
        int group = 0;
        int process = 0;
        String groupTitle = "";
        int[] counters = new int[8];

        for (Flowchart flowchart : flowcharts(sop)) {
            List<FlowchartEtape> steps = etapes(flowchart.getIdFlowchart());
            if (steps.isEmpty()) continue;
            String titre = value(flowchart.getTitre(), "");
            String info = value(flowchart.getInformation(), "");

            boolean newGroup = group == 0 || (notBlank(titre) && !titre.equalsIgnoreCase(groupTitle));
            if (newGroup) {
                group++;
                process = 0;
                groupTitle = titre;
                if (notBlank(titre)) drawHeading(ctx, titre, 1, H2_AFTER, true);
            }
            process++;
            if (notBlank(info)) drawHeading(ctx, info, 2, H3_AFTER, true);
            if (newGroup && notBlank(titre)) drawStep(ctx, group + ".", titre, 0, true);

            Arrays.fill(counters, 0);
            counters[0] = process;

            // si les données n'ont pas d'étape de niveau 1, on écrit "1.k. Processus" nous-mêmes
            boolean startsAtLevel1 = value(steps.get(0).getNiveau(), 1) <= 1;
            if (!startsAtLevel1 && notBlank(info)) {
                drawStep(ctx, group + "." + process + ".", info, 1, true);
            }

            for (FlowchartEtape step : steps) {
                int level = Math.max(1, Math.min(8, value(step.getNiveau(), 1)));
                if (level == 1) {
                    Arrays.fill(counters, 1, counters.length, 0);
                } else {
                    counters[level - 1]++;
                    for (int i = level; i < counters.length; i++) counters[i] = 0;
                    for (int i = 1; i < level - 1; i++) if (counters[i] == 0) counters[i] = 1;
                }
                String number = group + "." + Arrays.stream(counters, 0, level)
                        .mapToObj(String::valueOf).collect(Collectors.joining(".")) + ".";
                drawStep(ctx, number, value(step.getInformation(), ""), level, level == 1);
            }
            ctx.y -= 6;
        }
    }

    private void drawStep(PageContext ctx, String number, String text, int level, boolean bold) throws IOException {
        float nx = STEP_NUM_X[level];
        float tx = STEP_TEXT_X[level];
        Span span = bold ? Span.bold(text) : Span.plain(text);
        drawRich(ctx, Collections.singletonList(span), tx, RIGHT_EDGE - tx, true, number, nx, STEP_SPACING);
    }

    private void drawAnnexes(PageContext ctx, Sop sop) throws IOException {
        for (Annexe annexe : annexes(sop)) drawAttachmentImage(ctx, annexe.getIdPieceJointe());
    }

    /** Image à la largeur du contenu, réduite pour tenir dans la page (jamais coupée). */
    private void drawAttachmentImage(PageContext ctx, String id) throws IOException {
        if (!notBlank(id)) return;
        PieceJointe attachment = pieceJointeRepository.findById(id).orElse(null);
        if (attachment == null || attachment.getContenu() == null || !isImage(attachment.getTypeMime())) return;
        PDImageXObject image = PDImageXObject.createFromByteArray(ctx.document, attachment.getContenu(),
                value(attachment.getNomFichier(), "image"));

        if (ctx.y - CONTENT_BOTTOM < 150) ctx.newPage();
        float avail = ctx.y - CONTENT_BOTTOM;
        float scale = Math.min(CONTENT_WIDTH / image.getWidth(), avail / image.getHeight());
        scale = Math.min(scale, 1.5f);
        float w = image.getWidth() * scale;
        float h = image.getHeight() * scale;
        ctx.content.drawImage(image, MARGIN_LEFT, ctx.y - h, w, h);
        ctx.y -= h + 8;
    }

    // ================= TABLEAUX =================
    private void drawTable(PageContext ctx, String[] headers, float[] ratios, boolean greyHeader,
            boolean[] centerBody, List<String[]> rows) throws IOException {
        // Normalisation : la somme des ratios est ramenée à 1 pour que le tableau
        // occupe toujours exactement CONTENT_WIDTH (même largeur pour tous les tableaux).
        float sum = 0;
        for (float r : ratios) sum += r;
        float[] cols = new float[ratios.length];
        for (int i = 0; i < cols.length; i++) cols[i] = ratios[i] / sum * CONTENT_WIDTH;

        ctx.checkPageBreak(60);
        drawTableRow(ctx, headers, cols, true, greyHeader, null);
        for (String[] row : rows) {
            if (ctx.y - rowHeight(row, cols) < CONTENT_BOTTOM) {
                ctx.newPage();
                drawTableRow(ctx, headers, cols, true, greyHeader, null); // en-tête répété
            }
            drawTableRow(ctx, row, cols, false, false, centerBody);
        }
        ctx.y -= TABLE_SPACE_AFTER;
    }

    private List<List<String>> cellLines(String[] values, float[] cols) throws IOException {
        List<List<String>> result = new ArrayList<>();
        for (int i = 0; i < cols.length; i++) {
            String v = i < values.length ? values[i] : "";
            result.add(wrapText(pdfText(v == null ? "" : v), F_REG, SIZE_TABLE, cols[i] - 2 * CELL_PAD));
        }
        return result;
    }

    private float rowHeight(String[] values, float[] cols) throws IOException {
        int maxLines = 1;
        for (List<String> l : cellLines(values, cols)) maxLines = Math.max(maxLines, l.size());
        return maxLines * TABLE_LINE_HEIGHT + TABLE_ROW_PADDING * 2;
    }

    private void drawTableRow(PageContext ctx, String[] values, float[] cols, boolean header, boolean grey,
            boolean[] centerBody) throws IOException {
        PDPageContentStream c = ctx.content;
        List<List<String>> lines = cellLines(values, cols);
        float h = rowHeight(values, cols);
        float total = 0;
        for (float w : cols) total += w;

        if (header && grey) {
            c.setNonStrokingColor(GREY_HEADER, GREY_HEADER, GREY_HEADER);
            c.addRect(MARGIN_LEFT, ctx.y - h, total, h);
            c.fill();
        }
        c.setStrokingColor(0f, 0f, 0f);
        c.setLineWidth(.5f);
        float x = MARGIN_LEFT;
        for (float w : cols) {
            c.addRect(x, ctx.y - h, w, h);
            x += w;
        }
        c.stroke();

        x = MARGIN_LEFT;
        for (int i = 0; i < cols.length; i++) {
            boolean center = header || (centerBody != null && i < centerBody.length && centerBody[i]);
            // centrage vertical du bloc de texte dans la cellule
            float textBlockH = lines.get(i).size() * TABLE_LINE_HEIGHT;
            float baseline = ctx.y - (h - textBlockH) / 2 - SIZE_TABLE * 0.9f;
            for (String line : lines.get(i)) {
                float tw = F_REG.getStringWidth(line) / 1000 * SIZE_TABLE;
                float tx = center ? x + (cols[i] - tw) / 2 : x + CELL_PAD;
                boolean mail = !header && line.contains("@");
                drawStyled(c, line, F_REG, SIZE_TABLE, tx, baseline, mail ? COLOR_LIEN : COLOR_NOIR, mail);
                baseline -= TABLE_LINE_HEIGHT;
            }
            x += cols[i];
        }
        ctx.y -= h;
    }

    // ================= TABLE DES MATIÈRES =================
    /**
     * Remplit les pages réservées ; retourne le nombre de pages réellement nécessaires.
     * Les entrées viennent de drawHeading(..., addToToc = true) : niveau 0 = sections,
     * niveau 1 = groupes de flowchart (ex. "Constatation des recettes", "Légendes"),
     * niveau 2 = processus. Chaque niveau est décalé de TOC_INDENT.
     */
    private int drawToc(PDDocument doc, List<PDPage> reserved, List<TocEntry> entries, int totalPages)
            throws IOException {
        int idx = 0;
        PDPageContentStream cs = openAppend(doc, reserved.get(0));
        float y = CONTENT_TOP;
        drawText(cs, staticText(TXT_TABLE_MATIERES), F_BOLD, SIZE_SECTION, MARGIN_LEFT, y, COLOR_TITRE);
        y -= SECTION_SPACE_AFTER + 4;
        float dotW = F_REG.getStringWidth(".") / 1000 * SIZE_TOC;

        for (TocEntry e : entries) {
            float x = MARGIN_LEFT + TOC_INDENT * e.level;
            String num = String.valueOf(e.page);
            float numW = F_REG.getStringWidth(num) / 1000 * SIZE_TOC;
            List<String> lines = wrapText(pdfText(e.title), F_REG, SIZE_TOC, RIGHT_EDGE - x - 28);
            if (lines.isEmpty()) continue;
            float h = lines.size() * TOC_LINE_HEIGHT + TOC_SPACE_AFTER;

            if (y - h < CONTENT_BOTTOM) {
                if (cs != null) cs.close();
                idx++;
                cs = idx < reserved.size() ? openAppend(doc, reserved.get(idx)) : null;
                y = CONTENT_TOP;
            }
            if (cs != null) {
                float ly = y;
                for (String line : lines) {
                    drawText(cs, line, F_REG, SIZE_TOC, x, ly);
                    ly -= TOC_LINE_HEIGHT;
                }
                float lastY = ly + TOC_LINE_HEIGHT;
                float endX = x + F_REG.getStringWidth(lines.get(lines.size() - 1)) / 1000 * SIZE_TOC + 3;
                int dots = (int) ((RIGHT_EDGE - numW - 4 - endX) / dotW);
                if (dots > 0) drawText(cs, ".".repeat(dots), F_REG, SIZE_TOC, endX, lastY);
                drawText(cs, num, F_REG, SIZE_TOC, RIGHT_EDGE - numW, lastY);
            }
            y -= h;
        }
        if (cs != null) cs.close();

        // "Ce document contient vingt (20) pages" en bas de la dernière page du sommaire
        PDPageContentStream last = openAppend(doc, reserved.get(reserved.size() - 1));
        drawText(last, "Ce document contient " + frenchNumber(totalPages) + " (" + totalPages + ") pages",
                F_REG, SIZE_BODY, MARGIN_LEFT, CONTENT_BOTTOM + 10);
        last.close();
        return idx + 1;
    }

    private static PDPageContentStream openAppend(PDDocument doc, PDPage page) throws IOException {
        return new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
    }

    /** Nombre en lettres (0 à 99) : 20 -> "vingt", 21 -> "vingt et un", 80 -> "quatre-vingts". */
    private static String frenchNumber(int n) {
        if (n < 20) return UNITES[Math.max(n, 0)];
        if (n < 70) {
            int d = n / 10;
            int u = n % 10;
            return DIZAINES[d] + (u == 0 ? "" : u == 1 ? " et un" : "-" + UNITES[u]);
        }
        if (n < 80) return n == 71 ? "soixante et onze" : "soixante-" + UNITES[n - 60];
        if (n < 100) return n == 80 ? "quatre-vingts" : "quatre-vingt-" + UNITES[n - 80];
        return String.valueOf(n);
    }

    // ================= TITRES / TEXTE =================
    /** level 0 = section (bleu 14 gras) ; 1 = groupe (bleu 13 gras) ; 2 = processus (bleu foncé 11 gras). */
    private void drawHeading(PageContext ctx, String text, int level, float after) throws IOException {
        drawHeading(ctx, text, level, after, true);
    }

    private void drawHeading(PageContext ctx, String text, int level, float after, boolean addToToc)
            throws IOException {
        float size = level == 0 ? SIZE_SECTION : level == 1 ? SIZE_H2 : SIZE_SUB;
        float[] color = level >= 2 ? COLOR_SOUS_TITRE : COLOR_TITRE;
        float before = level == 0 ? SECTION_SPACE_BEFORE : 6f;
        List<String> lines = wrapText(pdfText(text), F_BOLD, size, CONTENT_WIDTH);
        if (lines.isEmpty()) return;
        ctx.checkPageBreak(before + lines.size() * LINE_HEIGHT + after);
        if (ctx.y < CONTENT_TOP) ctx.y -= before;
        for (String line : lines) {
            drawText(ctx.content, line, F_BOLD, size, MARGIN_LEFT, ctx.y, color);
            ctx.y -= LINE_HEIGHT;
        }
        if (addToToc && notBlank(text)) ctx.toc.add(new TocEntry(text, level, ctx.currentPage));
        ctx.y += LINE_HEIGHT - after;
    }

    private void drawBodyText(PageContext ctx, String text) throws IOException {
        drawRich(ctx, Collections.singletonList(Span.plain(text)), MARGIN_LEFT, CONTENT_WIDTH, true,
                null, 0, PARAGRAPH_SPACING);
    }

    private void drawRich(PageContext ctx, List<Span> spans, float x, float width, boolean justify,
            String label, float labelX, float after) throws IOException {
        drawRich(ctx, spans, x, width, justify, label, labelX, after, LINE_HEIGHT);
    }

    /** Paragraphe multi-styles (gras, surlignage, lien), justifié, avec étiquette optionnelle (puce / numéro). */
    private void drawRich(PageContext ctx, List<Span> spans, float x, float width, boolean justify,
            String label, float labelX, float after, float lineHeight) throws IOException {
        List<Word> words = new ArrayList<>();
        for (Span s : spans) {
            for (String w : pdfText(s.text).trim().split("\\s+")) {
                if (w.isEmpty()) continue;
                String remaining = w;
                boolean continuation = false;
                while (!remaining.isEmpty()) {
                    int cut = remaining.length();
                    while (cut > 1
                            && s.font.getStringWidth(remaining.substring(0, cut)) / 1000 * SIZE_BODY > width) {
                        cut--;
                    }
                    String part = remaining.substring(0, cut);
                    words.add(new Word(part, s, s.font.getStringWidth(part) / 1000 * SIZE_BODY, continuation));
                    remaining = remaining.substring(cut);
                    continuation = true;
                }
            }
        }
        if (words.isEmpty()) {
            ctx.y -= lineHeight;
            return;
        }
        float space = F_REG.getStringWidth(" ") / 1000 * SIZE_BODY;

        List<List<Word>> lines = new ArrayList<>();
        List<Word> cur = new ArrayList<>();
        float curW = 0;
        for (Word w : words) {
            float add = cur.isEmpty() || w.noSpaceBefore ? w.width : space + w.width;
            if (!cur.isEmpty() && curW + add > width) {
                lines.add(cur);
                cur = new ArrayList<>();
                curW = 0;
                add = w.width;
            }
            cur.add(w);
            curW += add;
        }
        lines.add(cur);

        for (int i = 0; i < lines.size(); i++) {
            ctx.checkPageBreak(lineHeight);
            List<Word> line = lines.get(i);
            if (i == 0 && label != null) {
                drawStyled(ctx.content, label, line.get(0).style.font, SIZE_BODY, labelX, ctx.y, COLOR_NOIR, false);
            }
            float wordsW = 0;
            for (Word w : line) wordsW += w.width;
            int gaps = 0;
            for (int j = 1; j < line.size(); j++) {
                if (!line.get(j).noSpaceBefore) gaps++;
            }
            float gap = space;
            if (justify && i < lines.size() - 1 && gaps > 0) {
                float g = (width - wordsW) / gaps;
                if (g >= space && g < space * 3) gap = g;
            }
            float cx = x;
            for (int k = 0; k < line.size(); k++) {
                Word w = line.get(k);
                boolean hasSpaceBefore = k > 0 && !w.noSpaceBefore;
                if (hasSpaceBefore) cx += gap;
                boolean joinNext = k < line.size() - 1 && !line.get(k + 1).noSpaceBefore
                        && w.style.highlight && line.get(k + 1).style.highlight;
                if (w.style.highlight) {
                    ctx.content.setNonStrokingColor(1f, 1f, 0f);
                    ctx.content.addRect(cx, ctx.y - 2.2f, w.width + (joinNext ? gap : 0), SIZE_BODY + 1.5f);
                    ctx.content.fill();
                }
                drawStyled(ctx.content, w.text, w.style.font, SIZE_BODY, cx, ctx.y, w.style.color, w.style.underline);
                cx += w.width;
            }
            ctx.y -= lineHeight;
        }
        ctx.y -= after;
    }

    /** Découpe un texte en paragraphes / puces : {niveau, texte}. */
    private static List<String[]> parseParagraphs(String raw) {
        String s = value(raw, "").replace("\r", "");
        StringBuilder sb = new StringBuilder();
        for (char ch : s.toCharArray()) {
            if (isBullet(ch)) sb.append('\n').append(ch).append(' ');
            else sb.append(ch);
        }
        List<String[]> out = new ArrayList<>();
        for (String line : sb.toString().split("\n")) {
            String t = line.trim();
            if (t.isEmpty()) continue;
            char f = t.charAt(0);
            if (isBullet(f)) {
                String level = BULLET_L1.indexOf(f) >= 0 ? "1" : "2";
                String body = t.substring(1).trim();
                if (!body.isEmpty()) out.add(new String[] {level, body});
            } else {
                out.add(new String[] {"0", t});
            }
        }
        return out;
    }

    private static boolean isBullet(char ch) {
        return BULLET_L1.indexOf(ch) >= 0 || BULLET_L2.indexOf(ch) >= 0;
    }

    // ================= EN-TÊTE =================
    private void drawHeader(PDPageContentStream c, PDImageXObject logo, Sop sop, int totalPages, int currentPage)
            throws IOException {
        final float left = 56, right = 542, top = 806, bottom = 743;
        final float col1 = 193, col2 = 391, row1 = 778.5f, row2 = 763.5f;

        c.drawImage(logo, left + 1, bottom + 1, col1 - left - 2, top - bottom - 2);
        c.setStrokingColor(0f, 0f, 0f);
        c.setLineWidth(.75f);
        c.addRect(left, bottom, right - left, top - bottom);
        c.moveTo(col1, top);  c.lineTo(col1, bottom);
        c.moveTo(col2, top);  c.lineTo(col2, bottom);
        c.moveTo(col2, row1); c.lineTo(right, row1);
        c.moveTo(col2, row2); c.lineTo(right, row2);
        c.stroke();

        float midX = (col1 + col2) / 2;
        drawCenteredFit(c, fix(sop.getTitre()), F_REG, SIZE_HEADER, midX, 792, col2 - col1 - 8);
        drawCentered(c, staticText(TXT_SOP), F_REG, SIZE_HEADER, midX, 778);
        drawCentered(c, staticText(TXT_MANUEL), F_REG, SIZE_HEADER, midX, 764);
        drawCentered(c, staticText(TXT_SLA), F_REG, SIZE_HEADER, midX, 750);

        float rightX = (col2 + right) / 2;
        drawCenteredFit(c, fix(sop.getNumeroSop()), F_REG, 10f, rightX, 788, right - col2 - 8);
        drawCentered(c, "Version " + value(sop.getVersionCourante(), ""), F_REG, SIZE_HEADER, rightX, 768);

        String[] parts = {"Page ", String.valueOf(currentPage), " of ", String.valueOf(totalPages)};
        PDType1Font[] fonts = {F_REG, F_BOLD, F_REG, F_BOLD};
        float w = 0;
        for (int i = 0; i < parts.length; i++) w += fonts[i].getStringWidth(parts[i]) / 1000 * SIZE_HEADER;
        c.beginText();
        c.setNonStrokingColor(0f, 0f, 0f);
        c.newLineAtOffset(rightX - w / 2, 750);
        for (int i = 0; i < parts.length; i++) {
            c.setFont(fonts[i], SIZE_HEADER);
            c.showText(pdfText(parts[i]));
        }
        c.endText();
    }

    // ================= PRIMITIVES DE DESSIN =================
    private static void drawCentered(PDPageContentStream c, String text, PDType1Font font,
            float size, float centerX, float y) throws IOException {
        String t = pdfText(text);
        float w = font.getStringWidth(t) / 1000 * size;
        drawStyled(c, t, font, size, centerX - w / 2, y, COLOR_NOIR, false);
    }

    private static void drawCenteredFit(PDPageContentStream c, String text, PDType1Font font,
            float size, float centerX, float y, float maxWidth) throws IOException {
        String t = pdfText(text);
        float textWidth = font.getStringWidth(t) / 1000 * size;
        float fittedSize = textWidth > maxWidth ? size * maxWidth / textWidth : size;
        drawCentered(c, t, font, fittedSize, centerX, y);
    }

    private static void drawText(PDPageContentStream c, String text, PDType1Font font, float size,
            float x, float y) throws IOException {
        drawStyled(c, text, font, size, x, y, COLOR_NOIR, false);
    }

    private static void drawText(PDPageContentStream c, String text, PDType1Font font, float size,
            float x, float y, float[] color) throws IOException {
        drawStyled(c, text, font, size, x, y, color, false);
    }

    private static void drawStyled(PDPageContentStream c, String text, PDType1Font font, float size,
            float x, float y, float[] color, boolean underline) throws IOException {
        String t = pdfText(text);
        c.beginText();
        c.setFont(font, size);
        c.setNonStrokingColor(color[0], color[1], color[2]);
        c.newLineAtOffset(x, y);
        c.showText(t);
        c.endText();
        if (underline) {
            float w = font.getStringWidth(t) / 1000 * size;
            c.setStrokingColor(color[0], color[1], color[2]);
            c.setLineWidth(.5f);
            c.moveTo(x, y - 1.5f);
            c.lineTo(x + w, y - 1.5f);
            c.stroke();
        }
    }

    private static PDImageXObject loadHeaderLogo(PDDocument document) throws IOException {
        try (InputStream stream = PdfExportService.class.getResourceAsStream("/static/logo_entete.jpeg")) {
            if (stream == null) throw new IllegalStateException("Ressource du logo d'en-tête introuvable");
            return PDImageXObject.createFromByteArray(document, stream.readAllBytes(), "logo_entete.jpeg");
        }
    }

    // ================= DONNÉES =================
    private String staticText(String code) {
        String content = textStatiqueService.findById(code).getContenu();
        return content == null ? "" : fix(content.replaceAll("\\s+", " ").trim());
    }

    /** Texte statique optionnel : si le code n'existe pas en base, on utilise la valeur par défaut. */
    private String staticTextOr(String code, String fallback) {
        try {
            String text = staticText(code);
            return notBlank(text) ? text : fallback;
        } catch (RuntimeException ex) {
            return fallback;
        }
    }

    private List<Contact> contacts(Sop sop) {
        return contactRepository.findAll().stream().filter(c -> sop.getIdSop().equals(c.getIdSop())).collect(Collectors.toList());
    }
    private List<Version> versions(Sop sop) {
        return versionRepository.findAll().stream().filter(v -> sop.getIdSop().equals(v.getIdSop())).collect(Collectors.toList());
    }
    private List<Destinataire> destinataires(Sop sop) {
        return destinataireRepository.findAll().stream().filter(d -> sop.getIdSop().equals(d.getIdSop())).collect(Collectors.toList());
    }

    private List<EquipeProjet> equipe(Sop sop) {
        return equipeRepository.findAll().stream().filter(e -> sop.getIdSop().equals(e.getIdSop())).collect(Collectors.toList());
    }
    private List<Glossaire> glossaire(Sop sop) {
        return glossaireRepository.findAll().stream().filter(g -> sop.getIdSop().equals(g.getIdSop())).collect(Collectors.toList());
    }
    private List<Reference> references(Sop sop) {
        return referenceRepository.findAll().stream().filter(r -> sop.getIdSop().equals(r.getIdSop())).collect(Collectors.toList());
    }
    private List<Flowchart> flowcharts(Sop sop) {
        return flowchartRepository.findAll().stream().filter(f -> sop.getIdSop().equals(f.getIdSop())).collect(Collectors.toList());
    }
    private List<FlowchartEtape> etapes(String idFlowchart) {
        return etapeRepository.findAll().stream().filter(e -> idFlowchart.equals(e.getIdFlowchart())).collect(Collectors.toList());
    }
    private List<Annexe> annexes(Sop sop) {
        return annexeRepository.findAll().stream().filter(a -> sop.getIdSop().equals(a.getIdSop())).collect(Collectors.toList());
    }

    private String serviceLabel(Sop sop) {
        if (notBlank(sop.getLibelleService())) return fix(sop.getLibelleService());
        if (notBlank(sop.getCodeService())) {
            return serviceDrhRepository.findById(sop.getCodeService())
                    .map(service -> value(service.getLibelleService(), sop.getCodeService()))
                    .orElse(sop.getCodeService());
        }
        return "";
    }

    // ================= UTILITAIRES =================
    private static String value(String value, String fallback) {
        return notBlank(value) ? fix(value) : fallback;
    }
    private static String value(String value, String fallback, String emptyFallback) {
        return notBlank(value) ? fix(value) : (notBlank(fallback) ? fix(fallback) : emptyFallback);
    }
    private static int value(Integer value, int fallback) {
        return value == null ? fallback : value;
    }
    private static boolean notBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
    private static boolean isImage(String mime) {
        return mime != null && mime.toLowerCase(Locale.ROOT).startsWith("image/");
    }

    /**
     * Rustine : répare le mojibake CP850 lu en windows-1252 (é -> ‚, è -> Š, à -> …).
     * Ne s'applique que si la chaîne contient ces marqueurs et aucun accent correct.
     * À supprimer une fois les textes ré-insérés en UTF-8.
     */
    private static String fix(String s) {
        if (s == null) return "";
        boolean suspect = false;
        boolean proper = false;
        for (char ch : s.toCharArray()) {
            if ("\u201A\u0192\u201E\u2026\u2020\u2021\u02C6\u2030\u0160\u2039\u0152".indexOf(ch) >= 0) suspect = true;
            if (ch >= 0xC0 && ch <= 0xFF) proper = true;
        }
        if (!suspect || proper) return s;
        return new String(s.getBytes(WIN_1252), CP850);
    }

    /** Encodage windows-1252 (é, è, à, ’, œ, •…) ; le reste devient '?'. */
    private static String pdfText(String value) {
        if (value == null) return "";
        String v = value.replaceAll("[\\r\\n\\t]+", " ")
                .replace('\u25E6', '-').replace('\u25CB', '-').replace('\uF0A7', '-')
                .replace('\uF0B7', '\u2022').replace('\u25AA', '\u2022').replace('\u2023', '\u2022');
        return new String(v.getBytes(WIN_1252), WIN_1252);
    }

    /**
     * Découpe un texte en lignes de largeur maxWidth.
     * Un mot plus large que la cellule (ex. une longue adresse e-mail) est coupé
     * caractère par caractère pour ne jamais dépasser de la bordure.
     */
    private static List<String> wrapText(String text, PDType1Font font, float fontSize, float maxWidth)
            throws IOException {
        List<String> lines = new ArrayList<>();
        String[] words = text.trim().split("\\s+");
        StringBuilder current = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) continue;

            // mot trop long : on le découpe
            while (word.length() > 1 && font.getStringWidth(word) / 1000 * fontSize > maxWidth) {
                if (current.length() > 0) {
                    lines.add(current.toString());
                    current = new StringBuilder();
                }
                int cut = word.length() - 1;
                while (cut > 1 && font.getStringWidth(word.substring(0, cut)) / 1000 * fontSize > maxWidth) cut--;
                lines.add(word.substring(0, cut));
                word = word.substring(cut);
            }

            String candidate = current.length() == 0 ? word : current + " " + word;
            if (font.getStringWidth(candidate) / 1000 * fontSize > maxWidth && current.length() > 0) {
                lines.add(current.toString());
                current = new StringBuilder(word);
            } else {
                current = new StringBuilder(candidate);
            }
        }
        if (current.length() > 0) lines.add(current.toString());
        return lines;
    }
} 