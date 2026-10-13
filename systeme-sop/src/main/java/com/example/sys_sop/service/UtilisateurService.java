package com.example.sys_sop.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.sys_sop.modele.Utilisateur;
import com.example.sys_sop.repository.UtilisateurRepository;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }

    public Utilisateur findById(String matricule) {
        return utilisateurRepository.findById(matricule)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable : " + matricule));
    }
}