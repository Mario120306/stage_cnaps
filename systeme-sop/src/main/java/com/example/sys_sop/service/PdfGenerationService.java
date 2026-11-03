package com.example.sys_sop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.sys_sop.modele.Export;
import com.example.sys_sop.modele.Sop;
import com.example.sys_sop.repository.ExportRepository;

@Service
public class PdfGenerationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfGenerationService.class);

    private final PdfExportService pdfExportService;
    private final ExportRepository exportRepository;

    public PdfGenerationService(PdfExportService pdfExportService, ExportRepository exportRepository) {
        this.pdfExportService = pdfExportService;
        this.exportRepository = exportRepository;
    }

    @Async("pdfTaskExecutor")
    public void generateAndStore(Sop sop, String idVersion, String idExport, String fileName, String author,
            String label) {
        try {
            byte[] pdf = pdfExportService.generate(sop);
            exportRepository.create(new Export(idExport, sop.getIdSop(), idVersion, label, 1, null,
                    fileName, author), pdf);
        } catch (RuntimeException ex) {
            LOGGER.error("Échec de la génération PDF du SOP {}", sop.getIdSop(), ex);
        }
    }
}
