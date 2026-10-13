package com.example.sys_sop.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.sys_sop.dto.ReferenceDto;
import com.example.sys_sop.modele.Reference;
import com.example.sys_sop.service.ReferenceService;

@RestController
@RequestMapping("/api/references")
public class ReferenceController {

    private final ReferenceService service;

    public ReferenceController(ReferenceService service) { 
        this.service = service; 
    }
    @GetMapping 
    public List<ReferenceDto> getAll() { 
        return service.findAll()
                    .stream()
                    .map(this::toDto)
                    .collect(Collectors
                    .toList()); 
    }
    @GetMapping("/{id}")
    public ReferenceDto getById(@PathVariable String id) { 
        return toDto(service.findById(id)); 
    }
    @PostMapping 
    public ResponseEntity<ReferenceDto> create(@RequestBody ReferenceDto dto) { 
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(toDto(service.create(toEntity(dto)))); 
    }
    @PutMapping("/{id}") public ResponseEntity<ReferenceDto> update(@PathVariable String id, @RequestBody ReferenceDto dto) {
        Reference entity = toEntity(dto); entity.setIdReference(id);
        return service.update(entity) ? ResponseEntity.ok(toDto(entity)) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { 
        return service.deleteById(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build(); 
    }
    private Reference toEntity(ReferenceDto d) { 
        return new Reference(d.getIdReference(), d.getIdSop(), d.getContenu(), d.getOrdre()); 
    }
    private ReferenceDto toDto(Reference e) { 
        return new ReferenceDto(e.getIdReference(), e.getIdSop(), e.getContenu(), e.getOrdre()); 
    }
}
