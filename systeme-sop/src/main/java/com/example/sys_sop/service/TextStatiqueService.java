package com.example.sys_sop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.sys_sop.modele.TextStatique;
import com.example.sys_sop.repository.TextStatiqueRepository;

@Service 
public class TextStatiqueService {
    
    private final TextStatiqueRepository textStatiqueRepository;
    
    public TextStatiqueService(TextStatiqueRepository textStatiqueRepository) {
        this.textStatiqueRepository = textStatiqueRepository;
    }
    public List<TextStatique>findAll(){
        return textStatiqueRepository.findAll();
    }
    public TextStatique findById(String code_text){
        return textStatiqueRepository.findById(code_text)
                .orElseThrow(()-> new RuntimeException("Text statique introuvable :" + code_text));
    }
    
}
