package com.example.sys_sop.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.sys_sop.dto.ContactDto;
import com.example.sys_sop.modele.Contact;
import com.example.sys_sop.service.ContactService;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    private final ContactService service;
    public ContactController(ContactService service) { 
        this.service = service; 
    }
    @GetMapping 
    public List<ContactDto> getAll() { 
        return service.findAll()
                      .stream()
                      .map(this::toDto)
                      .collect(Collectors
                      .toList());
    }
    @GetMapping("/{id}") 
    public ContactDto getById(@PathVariable String id) { 
        return toDto(service.findById(id)); 
    }
    @PostMapping 
    public ResponseEntity<ContactDto> create(@RequestBody ContactDto dto) { 
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(service.create(toEntity(dto)))); 
    }
    @PutMapping("/{id}")
    public ResponseEntity<ContactDto> update(@PathVariable String id, @RequestBody ContactDto dto) {
        Contact entity = toEntity(dto); entity.setIdContact(id);
        return service.update(entity) ? ResponseEntity.ok(toDto(entity)) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { 
        return service.deleteById(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build(); 
    }
    private Contact toEntity(ContactDto d) { 
        return new Contact(d.getIdContact(), d.getIdSop(), d.getMatricule(), d.getEmail(), d.getOrdre()); 
    }
    private ContactDto toDto(Contact e) { 
        return new ContactDto(e.getIdContact(), e.getIdSop(), e.getMatricule(), e.getEmail(), e.getOrdre()); 
    }
}
