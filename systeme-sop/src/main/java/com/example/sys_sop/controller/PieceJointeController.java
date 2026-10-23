package com.example.sys_sop.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.sys_sop.dto.PieceJointeDto;
import com.example.sys_sop.modele.PieceJointe;
import com.example.sys_sop.service.PieceJointeService;

@RestController
@RequestMapping("/api/pieces-jointes")
public class PieceJointeController {

    private final PieceJointeService service;

    public PieceJointeController(PieceJointeService service) { 
        this.service = service; 
    }
    @GetMapping 
    public List<PieceJointeDto> getAll() { 
        return service.findAll()
                    .stream()
                    .map(this::toDto)
                    .collect(Collectors
                    .toList());
    }
    @GetMapping("/{id}")
    public PieceJointeDto getById(@PathVariable String id) { 
        return toDto(service.findMetadataById(id)); 
    }
    @GetMapping("/{id}/contenu")
    public ResponseEntity<byte[]> getContent(@PathVariable String id) {
        PieceJointe piece = service.findMetadataById(id);
        byte[] contenu = service.findContentById(id);
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (piece.getTypeMime() != null && !piece.getTypeMime().isBlank()) {
            try {
                mediaType = MediaType.parseMediaType(piece.getTypeMime());
            } catch (IllegalArgumentException ignored) {
                // Keep the safe binary default for an invalid stored MIME type.
            }
        }
        return ResponseEntity.ok()
                .contentType(mediaType)
                .cacheControl(CacheControl.maxAge(java.time.Duration.ofHours(1)).cachePublic())
                .body(contenu);
    }
    @PostMapping 
    public ResponseEntity<PieceJointeDto> create(@RequestBody PieceJointeDto dto) { 
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(toDto(service
                            .create(toEntity(dto)))); 
    }
    @PutMapping("/{id}")
    public ResponseEntity<PieceJointeDto> update(@PathVariable String id, @RequestBody PieceJointeDto dto) {
        PieceJointe entity = toEntity(dto); entity.setIdPieceJointe(id);
        return service.update(entity) ? ResponseEntity.ok(toDto(entity)) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> delete(@PathVariable String id) { 
        return service.deleteById(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build(); 
    }
    private PieceJointe toEntity(PieceJointeDto d) { 
        return new PieceJointe(d.getIdPieceJointe(), d.getIdSop(), d.getUsage(), d.getNomFichier(),
                             d.getTypeMime(), d.getTaille(), d.getContenu(), d.getAuteurMatricule(), d.getDateInsert()); 
    }
    private PieceJointeDto toDto(PieceJointe e) { 
        return new PieceJointeDto(e.getIdPieceJointe(), e.getIdSop(), e.getUsage(), e.getNomFichier(), 
        e.getTypeMime(), e.getTaille(), e.getContenu(), e.getAuteurMatricule(), e.getDateInsert()); 
    }
}
