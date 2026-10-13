package com.example.sys_sop.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.sys_sop.dto.EquipeProjetDto;
import com.example.sys_sop.modele.EquipeProjet;
import com.example.sys_sop.service.EquipeProjetService;

@RestController
@RequestMapping("/api/equipes-projet")
public class EquipeProjetController {

    private final EquipeProjetService service;

    public EquipeProjetController(EquipeProjetService service) { 
        this.service = service; 
    }
    @GetMapping 
    public List<EquipeProjetDto> getAll() { 
        return service.findAll().stream().map(this::toDto)
                      .collect(Collectors
                      .toList()); 
    }
    @GetMapping("/{id}")
    public EquipeProjetDto getById(@PathVariable String id) { 
        return toDto(service.findById(id)); 
    }
    @PostMapping
    public ResponseEntity<EquipeProjetDto> create(@RequestBody EquipeProjetDto dto) { 
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(toDto(service.create(toEntity(dto)))); 
    }
    @PutMapping("/{id}") 
    public ResponseEntity<EquipeProjetDto> update(@PathVariable String id, @RequestBody EquipeProjetDto dto) {
        EquipeProjet entity = toEntity(dto); entity.setIdMembre(id);
        return service.update(entity) ? ResponseEntity.ok(toDto(entity)) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { 
        return service.deleteById(id) ? ResponseEntity
                    .noContent().build() : ResponseEntity.notFound().build(); 
    }
    private EquipeProjet toEntity(EquipeProjetDto d) { 
        return new EquipeProjet(d.getIdMembre(), d.getIdSop(), d.getMatricule(), d.getNom(), d.getFonction(),
                                d.getTelephone(), d.getEmail(), d.getOrdre()); 
    }
    private EquipeProjetDto toDto(EquipeProjet e) { 
        return new EquipeProjetDto(e.getIdMembre(), e.getIdSop(), e.getMatricule(), e.getNom(), e.getFonction(),
        e.getTelephone(), e.getEmail(), e.getOrdre()); 
    }
}
