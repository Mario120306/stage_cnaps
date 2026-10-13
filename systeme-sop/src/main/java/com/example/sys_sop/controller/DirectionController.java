package com.example.sys_sop.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.sys_sop.dto.DirectionResponseDto;
import com.example.sys_sop.modele.Direction;
import com.example.sys_sop.service.DirectionService;


@RestController
@RequestMapping("/api/directions")  
public class DirectionController {

    private final DirectionService directionService;

    public DirectionController(DirectionService directionService) {
        this.directionService = directionService;
    }

    @GetMapping
    public List<DirectionResponseDto> getAll(){
        return directionService.findAll()
                .stream()
                .map(this::toDirectionResponseDto)
                .collect(Collectors.toList());
    }
    @GetMapping("/{code}")
    public DirectionResponseDto getById(@PathVariable("code") String codeDirection) {
        Direction direction = directionService.findById(codeDirection);
        return toDirectionResponseDto(direction);
    }
    /* 
    private Direction toDirectionEntity(DirectionRequestDto dto) {
        if (dto == null) return null;
        return new Direction(
                dto.getCodeDirection(),
                dto.getLibelleDirection(),
                dto.getCodeAbrev()
        );
    }

    /**
     * Entité Direction  ->  DTO Response
     */
    private DirectionResponseDto toDirectionResponseDto(Direction entity) {
        if (entity == null) return null;
        return new DirectionResponseDto(
                entity.getCodeDirection(),
                entity.getLibelleDirection(),
                entity.getCodeAbrev()
        );
    }
}
