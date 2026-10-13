package com.example.sys_sop.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.sys_sop.modele.Export;
import com.example.sys_sop.repository.ExportRepository;

@Service
public class ExportService {

    private final ExportRepository repository;

    public ExportService(ExportRepository repository) { 
        this.repository = repository; 
    }
    public List<Export> findAll() { 
        return repository.findAll(); 
    }
    public Export findById(String id) { 
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Export introuvable : " + id)); 
    }
    public Export create(Export entity) { 
        repository.create(entity); return entity; 
    }
    public boolean update(Export entity) { 
        return repository.update(entity) > 0; 
    }
    public boolean deleteById(String id) { 
        return repository.deleteById(id) > 0; 
    }
}
