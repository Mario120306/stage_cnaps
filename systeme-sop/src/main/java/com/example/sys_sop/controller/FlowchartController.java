package com.example.sys_sop.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.sys_sop.dto.FlowchartDto;
import com.example.sys_sop.modele.Flowchart;
import com.example.sys_sop.service.FlowchartService;

@RestController
@RequestMapping("/api/flowcharts")
public class FlowchartController {

    private final FlowchartService service;

    public FlowchartController(FlowchartService service) { 
        this.service = service; 
    }
    @GetMapping
    public List<FlowchartDto> getAll() { 
        return service.findAll()
                    .stream()
                    .map(this::toDto)
                    .collect(Collectors
                    .toList()); 
    }
    @GetMapping("/{id}") 
    public FlowchartDto getById(@PathVariable String id) { 
        return toDto(service.findById(id)); 
    }
    @PostMapping
    public ResponseEntity<FlowchartDto> create(@RequestBody FlowchartDto dto) { 
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(toDto(service.create(toEntity(dto)))); 
    }
    @PutMapping("/{id}")
    public ResponseEntity<FlowchartDto> update(@PathVariable String id, @RequestBody FlowchartDto dto) {
        Flowchart entity = toEntity(dto); entity.setIdFlowchart(id);
        return service.update(entity) ? ResponseEntity.ok(toDto(entity)) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable String id) { 
        return service.deleteById(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build(); 
    }
    private Flowchart toEntity(FlowchartDto d) { 
        return new Flowchart(d.getIdFlowchart(), d.getIdSop(), d.getTitre(), d.getInformation(), d.getOrdre(), 
                            d.getIdPieceJointe()); 
    }
    private FlowchartDto toDto(Flowchart e) { 
        return new FlowchartDto(e.getIdFlowchart(), e.getIdSop(), e.getTitre(), e.getInformation(), e.getOrdre(), 
        e.getIdPieceJointe()); 
    }
}
