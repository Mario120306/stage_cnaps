package com.example.sys_sop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.sys_sop.modele.ServiceDrh;

@Repository 
public class ServiceDrhRepository {
    
    private  final JdbcTemplate jdbcTemplate;

    public ServiceDrhRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public  static  final RowMapper<ServiceDrh>ROW_MAPPER =(rs,rowNum)-> new ServiceDrh(
        rs.getString("CODE_SERVICE"),
        rs.getString("LIBELLE_SERVICE"),
        rs.getString("CODE_DIRECTION")
    );
    public List<ServiceDrh>findAll(){
        String sql="SELECT code_service,libelle_service,code_direction FROM DRH.DRH_SERVICE";
        return jdbcTemplate.query(sql,ROW_MAPPER);
    }
    public  Optional<ServiceDrh>findById(String codeService){
        String sql ="SELECT code_service,libelle_service,code_direction "
                   + "FROM DRH.DRH_SERVICE  "
                   + "WHERE code_service = ?";
        return jdbcTemplate.query(sql, ROW_MAPPER,codeService).stream().findFirst();                
    }
}
