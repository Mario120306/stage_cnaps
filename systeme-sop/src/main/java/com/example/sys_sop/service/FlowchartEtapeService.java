package com.example.sys_sop.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.sys_sop.modele.FlowchartEtape;
import com.example.sys_sop.repository.FlowchartEtapeRepository;

@Service
public class FlowchartEtapeService {

    private final FlowchartEtapeRepository repository;

    public FlowchartEtapeService(FlowchartEtapeRepository repository) { 
        this.repository = repository; 
    }
    public List<FlowchartEtape> findAll() { 
        return repository.findAll(); 
    }
    public FlowchartEtape findById(String id) { 
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Étape introuvable : " + id)); 
    }
    public FlowchartEtape create(FlowchartEtape entity) { 
        repository.create(entity); return entity; 
    }
    public boolean update(FlowchartEtape entity) { 
        return repository.update(entity) > 0; 
    }
    public boolean deleteById(String id) { 
        return repository.deleteById(id) > 0; 
    }
}
