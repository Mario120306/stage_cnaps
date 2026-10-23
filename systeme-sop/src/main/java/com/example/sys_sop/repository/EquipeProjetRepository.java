package com.example.sys_sop.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.sys_sop.modele.EquipeProjet;

@Repository
public class EquipeProjetRepository {

    private static final String SELECT = "SELECT id_membre,id_sop,matricule,nom,fonction,telephone,email,ordre FROM SOP_EQUIPE_PROJET ";
    
    private static final RowMapper<EquipeProjet> MAPPER = (rs, n) -> new EquipeProjet(
            rs.getString("ID_MEMBRE"), rs.getString("ID_SOP"), rs.getString("MATRICULE"), rs.getString("NOM"),
            rs.getString("FONCTION"), rs.getString("TELEPHONE"), rs.getString("EMAIL"), rs.getObject("ORDRE", Integer.class));
    
    private final JdbcTemplate jdbc;
    public EquipeProjetRepository(JdbcTemplate jdbc) { 
        this.jdbc = jdbc; 
    }
    public List<EquipeProjet> findAll() { 
        return jdbc.query(SELECT + "ORDER BY ordre", MAPPER); 
    }
    public Optional<EquipeProjet> findById(String id) { 
        return jdbc.query(SELECT + "WHERE id_membre=?", MAPPER, id).stream().findFirst(); 
    }
    public int create(EquipeProjet e) { 
        return jdbc.update("INSERT INTO SOP_EQUIPE_PROJET (id_membre,id_sop,matricule,nom,fonction,telephone,email,ordre) VALUES (?,?,?,?,?,?,?,?)", e.getIdMembre(), 
                                e.getIdSop(), e.getMatricule(), e.getNom(), e.getFonction(), e.getTelephone(), e.getEmail(), e.getOrdre()); 
    }
    public int update(EquipeProjet e) { 
        return jdbc.update("UPDATE SOP_EQUIPE_PROJET SET id_sop=?,matricule=?,nom=?,fonction=?,telephone=?,email=?,ordre=? WHERE id_membre=?", e.getIdSop(), e.getMatricule(),
         e.getNom(), e.getFonction(), e.getTelephone(), e.getEmail(), e.getOrdre(), e.getIdMembre()); 
    }
    public int deleteById(String id) { 
        return jdbc.update("DELETE FROM SOP_EQUIPE_PROJET WHERE id_membre=?", id); 
    }
    public int deleteBySop(String idSop) {
        return jdbc.update("DELETE FROM SOP_EQUIPE_PROJET WHERE id_sop=?", idSop);
    }
}
