package com.example.sys_sop.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.sys_sop.modele.Statut;
import com.example.sys_sop.repository.StatutRepository;

@Service
public class StatutService {

    private final StatutRepository statutRepository;

    public StatutService(StatutRepository statutRepository) {
        this.statutRepository = statutRepository;
    }

    public List<Statut> findAll() {
        return statutRepository.findAll();
    }

    public Statut findById(String codeStatut) {
        return statutRepository.findById(codeStatut)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Statut introuvable : " + codeStatut
                ));
    }

    public Statut create(Statut statut) {
        statutRepository.create(statut);
        return statut;
    }

    public boolean update(Statut statut) {
        return statutRepository.update(statut) > 0;
    }

    public boolean deleteById(String codeStatut) {
        return statutRepository.deleteById(codeStatut) > 0;
    }
}