package com.example.sys_sop.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.sys_sop.modele.Glossaire;

@Repository
public class GlossaireRepository {

    private static final String SELECT = "SELECT id_glossaire,id_sop,acronyme,terme,ordre FROM SOP_GLOSSAIRE ";
    
    private static final RowMapper<Glossaire> MAPPER = (rs, n) -> new Glossaire(
            rs.getString("ID_GLOSSAIRE"), rs.getString("ID_SOP"), rs.getString("ACRONYME"), rs.getString("TERME"), rs.getObject("ORDRE", Integer.class));
    
    private final JdbcTemplate jdbc;

    public GlossaireRepository(JdbcTemplate jdbc) { 
        this.jdbc = jdbc; 
    }
    public List<Glossaire> findAll() { 
        return jdbc.query(SELECT + "ORDER BY ordre", MAPPER); 
    }
    public Optional<Glossaire> findById(String id) { 
        return jdbc.query(SELECT + "WHERE id_glossaire=?", MAPPER, id).stream().findFirst(); 
    }
    public int create(Glossaire e) { 
        return jdbc.update("INSERT INTO SOP_GLOSSAIRE (id_glossaire,id_sop,acronyme,terme,ordre) VALUES (?,?,?,?,?)", e.getIdGlossaire(), 
                                e.getIdSop(), e.getAcronyme(), e.getTerme(), e.getOrdre()); 
    }
    public int update(Glossaire e) { 
        return jdbc.update("UPDATE SOP_GLOSSAIRE SET id_sop=?,acronyme=?,terme=?,ordre=? WHERE id_glossaire=?", e.getIdSop(), e.getAcronyme(),
         e.getTerme(), e.getOrdre(), e.getIdGlossaire());
    }
    public int deleteById(String id) { 
        return jdbc.update("DELETE FROM SOP_GLOSSAIRE WHERE id_glossaire=?", id); 
    }
    public int deleteBySop(String idSop) {
        return jdbc.update("DELETE FROM SOP_GLOSSAIRE WHERE id_sop=?", idSop);
    }
}
