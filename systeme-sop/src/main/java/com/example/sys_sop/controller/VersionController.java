package com.example.sys_sop.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.sys_sop.dto.VersionDto;
import com.example.sys_sop.modele.Version;
import com.example.sys_sop.service.VersionService;

@RestController
@RequestMapping("/api/versions")
public class VersionController {

    private final VersionService service;
    
    public VersionController(VersionService service) { 
        this.service = service; 
    }
    @GetMapping 
    public List<VersionDto> getAll() { 
        return service.findAll()
                    .stream()
                    .map(this::toDto)
                    .collect(Collectors
                    .toList());
    }

    @GetMapping("/{id}") 
    public VersionDto getById(@PathVariable String id) { 
        return toDto(service.findById(id)); 
    }
    @GetMapping("/count/{id}") 
    public int countBySopId(@PathVariable("id") String idSop) { 
        return service.countVersionByIdSop(idSop); 
    }
    @PostMapping 
    public ResponseEntity<VersionDto> create(@RequestBody VersionDto dto) { 
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(toDto(service.create(toEntity(dto)))); 
    }
    /*@PutMapping("/{id}") 
    public ResponseEntity<VersionDto> update(@PathVariable String id, @RequestBody VersionDto dto) {
        Version entity = toEntity(dto); entity.setIdVersion(id);
        return service.update(entity) ? ResponseEntity.ok(toDto(entity)) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> delete(@PathVariable String id) { 
        return service.deleteById(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build(); 
    }*/
    private Version toEntity(VersionDto d) { 
        return new Version(d.getIdVersion(), d.getIdSop(), d.getNumeroVersion(),
                        d.getDateEntree(), d.getLibelleModification(), d.getAuteurModification(),
                        d.getCodeStatut()); 
    }
    private VersionDto toDto(Version e) { 
        return new VersionDto(e.getIdVersion(), e.getIdSop(), e.getNumeroVersion(),
                            e.getDateEntree(), e.getLibelleModification(), e.getAuteurModification(), 
                            e.getCodeStatut()); 
    }
}
