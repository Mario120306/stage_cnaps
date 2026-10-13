package com.example.sys_sop.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.sys_sop.modele.Annexe;

@Repository
public class AnnexeRepository {
    
    private static final String SELECT = "SELECT id_annexe,id_sop,titre,id_piece_jointe,ordre FROM SOP_ANNEXE ";
    
    private static final RowMapper<Annexe> MAPPER = (rs, n) -> new Annexe(
            rs.getString("ID_ANNEXE"), rs.getString("ID_SOP"), rs.getString("TITRE"), rs.getString("ID_PIECE_JOINTE"), rs.getObject("ORDRE", Integer.class));
    
    private final JdbcTemplate jdbc;

    public AnnexeRepository(JdbcTemplate jdbc) { 
        this.jdbc = jdbc; 
    }
    public List<Annexe> findAll() { 
        return jdbc.query(SELECT + "ORDER BY ordre", MAPPER); 
    }
    public Optional<Annexe> findById(String id) { 
        return jdbc.query(SELECT + "WHERE id_annexe=?", MAPPER, id).stream().findFirst(); 
    }
    public int create(Annexe e) { 
        return jdbc.update("INSERT INTO SOP_ANNEXE (id_annexe,id_sop,titre,id_piece_jointe,ordre) VALUES (?,?,?,?,?)", 
                            e.getIdAnnexe(), e.getIdSop(), e.getTitre(), e.getIdPieceJointe(), e.getOrdre()); 
    }
    public int update(Annexe e) { 
        return jdbc.update("UPDATE SOP_ANNEXE SET id_sop=?,titre=?,id_piece_jointe=?,ordre=? WHERE id_annexe=?", 
                            e.getIdSop(), e.getTitre(), e.getIdPieceJointe(), e.getOrdre(), e.getIdAnnexe()); 
    }
    public int deleteById(String id) { 
        return jdbc.update("DELETE FROM SOP_ANNEXE WHERE id_annexe=?", id); 
    }
}
