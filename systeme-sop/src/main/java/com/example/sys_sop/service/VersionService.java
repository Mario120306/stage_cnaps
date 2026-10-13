package com.example.sys_sop.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.sys_sop.modele.Version;
import com.example.sys_sop.repository.VersionRepository;

@Service
public class VersionService {
    
    private final VersionRepository repository;
    
    public VersionService(VersionRepository repository) { 
        this.repository = repository; 
    }
    public List<Version> findAll() { 
        return repository.findAll(); 
    }
    public Version findById(String id) { 
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Version introuvable : " + id)); 
    }
    public Version create(Version entity) { 
        repository.create(entity); return entity; 
    }
    public boolean update(Version entity) { 
        return repository.update(entity) > 0; 
    }
    public int countVersionByIdSop(String idSop){
        return repository.countVersionByIdSop(idSop);
    }
}
