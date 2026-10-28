package com.example.sys_sop.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.sys_sop.modele.Flowchart;

@Repository
public class FlowchartRepository {
    
    private static final String SELECT = "SELECT id_flowchart,id_sop,titre,information,ordre,id_piece_jointe FROM SOP_FLOWCHART ";
    
    private static final RowMapper<Flowchart> MAPPER = (rs, n) -> new Flowchart(
            rs.getString("ID_FLOWCHART"), rs.getString("ID_SOP"), rs.getString("TITRE"), rs.getString("INFORMATION"), rs.getObject("ORDRE", Integer.class), rs.getString("ID_PIECE_JOINTE"));
    
    private final JdbcTemplate jdbc;

    public FlowchartRepository(JdbcTemplate jdbc) { 
        this.jdbc = jdbc;
    }
    public List<Flowchart> findAll() { 
        return jdbc.query(SELECT + "ORDER BY ordre", MAPPER); 
    }
    public List<Flowchart> findBySop(String idSop) {
        return jdbc.query(SELECT + "WHERE id_sop=? ORDER BY ordre", MAPPER, idSop);
    }
    public Optional<Flowchart> findById(String id) { 
        return jdbc.query(SELECT + "WHERE id_flowchart=?", MAPPER, id).stream().findFirst(); 
    }
    public int create(Flowchart e) { 
        return jdbc.update("INSERT INTO SOP_FLOWCHART (id_flowchart,id_sop,titre,information,ordre,id_piece_jointe) VALUES (?,?,?,?,?,?)", 
                                e.getIdFlowchart(), e.getIdSop(), e.getTitre(), e.getInformation(), e.getOrdre(), e.getIdPieceJointe()); 
    }
    public int update(Flowchart e) { 
        return jdbc.update("UPDATE SOP_FLOWCHART SET id_sop=?,titre=?,information=?,ordre=?,id_piece_jointe=? WHERE id_flowchart=?", 
        e.getIdSop(), e.getTitre(), e.getInformation(), e.getOrdre(), e.getIdPieceJointe(), e.getIdFlowchart()); 
    }
    public int deleteById(String id) { 
        return jdbc.update("DELETE FROM SOP_FLOWCHART WHERE id_flowchart=?", id); 
    }
}
