package com.example.sys_sop.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sys_sop.dto.StatutDto;
import com.example.sys_sop.modele.Statut;
import com.example.sys_sop.service.StatutService;

@RestController
@RequestMapping("/api/statuts")
public class StatutController {

    private final StatutService statutService;

    public StatutController(StatutService statutService) {
        this.statutService = statutService;
    }

    @GetMapping
    public List<StatutDto> getAll() {
        return statutService.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{codeStatut}")
    public StatutDto getById(@PathVariable("codeStatut") String codeStatut) {
        return toDto(statutService.findById(codeStatut));
    }

    @PostMapping
    public ResponseEntity<StatutDto> create(@RequestBody StatutDto dto) {
        Statut created = statutService.create(toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @PutMapping("/{codeStatut}")
    public ResponseEntity<StatutDto> update(
            @PathVariable("codeStatut") String codeStatut,
            @RequestBody StatutDto dto
    ) {
        Statut statut = toEntity(dto);
        statut.setCodeStatut(codeStatut);
        if (!statutService.update(statut)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDto(statut));
    }

    @DeleteMapping("/{codeStatut}")
    public ResponseEntity<Void> delete(@PathVariable("codeStatut") String codeStatut) {
        if (!statutService.deleteById(codeStatut)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    private Statut toEntity(StatutDto dto) {
        return new Statut(dto.getCodeStatut(), dto.getLibelle(), dto.getOrdre());
    }

    private StatutDto toDto(Statut entity) {
        return new StatutDto(entity.getCodeStatut(), entity.getLibelle(), entity.getOrdre());
    }
}