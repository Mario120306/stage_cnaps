package com.example.sys_sop.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.sys_sop.modele.EquipeProjet;
import com.example.sys_sop.repository.EquipeProjetRepository;

@Service
public class EquipeProjetService {
    private final EquipeProjetRepository repository;
    public EquipeProjetService(EquipeProjetRepository repository) { 
        this.repository = repository; 
    }
    public List<EquipeProjet> findAll() { 
        return repository.findAll(); 
    }
    public EquipeProjet findById(String id) { 
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membre introuvable : " + id)); 
    }
    public EquipeProjet create(EquipeProjet entity) { 
        repository.create(entity); return entity; 
    }
    public boolean update(EquipeProjet entity) { 
        return repository.update(entity) > 0; 
    }
    public boolean deleteById(String id) { 
        return repository.deleteById(id) > 0; 
    }
}
