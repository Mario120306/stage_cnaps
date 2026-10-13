package com.example.sys_sop.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.sys_sop.dto.GlossaireDto;
import com.example.sys_sop.modele.Glossaire;
import com.example.sys_sop.service.GlossaireService;

@RestController
@RequestMapping("/api/glossaires")
public class GlossaireController {

    private final GlossaireService service;

    public GlossaireController(GlossaireService service) { 
        this.service = service; 
    }
    @GetMapping 
    public List<GlossaireDto> getAll() {
        return service.findAll()
                      .stream()
                      .map(this::toDto)
                      .collect(Collectors.toList()); 
    }
    @GetMapping("/{id}") 
    public GlossaireDto getById(@PathVariable String id) { 
        return toDto(service.findById(id)); 
    }
    @PostMapping
    public ResponseEntity<GlossaireDto> create(@RequestBody GlossaireDto dto) { 
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(toDto(service
                            .create(toEntity(dto)))); 
    }
    @PutMapping("/{id}")
    public ResponseEntity<GlossaireDto> update(@PathVariable String id, @RequestBody GlossaireDto dto) {
        Glossaire entity = toEntity(dto); entity.setIdGlossaire(id);
        return service.update(entity) ? ResponseEntity.ok(toDto(entity)) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { 
        return service.deleteById(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build(); 
    }
    private Glossaire toEntity(GlossaireDto d) { 
        return new Glossaire(d.getIdGlossaire(), d.getIdSop(), d.getAcronyme(), d.getTerme(), 
                            d.getOrdre()); 
    }
    private GlossaireDto toDto(Glossaire e) { 
        return new GlossaireDto(e.getIdGlossaire(), e.getIdSop(), e.getAcronyme(), e.getTerme(), 
                                e.getOrdre()); 
    }
}
