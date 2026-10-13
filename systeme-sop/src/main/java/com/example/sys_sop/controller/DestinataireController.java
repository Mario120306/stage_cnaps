package com.example.sys_sop.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.sys_sop.dto.DestinataireDto;
import com.example.sys_sop.modele.Destinataire;
import com.example.sys_sop.service.DestinataireService;

@RestController
@RequestMapping("/api/destinataires")
public class DestinataireController {

    private final DestinataireService service;

    public DestinataireController(DestinataireService service) { 
        this.service = service; 
    }
    @GetMapping 
    public List<DestinataireDto> getAll() { 
        return service.findAll()
                    .stream()
                    .map(this::toDto)
                    .collect(Collectors
                    .toList());
    }
    @GetMapping("/{id}")
    public DestinataireDto getById(@PathVariable String id) { 
        return toDto(service.findById(id)); 
    }
    @PostMapping
    public ResponseEntity<DestinataireDto> create(@RequestBody DestinataireDto dto) { 
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(service.create(toEntity(dto)))); 
    }
    @PutMapping("/{id}")
    public ResponseEntity<DestinataireDto> update(@PathVariable String id, @RequestBody DestinataireDto dto) {
        Destinataire entity = toEntity(dto); entity.setIdDestinataire(id);
        return service.update(entity) ? ResponseEntity.ok(toDto(entity)) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { 
        return service.deleteById(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build(); 
    }
    private Destinataire toEntity(DestinataireDto d) { 
        return new Destinataire(d.getIdDestinataire(), d.getIdSop(), d.getMatricule(), d.getNom(),
                                d.getOrganisation(), d.getAction(), d.getDateAction(), d.getOrdre()); 
    }
    private DestinataireDto toDto(Destinataire e) { 
        return new DestinataireDto(e.getIdDestinataire(), e.getIdSop(), e.getMatricule(), e.getNom(),
        e.getOrganisation(), e.getAction(), e.getDateAction(), e.getOrdre()); 
    }
}
