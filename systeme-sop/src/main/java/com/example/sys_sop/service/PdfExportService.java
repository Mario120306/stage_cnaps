package com.example.sys_sop.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    // ===== Polices / tailles =====
    private static final PDType1Font F_REG = PDType1Font.HELVETICA;
    private static final PDType1Font F_BOLD = PDType1Font.HELVETICA_BOLD;
    private static final float SIZE_BODY = 9f;
    private static final float SIZE_SECTION = 14f;
    private static final float SIZE_SUB = 11f;
    private static final float SIZE_TITRE_DOC = 11f;
    private static final float SIZE_HEADER = 9f;

    // ===== Couleurs =====
    private static final float[] COLOR_TITRE = {46 / 255f, 116 / 255f, 181 / 255f};     // #2E74B5
    private static final float[] COLOR_SOUS_TITRE = {31 / 255f, 78 / 255f, 121 / 255f}; // #1F4E79
    private static final float[] COLOR_LIEN = {5 / 255f, 99 / 255f, 193 / 255f};
    private static final float[] COLOR_NOIR = {0f, 0f, 0f};
    private static final float GREY_HEADER = 0.65f;

    // ===== Mise en page (mesurée sur la référence) =====
    private static final float PAGE_W = PDRectangle.A4.getWidth();
    private static final float MARGIN_LEFT = 71;
    private static final float MARGIN_RIGHT = 69;
    private static final float CONTENT_WIDTH = PAGE_W - MARGIN_LEFT - MARGIN_RIGHT;
    private static final float RIGHT_EDGE = PAGE_W - MARGIN_RIGHT;
    private static final float CONTENT_TOP = 719;
    private static final float CONTENT_BOTTOM = 80;
    private static final float LINE_HEIGHT = 10.4f;
    private static final float PARAGRAPH_SPACING = 5f;
    private static final float SECTION_SPACE_BEFORE = 11f;
    private static final float SECTION_SPACE_AFTER = 14f;
    private static final float HEADING_BEFORE_TABLE = 6f;
    private static final float TABLE_SPACE_AFTER = 36f;
    private static final float CELL_PAD = 4f;

    // Retraits des étapes (x du numéro / x du texte) selon la profondeur 1..9
    private static final float[] STEP_NUM_X = {71, 80, 129, 174, 224, 270, 310, 350, 390};
    private static final float[] STEP_TEXT_X = {97, 129, 174, 224, 270, 310, 350, 390, 430};

    private static final String BULLET_L1 = "\u2022\uF0B7\u2023";
    private static final String BULLET_L2 = "\u25E6\u25CB\u25AA\uF0A7";

    private static final Charset WIN_1252 = Charset.forName("windows-1252");
    private static final Charset CP850 = Charset.forName("IBM850");
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);

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

    public PdfExportService(TextStatiqueService textStatiqueService, VersionRepository versionRepository,
            ContactRepository contactRepository, DestinataireRepository destinataireRepository,
            EquipeProjetRepository equipeRepository, GlossaireRepository glossaireRepository,
            ReferenceRepository referenceRepository, FlowchartRepository flowchartRepository,
            FlowchartEtapeRepository etapeRepository, AnnexeRepository annexeRepository,
            PieceJointeRepository pieceJointeRepository, ServiceDrhRepository serviceDrhRepository) {
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

        Word(String text, Span style, float width) {
            this.text = text;
            this.style = style;
            this.width = width;
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

            int needed = drawToc(document, reserved, ctx.toc);

            int totalPages = document.getNumberOfPages();
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

        drawHeading(ctx, staticText(TXT_NOTE_PUB), 0, SECTION_SPACE_AFTER, false);
        String service = serviceLabel(sop);
        drawRich(ctx, Arrays.asList(Span.plain(staticText(TXT_GUIDE_PUB)), Span.highlight(service),
                Span.plain(staticText(TXT_CAISSE))), MARGIN_LEFT, CONTENT_WIDTH, true, null, 0, PARAGRAPH_SPACING);
        drawBodyText(ctx, staticText(TXT_CONFIDENTIEL));

        drawHeading(ctx, staticText(TXT_MARQUE), 0, SECTION_SPACE_AFTER, false);
        drawBodyText(ctx, staticText(TXT_MARQUES_RECONNUES));

        drawHeading(ctx, staticText(TXT_COMMENTAIRES), 0, SECTION_SPACE_AFTER, false);
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
        float[] ratios = {170f / 704, 166f / 704, 168f / 704, 200f / 704};
        boolean[] center = {false, false, true, false};
        List<String[]> rows = destinataires(sop).stream().map(d -> new String[] {
                value(d.getNom(), d.getMatricule(), ""),
                "",  // pas de fonction dans le modèle actuel
                value(d.getOrganisation(), ""),
                value(d.getAction(), "")
        }).collect(Collectors.toList());
        drawTable(ctx, headers, ratios, true, center, rows);
    }

    private void drawEquipe(PageContext ctx, Sop sop) throws IOException {
        String[] headers = {"Nom", "Fonction & Responsabilité dans le projet", "Numéro de téléphone", "e-mail"};
        float[] ratios = {149f / 704, 148f / 704, 148f / 704, 259f / 704};
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
                    drawBodyText(ctx, p[1]);
                } else {
                    float bx = MARGIN_LEFT + 14 * level;
                    float tx = bx + 14;
                    drawRich(ctx, Collections.singletonList(Span.plain(p[1])), tx, RIGHT_EDGE - tx, true,
                            level == 1 ? "\u2022" : "o", bx, 2f);
                }
            }
        }
    }

    private void drawFlowcharts(PageContext ctx, Sop sop) throws IOException {
        int i = 0;
        for (Flowchart flowchart : flowcharts(sop)) {
            if (i++ > 0) ctx.newPage(); // une page par flowchart, comme la référence
            drawSubheading(ctx, value(flowchart.getTitre(), ""));
            if (notBlank(flowchart.getInformation())) drawBodyText(ctx, value(flowchart.getInformation(), ""));
            drawAttachmentImage(ctx, flowchart.getIdPieceJointe());
        }
    }

    private void drawEtapes(PageContext ctx, Sop sop) throws IOException {
        int flowchartNumber = 0;
        for (Flowchart flowchart : flowcharts(sop)) {
            flowchartNumber++;
            String titre = value(flowchart.getTitre(), "");
            ctx.checkPageBreak(LINE_HEIGHT * 4);
            drawSubheading(ctx, titre);
            if (notBlank(flowchart.getInformation())) {
                drawBodyText(ctx, flowchart.getInformation());
            }

            int[] counters = new int[8];
            for (FlowchartEtape step : etapes(flowchart.getIdFlowchart())) {
                int level = Math.max(1, Math.min(8, value(step.getNiveau(), 1)));
                int index = level - 1;
                counters[index]++;
                for (int i = index + 1; i < counters.length; i++) counters[i] = 0;
                for (int i = 0; i < index; i++) if (counters[i] == 0) counters[i] = 1;
                String number = flowchartNumber + "." + Arrays.stream(counters, 0, level)
                        .mapToObj(String::valueOf).collect(Collectors.joining(".")) + ".";

                float nx = STEP_NUM_X[level];
                float tx = STEP_TEXT_X[level];
                drawRich(ctx, Collections.singletonList(Span.plain(value(step.getInformation(), ""))),
                        tx, RIGHT_EDGE - tx, true, number, nx, 1f);
            }
            ctx.y -= 6;
        }
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
        float[] cols = new float[ratios.length];
        for (int i = 0; i < cols.length; i++) cols[i] = ratios[i] * CONTENT_WIDTH;

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
            result.add(wrapText(pdfText(v == null ? "" : v), F_REG, SIZE_BODY, cols[i] - 2 * CELL_PAD));
        }
        return result;
    }

    private float rowHeight(String[] values, float[] cols) throws IOException {
        int maxLines = 1;
        for (List<String> l : cellLines(values, cols)) maxLines = Math.max(maxLines, l.size());
        return maxLines * LINE_HEIGHT + 3;
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
            float baseline = ctx.y - 1.5f - 8f;
            for (String line : lines.get(i)) {
                float tw = F_REG.getStringWidth(line) / 1000 * SIZE_BODY;
                float tx = center ? x + (cols[i] - tw) / 2 : x + CELL_PAD;
                boolean mail = !header && line.contains("@");
                drawStyled(c, line, F_REG, SIZE_BODY, tx, baseline, mail ? COLOR_LIEN : COLOR_NOIR, mail);
                baseline -= LINE_HEIGHT;
            }
            x += cols[i];
        }
        ctx.y -= h;
    }

    // ================= TABLE DES MATIÈRES =================
    /** Remplit les pages réservées ; retourne le nombre de pages réellement nécessaires. */
    private int drawToc(PDDocument doc, List<PDPage> reserved, List<TocEntry> entries) throws IOException {
        int idx = 0;
        PDPageContentStream cs = openAppend(doc, reserved.get(0));
        float y = CONTENT_TOP;
        drawText(cs, staticText(TXT_TABLE_MATIERES), F_BOLD, SIZE_SECTION, MARGIN_LEFT, y, COLOR_TITRE);
        y -= SECTION_SPACE_AFTER + 4;
        float dotW = F_REG.getStringWidth(".") / 1000 * SIZE_BODY;

        for (TocEntry e : entries) {
            float x = MARGIN_LEFT + 12 * e.level;
            String num = String.valueOf(e.page);
            float numW = F_REG.getStringWidth(num) / 1000 * SIZE_BODY;
            List<String> lines = wrapText(pdfText(e.title), F_REG, SIZE_BODY, RIGHT_EDGE - x - 28);
            if (lines.isEmpty()) continue;
            float h = lines.size() * LINE_HEIGHT + 4;

            if (y - h < CONTENT_BOTTOM) {
                if (cs != null) cs.close();
                idx++;
                cs = idx < reserved.size() ? openAppend(doc, reserved.get(idx)) : null;
                y = CONTENT_TOP;
            }
            if (cs != null) {
                float ly = y;
                for (String line : lines) {
                    drawText(cs, line, F_REG, SIZE_BODY, x, ly);
                    ly -= LINE_HEIGHT;
                }
                float lastY = ly + LINE_HEIGHT;
                float endX = x + F_REG.getStringWidth(lines.get(lines.size() - 1)) / 1000 * SIZE_BODY + 3;
                int dots = (int) ((RIGHT_EDGE - numW - 4 - endX) / dotW);
                if (dots > 0) drawText(cs, ".".repeat(dots), F_REG, SIZE_BODY, endX, lastY);
                drawText(cs, num, F_REG, SIZE_BODY, RIGHT_EDGE - numW, lastY);
            }
            y -= h;
        }
        if (cs != null) cs.close();
        return idx + 1;
    }

    private static PDPageContentStream openAppend(PDDocument doc, PDPage page) throws IOException {
        return new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
    }

    // ================= TITRES / TEXTE =================
    /** level 0 = titre de section (bleu 14 gras) ; level 1 = sous-titre (bleu foncé 11 gras). */
    private void drawHeading(PageContext ctx, String text, int level, float after) throws IOException {
        drawHeading(ctx, text, level, after, true);
    }

    private void drawHeading(PageContext ctx, String text, int level, float after, boolean addToToc)
            throws IOException {
        float size = level == 0 ? SIZE_SECTION : SIZE_SUB;
        ctx.checkPageBreak(SECTION_SPACE_BEFORE + size + after + 2 * LINE_HEIGHT);
        if (ctx.y < CONTENT_TOP) ctx.y -= (level == 0 ? SECTION_SPACE_BEFORE : 6f);
        drawText(ctx.content, text, F_BOLD, size, MARGIN_LEFT, ctx.y, level == 0 ? COLOR_TITRE : COLOR_SOUS_TITRE);
        if (addToToc) ctx.toc.add(new TocEntry(text, level, ctx.currentPage));
        ctx.y -= after;
    }

    private void drawSubheading(PageContext ctx, String text) throws IOException {
        ctx.checkPageBreak(SECTION_SPACE_BEFORE + SIZE_SUB + SECTION_SPACE_AFTER);
        if (ctx.y < CONTENT_TOP) ctx.y -= 6f;
        drawText(ctx.content, text, F_BOLD, SIZE_SUB, MARGIN_LEFT, ctx.y, COLOR_SOUS_TITRE);
        ctx.y -= 10f;
    }

    private void drawBodyText(PageContext ctx, String text) throws IOException {
        drawRich(ctx, Collections.singletonList(Span.plain(text)), MARGIN_LEFT, CONTENT_WIDTH, true,
                null, 0, PARAGRAPH_SPACING);
    }

    /** Paragraphe multi-styles (gras, surlignage, lien), justifié, avec étiquette optionnelle (puce / numéro). */
    private void drawRich(PageContext ctx, List<Span> spans, float x, float width, boolean justify,
            String label, float labelX, float after) throws IOException {
        List<Word> words = new ArrayList<>();
        for (Span s : spans) {
            for (String w : pdfText(s.text).trim().split("\\s+")) {
                if (!w.isEmpty()) words.add(new Word(w, s, s.font.getStringWidth(w) / 1000 * SIZE_BODY));
            }
        }
        if (words.isEmpty()) {
            ctx.y -= LINE_HEIGHT;
            return;
        }
        float space = F_REG.getStringWidth(" ") / 1000 * SIZE_BODY;

        List<List<Word>> lines = new ArrayList<>();
        List<Word> cur = new ArrayList<>();
        float curW = 0;
        for (Word w : words) {
            float add = cur.isEmpty() ? w.width : space + w.width;
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
            ctx.checkPageBreak(LINE_HEIGHT);
            List<Word> line = lines.get(i);
            if (i == 0 && label != null) {
                drawStyled(ctx.content, label, line.get(0).style.font, SIZE_BODY, labelX, ctx.y, COLOR_NOIR, false);
            }
            float wordsW = 0;
            for (Word w : line) wordsW += w.width;
            int gaps = line.size() - 1;
            float gap = space;
            if (justify && i < lines.size() - 1 && gaps > 0) {
                float g = (width - wordsW) / gaps;
                if (g >= space && g < space * 3) gap = g;
            }
            float cx = x;
            for (int k = 0; k < line.size(); k++) {
                Word w = line.get(k);
                boolean joinNext = k < line.size() - 1 && w.style.highlight && line.get(k + 1).style.highlight;
                if (w.style.highlight) {
                    ctx.content.setNonStrokingColor(1f, 1f, 0f);
                    ctx.content.addRect(cx, ctx.y - 2.2f, w.width + (joinNext ? gap : 0), SIZE_BODY + 1.5f);
                    ctx.content.fill();
                }
                drawStyled(ctx.content, w.text, w.style.font, SIZE_BODY, cx, ctx.y, w.style.color, w.style.underline);
                cx += w.width + gap;
            }
            ctx.y -= LINE_HEIGHT;
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
        drawCentered(c, fix(sop.getTitre()), F_REG, SIZE_HEADER, midX, 792);
        drawCentered(c, staticText(TXT_SOP), F_REG, SIZE_HEADER, midX, 778);
        drawCentered(c, staticText(TXT_MANUEL), F_REG, SIZE_HEADER, midX, 764);
        drawCentered(c, staticText(TXT_SLA), F_REG, SIZE_HEADER, midX, 750);

        float rightX = (col2 + right) / 2;
        drawCentered(c, fix(sop.getNumeroSop()), F_REG, 10f, rightX, 788);
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

    private static List<String> wrapText(String text, PDType1Font font, float fontSize, float maxWidth)
            throws IOException {
        List<String> lines = new ArrayList<>();
        String[] words = text.trim().split("\\s+");
        StringBuilder current = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) continue;
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