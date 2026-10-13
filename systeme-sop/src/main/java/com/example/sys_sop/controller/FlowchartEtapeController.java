package com.example.sys_sop.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.sys_sop.dto.FlowchartEtapeDto;
import com.example.sys_sop.modele.FlowchartEtape;
import com.example.sys_sop.service.FlowchartEtapeService;

@RestController
@RequestMapping("/api/flowchart-etapes")
public class FlowchartEtapeController {

    private final FlowchartEtapeService service;

    public FlowchartEtapeController(FlowchartEtapeService service) { 
        this.service = service; 
    }
    @GetMapping 
    public List<FlowchartEtapeDto> getAll() { 
        return service.findAll()
                    .stream()
                    .map(this::toDto)
                    .collect(Collectors.toList()); 
    }
    @GetMapping("/{id}") 
    public FlowchartEtapeDto getById(@PathVariable String id) { 
        return toDto(service.findById(id)); 
    }
    @PostMapping
    public ResponseEntity<FlowchartEtapeDto> create(@RequestBody FlowchartEtapeDto dto) { 
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(toDto(service.create(toEntity(dto)))); 
    }
    @PutMapping("/{id}") 
    public ResponseEntity<FlowchartEtapeDto> update(@PathVariable String id, @RequestBody FlowchartEtapeDto dto) {
        FlowchartEtape entity = toEntity(dto); entity.setIdEtape(id);
        return service.update(entity) ? ResponseEntity.ok(toDto(entity)) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> delete(@PathVariable String id) { 
        return service.deleteById(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build(); 
    }
    private FlowchartEtape toEntity(FlowchartEtapeDto d) { 
        return new FlowchartEtape(d.getIdEtape(), d.getIdFlowchart(), d.getInformation(), 
                                d.getNiveau(), d.getModeNumerotation(), d.getOrdre()); 
    }
    private FlowchartEtapeDto toDto(FlowchartEtape e) { 
        return new FlowchartEtapeDto(e.getIdEtape(), e.getIdFlowchart(), e.getInformation(), 
                                    e.getNiveau(), e.getModeNumerotation(), e.getOrdre()); 
    }    
}
