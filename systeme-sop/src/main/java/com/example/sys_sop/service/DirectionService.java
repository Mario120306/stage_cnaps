package com.example.sys_sop.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.sys_sop.modele.Direction;
import com.example.sys_sop.repository.DirectionRepository;

@Service 
public class DirectionService {

    private final DirectionRepository directionRepository;

    public DirectionService(DirectionRepository directionRepository) {
        this.directionRepository = directionRepository;
    }

    public List<Direction> findAll(){
        return this.directionRepository.findAll();
    }
    public  Direction findById(String codeDirection){
        return  directionRepository.findById(codeDirection)
                .orElseThrow(()-> new RuntimeException(
                    "Direction introuvable :" + codeDirection
                ));
    }
}
