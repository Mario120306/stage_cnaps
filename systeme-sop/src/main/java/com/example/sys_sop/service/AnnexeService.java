package com.example.sys_sop.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.sys_sop.modele.Annexe;
import com.example.sys_sop.repository.AnnexeRepository;

@Service
public class AnnexeService {

    private final AnnexeRepository repository;

    public AnnexeService(AnnexeRepository repository) { 
        this.repository = repository; 
    }
    public List<Annexe> findAll() {
         return repository.findAll(); 
    }
    public Annexe findById(String id) { 
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Annexe introuvable : " + id)); 
    }
    public Annexe create(Annexe entity) { 
        repository.create(entity); return entity; 
    }
    public boolean update(Annexe entity) { 
        return repository.update(entity) > 0; 
    }
    public boolean deleteById(String id) { 
        return repository.deleteById(id) > 0; 
    }
}
