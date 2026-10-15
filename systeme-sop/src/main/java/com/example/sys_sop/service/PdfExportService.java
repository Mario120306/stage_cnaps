package com.example.sys_sop.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import com.example.sys_sop.modele.Sop;

@Service
public class PdfExportService {

    public byte[] generate(Sop sop) {
        try (PDDocument document = new PDDocument();
                ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 18);
                content.newLineAtOffset(50, 770);
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
            }

            document.save(output);
            return output.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("Impossible de générer le PDF du SOP " + sop.getIdSop(), ex);
        }
    }

    private static String pdfText(String value) {
        if (value == null) {
            return "";
        }
        return new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.ISO_8859_1);
    }
}
