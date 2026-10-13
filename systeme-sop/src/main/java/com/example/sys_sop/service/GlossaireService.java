package com.example.sys_sop.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.sys_sop.modele.Glossaire;
import com.example.sys_sop.repository.GlossaireRepository;

@Service
public class GlossaireService {
    
    private final GlossaireRepository repository;

    public GlossaireService(GlossaireRepository repository) { 
        this.repository = repository; 
    }
    public List<Glossaire> findAll() { 
        return repository.findAll(); 
    }
    public Glossaire findById(String id) { 
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entrée de glossaire introuvable : " + id)); 
    }
    public Glossaire create(Glossaire entity) { 
        repository.create(entity); return entity; 
    }
    public boolean update(Glossaire entity) { 
        return repository.update(entity) > 0; 
    }
    public boolean deleteById(String id) { 
        return repository.deleteById(id) > 0; 
    }
}
