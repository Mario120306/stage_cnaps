package com.example.sys_sop.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ContentDisposition;
import org.springframework.web.bind.annotation.*;
import com.example.sys_sop.dto.ExportDto;
import com.example.sys_sop.modele.Export;
import com.example.sys_sop.service.ExportService;

@RestController
@RequestMapping("/api/exports")
public class ExportController {

    private final ExportService service;

    public ExportController(ExportService service) {
        this.service = service; 
    }
    @GetMapping 
    public List<ExportDto> getAll() { 
        return service.findAll()
                    .stream()
                    .map(this::toDto)
                    .collect(Collectors
                    .toList()); 
    }
    @GetMapping("/{id}")
    public ExportDto getById(@PathVariable String id) { 
        return toDto(service.findById(id)); 
    }
    @PostMapping
    public ResponseEntity<ExportDto> create(@RequestBody ExportDto dto) { 
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(toDto(service.create(toEntity(dto)))); 
    }
    @PutMapping("/{id}")
    public ResponseEntity<ExportDto> update(@PathVariable String id, @RequestBody ExportDto dto) {
        Export entity = toEntity(dto); entity.setIdExport(id);
        return service.update(entity) ? ResponseEntity.ok(toDto(entity)) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { 
        return service.deleteById(id) ? ResponseEntity
                    .noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/last/{idSop}/download")
    public ResponseEntity<byte[]> downloadLastExportPdf(@PathVariable String idSop) {
        // Find latest export for given SOP
        Export latest = service.findLatestBySop(idSop);
        if (latest == null || latest.getNomFichierPdf() == null || latest.getNomFichierPdf().trim().isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun export PDF enregistré pour ce SOP");
        }
        byte[] pdf = service.findPdf(latest.getIdExport());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename(latest.getNomFichierPdf()).build());
        headers.setContentLength(pdf.length);

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    private Export toEntity(ExportDto d) { 
        return new Export(d.getIdExport(), d.getIdSop(), d.getIdVersion(), 
                        d.getLibelleExport(), d.getNbPages(), d.getDateExport(), d.getNomFichierPdf(), d.getGenerePar()); 
    }
    private ExportDto toDto(Export e) { 
        return new ExportDto(e.getIdExport(), e.getIdSop(), e.getIdVersion(), 
        e.getLibelleExport(), e.getNbPages(), e.getDateExport(), e.getNomFichierPdf(), e.getGenerePar()); }
}
