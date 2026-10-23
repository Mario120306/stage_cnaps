package com.example.sys_sop.repository;

import java.sql.Types;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.sys_sop.modele.Reference;

@Repository
public class ReferenceRepository {

    private static final String SELECT = "SELECT id_reference,id_sop,contenu,ordre FROM SOP_REFERENCE ";
    
    private static final RowMapper<Reference> MAPPER = (rs, n) -> new Reference(
            rs.getString("ID_REFERENCE"), rs.getString("ID_SOP"), rs.getString("CONTENU"), rs.getObject("ORDRE", Integer.class));
    
    private final JdbcTemplate jdbc;

    public ReferenceRepository(JdbcTemplate jdbc) { 
        this.jdbc = jdbc; 
    }

    public List<Reference> findAll() { 
        return jdbc.query(SELECT + "ORDER BY ordre", MAPPER); 
    }

    public Optional<Reference> findById(String id) { 
        return jdbc.query(SELECT + "WHERE id_reference=?", MAPPER, id).stream().findFirst(); 
    }

    public int create(Reference e) {
        String sql = "INSERT INTO SOP_REFERENCE (id_reference,id_sop,contenu,ordre) VALUES (?,?,?,?)";
        return jdbc.update(sql, ps -> {
            ps.setString(1, e.getIdReference());
            ps.setString(2, e.getIdSop());
            if (e.getContenu() == null) ps.setNull(3, Types.CLOB);
            else ps.setCharacterStream(3, new StringReader(e.getContenu()), e.getContenu().length());
            ps.setObject(4, e.getOrdre());
        });
    }

    public int update(Reference e) {
        String sql = "UPDATE SOP_REFERENCE SET id_sop=?,contenu=?,ordre=? WHERE id_reference=?";
        return jdbc.update(sql, ps -> {
            ps.setString(1, e.getIdSop());
            if (e.getContenu() == null) ps.setNull(2, Types.CLOB);
            else ps.setCharacterStream(2, new StringReader(e.getContenu()), e.getContenu().length());
            ps.setObject(3, e.getOrdre());
            ps.setString(4, e.getIdReference());
        });
    }

    public int deleteById(String id) { 
        return jdbc.update("DELETE FROM SOP_REFERENCE WHERE id_reference=?", id); 
    }
    public int deleteBySop(String idSop) {
        return jdbc.update("DELETE FROM SOP_REFERENCE WHERE id_sop=?", idSop);
    }
}
