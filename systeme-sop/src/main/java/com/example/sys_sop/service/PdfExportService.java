package com.example.sys_sop.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import com.example.sys_sop.modele.Sop;

@Service
public class PdfExportService {

    public byte[] generate(Sop sop) {
        try (PDDocument document = new PDDocument();
                ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDImageXObject logo = loadHeaderLogo(document);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                drawHeader(content, page, logo, sop);
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 18);
                content.newLineAtOffset(50, 570);
                content.showText(pdfText(sop.getTitre()));
                content.setFont(PDType1Font.HELVETICA, 11);
                content.newLineAtOffset(0, -35);
                content.showText(pdfText("Numero : " + sop.getNumeroSop()));
                content.newLineAtOffset(0, -18);
                content.showText(pdfText("Version : " + sop.getVersionCourante()));
                content.newLineAtOffset(0, -18);
                content.showText(pdfText("Service : " + sop.getCodeService()));
                content.newLineAtOffset(0, -18);
                content.showText(pdfText("Auteur : " + sop.getAuteurMatricule()));
                content.endText();
                drawFooter(content, page, 1, 1);
            }

            document.save(output);
            return output.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("Impossible de générer le PDF du SOP " + sop.getIdSop(), ex);
        }
    }

    private static PDImageXObject loadHeaderLogo(PDDocument document) throws IOException {
        try (InputStream logoStream = PdfExportService.class.getResourceAsStream("/static/logo_entete.jpeg")) {
            if (logoStream == null) {
                throw new IllegalStateException("Ressource du logo d'en-tête introuvable : /static/logo_entete.jpeg");
            }
            return PDImageXObject.createFromByteArray(document, logoStream.readAllBytes(), "logo_entete.jpeg");
        }
    }

    private static void drawHeader(PDPageContentStream content, PDPage page, PDImageXObject logo, Sop sop)
            throws IOException {
        float left = 40;
        float right = page.getMediaBox().getWidth() - 40;
        float top = page.getMediaBox().getHeight() - 35;
        float bottom = 660;
        float middleColumn = 205;
        float rightColumn = 485;

        content.drawImage(logo, left + 8, bottom + 10, 150, 83);

        content.moveTo(left, top);
        content.lineTo(right, top);
        content.lineTo(right, bottom);
        content.lineTo(left, bottom);
        content.lineTo(left, top);
        content.moveTo(middleColumn, top);
        content.lineTo(middleColumn, bottom);
        content.moveTo(rightColumn, top);
        content.lineTo(rightColumn, bottom);
        content.moveTo(rightColumn, 710);
        content.lineTo(right, 710);
        content.stroke();

        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 14);
        content.newLineAtOffset(middleColumn + 12, 755);
        content.showText(pdfText(sop.getTitre()));
        content.setFont(PDType1Font.HELVETICA, 10);
        content.newLineAtOffset(0, -20);
        content.showText(pdfText("Standard Operating Procedure (SOP) MANUAL"));
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 10);
        content.newLineAtOffset(rightColumn + 12, 750);
        content.showText(pdfText(sop.getNumeroSop()));
        content.setFont(PDType1Font.HELVETICA, 10);
        content.newLineAtOffset(0, -30);
        content.showText(pdfText("Version " + sop.getVersionCourante()));
        content.endText();
    }

    private static void drawFooter(PDPageContentStream content, PDPage page, int currentPage, int totalPages)
            throws IOException {
        content.beginText();
        content.setFont(PDType1Font.HELVETICA, 9);
        content.newLineAtOffset(page.getMediaBox().getWidth() - 105, 30);
        content.showText(pdfText("Page " + currentPage + " sur " + totalPages));
        content.endText();
    }

    private static String pdfText(String value) {
        if (value == null) {
            return "";
        }
        return new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.ISO_8859_1);
    }
}
