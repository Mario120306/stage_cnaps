package com.example.sys_sop.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.sys_sop.dto.AnnexeDto;
import com.example.sys_sop.modele.Annexe;
import com.example.sys_sop.service.AnnexeService;

@RestController
@RequestMapping("/api/annexes")
public class AnnexeController {

    private final AnnexeService service;

    public AnnexeController(AnnexeService service) { 
        this.service = service; 
    }
    @GetMapping 
    public List<AnnexeDto> getAll() {
        return service.findAll()
                    .stream()
                    .map(this::toDto)
                    .collect(Collectors
                    .toList()); 
    }
    @GetMapping("/{id}") 
    public AnnexeDto getById(@PathVariable String id) { 
        return toDto(service.findById(id)); 
    }
    @PostMapping 
    public ResponseEntity<AnnexeDto> create(@RequestBody AnnexeDto dto) { 
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(toDto(service
                            .create(toEntity(dto)))); 
    }
    @PutMapping("/{id}") 
    public ResponseEntity<AnnexeDto> update(@PathVariable String id, @RequestBody AnnexeDto dto) {
        Annexe entity = toEntity(dto); entity.setIdAnnexe(id);
        return service.update(entity) ? ResponseEntity.ok(toDto(entity)) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { 
        return service.deleteById(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build(); 
    }
    private Annexe toEntity(AnnexeDto d) { 
        return new Annexe(d.getIdAnnexe(), d.getIdSop(), d.getTitre(), d.getIdPieceJointe(), 
        d.getOrdre()); 
    }
    private AnnexeDto toDto(Annexe e) { 
        return new AnnexeDto(e.getIdAnnexe(), e.getIdSop(), e.getTitre(), e.getIdPieceJointe(),
         e.getOrdre()); 
    }
}
