package com.example.sys_sop.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.sys_sop.modele.Destinataire;
import com.example.sys_sop.repository.DestinataireRepository;

@Service
public class DestinataireService {
    private final DestinataireRepository repository;

    public DestinataireService(DestinataireRepository repository) { 
        this.repository = repository; 
    }
    public List<Destinataire> findAll() { 
        return repository.findAll(); 
    }
    public Destinataire findById(String id) { 
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destinataire introuvable : " + id)); 
    }
    public Destinataire create(Destinataire entity) { 
        repository.create(entity); return entity; 
    }
    public boolean update(Destinataire entity) { 
        return repository.update(entity) > 0; 
    }
    public boolean deleteById(String id) { 
        return repository.deleteById(id) > 0; 
    }
}
