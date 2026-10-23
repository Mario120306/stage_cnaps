package com.example.sys_sop.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.sys_sop.modele.PieceJointe;
import com.example.sys_sop.repository.PieceJointeRepository;

@Service
public class PieceJointeService {
    
    private final PieceJointeRepository repository;

    public PieceJointeService(PieceJointeRepository repository) { 
        this.repository = repository; 
    }
    public List<PieceJointe> findAll() { 
        return repository.findAll(); 
    }
    public PieceJointe findById(String id) { 
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pièce jointe introuvable : " + id)); 
    }
    public PieceJointe findMetadataById(String id) {
        return repository.findMetadataById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Pièce jointe introuvable : " + id));
    }
    public byte[] findContentById(String id) {
        return repository.findContentById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pièce jointe introuvable : " + id));
    }
    public PieceJointe create(PieceJointe entity) { 
        repository.create(entity); return entity; 
    }
    public boolean update(PieceJointe entity) { 
        return repository.update(entity) > 0; 
    }
    public boolean deleteById(String id) { 
        return repository.deleteById(id) > 0; 
    }

    public PieceJointe findBySopAndNomFichier(String idSop, String nomFichier) {
        return repository.findBySopAndNomFichier(idSop, nomFichier)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pièce jointe introuvable pour SOP/fichier : " + idSop + " / " + nomFichier));
    }
}
