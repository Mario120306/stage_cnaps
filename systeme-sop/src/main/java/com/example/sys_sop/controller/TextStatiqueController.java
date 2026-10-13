package com.example.sys_sop.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sys_sop.dto.TextStatiqueResponseDto;
import com.example.sys_sop.modele.TextStatique;
import com.example.sys_sop.service.TextStatiqueService;

@RestController
@RequestMapping("/api/textes-statiques")
public class TextStatiqueController {

    private final TextStatiqueService textStatiqueService;

    public TextStatiqueController(TextStatiqueService textStatiqueService) {
        this.textStatiqueService = textStatiqueService;
    }

    @GetMapping
    public List<TextStatiqueResponseDto> getAll() {
        return textStatiqueService.findAll()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{codeText}")
    public TextStatiqueResponseDto getById(@PathVariable("codeText") String codeText) {
        return toResponseDto(textStatiqueService.findById(codeText));
    }

    private TextStatiqueResponseDto toResponseDto(TextStatique entity) {
        if (entity == null) return null;
        return new TextStatiqueResponseDto(
                entity.getCodeText(),
                entity.getContenu(),
                entity.getDateMaj(),
                entity.getActif()
        );
    }
}
