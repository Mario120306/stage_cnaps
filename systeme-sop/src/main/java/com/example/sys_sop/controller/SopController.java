package com.example.sys_sop.controller;

import java.time.LocalDate;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.sys_sop.dto.SopCreationDto;
import com.example.sys_sop.dto.SopDto;
import com.example.sys_sop.modele.Sop;
import com.example.sys_sop.service.SopService;

@RestController
@RequestMapping("/api/sops")
public class SopController {
    private final SopService service;

    public SopController(SopService service) { 
        this.service = service; 
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String titre,
            @RequestParam(required = false, name = "direction") String codeDirection,
            @RequestParam(required = false, name = "service") String codeService,
            @RequestParam(required = false) String auteur,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo
    ) {
        Map<String, Object> pageResult = service.findPage(page, size, titre, codeDirection, codeService, auteur, dateFrom, dateTo);
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/{id}") 
    public SopDto getById(@PathVariable String id) { 
        return toDto(service.findById(id)); 
    }
    @PostMapping 
    public ResponseEntity<SopDto> create(@RequestBody SopCreationDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(toDto(service.create(dto)));
    }
    @PutMapping("/{id}") 
    public ResponseEntity<SopDto> update(@PathVariable String id, @RequestBody SopDto dto) {
        Sop entity = toEntity(dto); entity.setIdSop(id);
        return service.update(entity) ? ResponseEntity.ok(toDto(entity)) : ResponseEntity.notFound().build();
    }
    /*@DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable String id) {
        return service.deleteById(id) ? ResponseEntity
                .noContent()
                .build() : ResponseEntity
                .notFound()
                .build();
    }*/
    private Sop toEntity(SopDto d) { 
        return new Sop(d.getIdSop(), d.getNumeroSop(), 
                    d.getTitre(), d.getVersionCourante(), d.getCodeService(), 
                    d.getAuteurMatricule(),d.getSectionValidee(), d.getCodeStatut(),
                    d.getSupprime(),d.getDateSuppression(), d.getDateCreation(),d.getDateModification(),
                    d.getLibelleService(),d.getLibelleDirection()); 
    }
    private SopDto toDto(Sop e) { 
        return new SopDto(e.getIdSop(), e.getNumeroSop(),
                        e.getTitre(), e.getVersionCourante(), 
                        e.getCodeService(), e.getAuteurMatricule(),
                        e.getSectionValidee(),e.getCodeStatut(),e.getSupprime(),
                        e.getDateSuppression(), e.getDateCreation(),e.getDateModification(),
                        e.getLibelleService(),e.getLibelleDirection()); 
    }
}
