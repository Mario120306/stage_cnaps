package com.example.sys_sop.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sys_sop.dto.UtilisateurResponseDto;
import com.example.sys_sop.modele.Utilisateur;
import com.example.sys_sop.service.UtilisateurService;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public List<UtilisateurResponseDto> getAll() {
        return utilisateurService.findAll()
                .stream()
                .map(this::toUtilisateurResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{matricule}")
    public UtilisateurResponseDto getById(@PathVariable("matricule") String matricule) {
        return toUtilisateurResponseDto(utilisateurService.findById(matricule));
    }

    private UtilisateurResponseDto toUtilisateurResponseDto(Utilisateur entity) {
        if (entity == null) return null;
        return new UtilisateurResponseDto(
                entity.getMatricule(),
                entity.getNom(),
                entity.getPrenoms(),
                entity.getDateNaissaince(),
                entity.getLieu(),
                entity.getCodeDirection(),
                entity.getLibelleDirection(),
                entity.getCodeService(),
                entity.getLibelleService(),
                entity.getCodeFonction(),
                entity.getLibelleFonction(),
                entity.getMail(),
                entity.getTelGsm()
        );
    }
}