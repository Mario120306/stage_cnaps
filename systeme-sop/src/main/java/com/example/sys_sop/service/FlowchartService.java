package com.example.sys_sop.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.sys_sop.modele.Flowchart;
import com.example.sys_sop.repository.FlowchartRepository;

@Service
public class FlowchartService {
    
    private final FlowchartRepository repository;

    public FlowchartService(FlowchartRepository repository) { 
        this.repository = repository; 
    }
    public List<Flowchart> findAll() { 
        return repository.findAll(); 
    }
    public Flowchart findById(String id) { 
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flowchart introuvable : " + id)); 
    }
    public Flowchart create(Flowchart entity) { 
        repository.create(entity); return entity; 
    }
    public boolean update(Flowchart entity) { 
        return repository.update(entity) > 0; 
    }
    public boolean deleteById(String id) { 
        return repository.deleteById(id) > 0; 
    }
}
