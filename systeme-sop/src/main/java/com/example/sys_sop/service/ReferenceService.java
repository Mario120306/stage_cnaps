package com.example.sys_sop.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.sys_sop.modele.Reference;
import com.example.sys_sop.repository.ReferenceRepository;

@Service
public class ReferenceService {

    private final ReferenceRepository repository;

    public ReferenceService(ReferenceRepository repository) { 
        this.repository = repository; 
    }
    public List<Reference> findAll() { 
        return repository.findAll(); 
    }
    public Reference findById(String id) { 
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Référence introuvable : " + id)); 
    }
    public Reference create(Reference entity) { 
        repository.create(entity); return entity; 
    }
    public boolean update(Reference entity) { 
        return repository.update(entity) > 0; 
    }
    public boolean deleteById(String id) { 
        return repository.deleteById(id) > 0; 
    }
}
