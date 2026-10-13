package com.example.sys_sop.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.sys_sop.modele.Contact;
import com.example.sys_sop.repository.ContactRepository;

@Service
public class ContactService {
    private final ContactRepository repository;

    public ContactService(ContactRepository repository) { 
        this.repository = repository; 
    }
    public List<Contact> findAll() { 
        return repository.findAll(); 
    }
    public Contact findById(String id) { 
        return repository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact introuvable : " + id)); 
    }
    public Contact create(Contact entity) { 
        repository.create(entity); return entity; 
    }
    public boolean update(Contact entity) { 
        return repository.update(entity) > 0; 
    }
    public boolean deleteById(String id) { 
        return repository.deleteById(id) > 0; 
    }
}
