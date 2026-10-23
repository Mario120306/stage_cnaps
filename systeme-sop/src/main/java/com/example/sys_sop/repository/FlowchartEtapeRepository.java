package com.example.sys_sop.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.sys_sop.modele.FlowchartEtape;

@Repository
public class FlowchartEtapeRepository {

    private static final String SELECT = "SELECT id_etape,id_flowchart,information,niveau,mode_numerotation,ordre FROM SOP_FLOWCHART_ETAPE ";
    
    private static final RowMapper<FlowchartEtape> MAPPER = (rs, n) -> new FlowchartEtape(
            rs.getString("ID_ETAPE"), rs.getString("ID_FLOWCHART"), rs.getString("INFORMATION"), rs.getObject("NIVEAU", Integer.class),
            rs.getString("MODE_NUMEROTATION"), rs.getObject("ORDRE", Integer.class));
    
    private final JdbcTemplate jdbc;
    
    public FlowchartEtapeRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc; 
    }
    public List<FlowchartEtape> findAll() { 
        return jdbc.query(SELECT + "ORDER BY ordre", MAPPER); 
    }
    public List<FlowchartEtape> findBySop(String idSop) {
        return jdbc.query(SELECT + "WHERE id_flowchart IN "
                + "(SELECT id_flowchart FROM SOP_FLOWCHART WHERE id_sop=?) ORDER BY ordre", MAPPER, idSop);
    }
    public Optional<FlowchartEtape> findById(String id) { 
        return jdbc.query(SELECT + "WHERE id_etape=?", MAPPER, id).stream().findFirst(); 
    }
    public int create(FlowchartEtape e) { 
        return jdbc.update("INSERT INTO SOP_FLOWCHART_ETAPE (id_etape,id_flowchart,information,niveau,mode_numerotation,ordre) VALUES (?,?,?,?,?,?)", 
                            e.getIdEtape(), e.getIdFlowchart(), e.getInformation(), e.getNiveau(), e.getModeNumerotation(), e.getOrdre()); 
    }
    public int update(FlowchartEtape e) { 
        return jdbc.update("UPDATE SOP_FLOWCHART_ETAPE SET id_flowchart=?,information=?,niveau=?,mode_numerotation=?,ordre=? WHERE id_etape=?", 
        e.getIdFlowchart(), e.getInformation(), e.getNiveau(), e.getModeNumerotation(), e.getOrdre(), e.getIdEtape()); 
    }
    public int deleteById(String id) { 
        return jdbc.update("DELETE FROM SOP_FLOWCHART_ETAPE WHERE id_etape=?", id); 
    }
}
