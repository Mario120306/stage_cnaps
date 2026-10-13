package com.example.sys_sop.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.sys_sop.modele.ServiceDrh;
import com.example.sys_sop.repository.ServiceDrhRepository;

@Service
public class ServiceDrhService {

    private  final ServiceDrhRepository serviceDrhRepository;

    public ServiceDrhService(ServiceDrhRepository serviceRepository) {
        this.serviceDrhRepository = serviceRepository;
    }
    public List<ServiceDrh>findAll(){
        return this.serviceDrhRepository.findAll();
    }
    public  ServiceDrh findById(String codeService){
        return  serviceDrhRepository.findById(codeService)
                .orElseThrow(()-> new RuntimeException(
                    "Direction introuvable :" + codeService
                ));
    }
}
