package com.example.sys_sop.controller;


import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sys_sop.dto.ServiceDrhResponseDto;
import com.example.sys_sop.modele.ServiceDrh;
import com.example.sys_sop.service.ServiceDrhService;

@RestController 
@RequestMapping("/api/services") 
public class ServiceController {

    private  final ServiceDrhService serviceDrhService;

    public ServiceController(ServiceDrhService serviceDrhService) {
        this.serviceDrhService = serviceDrhService;
    }
    @GetMapping
    public List<ServiceDrhResponseDto>getAll(){
        return serviceDrhService.findAll()
                .stream()
                .map(this::toServiceDrhResponseDto)
                .collect(Collectors.toList());
    }
    @GetMapping("/{code}")
    public ServiceDrhResponseDto getById(@PathVariable("code") String codeService){
        ServiceDrh serviceDrh=serviceDrhService.findById(codeService);
        return toServiceDrhResponseDto(serviceDrh);
    } 
    private ServiceDrhResponseDto toServiceDrhResponseDto(ServiceDrh entity){
        if(entity == null) return  null;
        return new ServiceDrhResponseDto(
            entity.getCodeService(),
            entity.getLibelleService(),
            entity.getCodeDirection()
        );
    } 
}
