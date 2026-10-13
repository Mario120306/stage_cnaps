package com.example.sys_sop.repository;


import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.sys_sop.modele.TextStatique;

@Repository 
public class TextStatiqueRepository {
    
    private final JdbcTemplate jdbcTemplate;

    public TextStatiqueRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<TextStatique> ROW_MAPPER = (rs, rowNum) -> new TextStatique(
            rs.getString("CODE_TEXT"),
            rs.getString("CONTENU"),
            rs.getObject("DATE_MAJ", java.time.LocalDate.class),
            rs.getString("ACTIF")
    );
    private  static final String SELECT_COLUMNS="SELECT code_text,contenu,date_maj,actif FROM SOP_TEXT_STATIQUE";
    public  List<TextStatique>findAll(){
        return jdbcTemplate.query(SELECT_COLUMNS,ROW_MAPPER);
    }
    public Optional<TextStatique>findById(String code_text){
        return  jdbcTemplate.query(SELECT_COLUMNS + "WHERE code_text = ?",ROW_MAPPER,code_text)
                .stream()        
                .findFirst();
    }    
}
