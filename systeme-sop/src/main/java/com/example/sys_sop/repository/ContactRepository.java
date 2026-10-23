package com.example.sys_sop.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.sys_sop.modele.Contact;

@Repository
public class ContactRepository {

    private static final String SELECT = "SELECT id_contact, id_sop, matricule, email, ordre FROM SOP_CONTACT ";
    private static final RowMapper<Contact> MAPPER = (rs, n) -> new Contact(
            rs.getString("ID_CONTACT"), rs.getString("ID_SOP"), rs.getString("MATRICULE"),
            rs.getString("EMAIL"), rs.getObject("ORDRE", Integer.class));
    
    private final JdbcTemplate jdbc;
    public ContactRepository(JdbcTemplate jdbc) { 
        this.jdbc = jdbc; 
    }
    public List<Contact> findAll() { 
        return jdbc.query(SELECT + "ORDER BY ordre", MAPPER); 
    }
    public List<Contact> findBySop(String idSop) {
        return jdbc.query(SELECT + "WHERE id_sop=? ORDER BY ordre", MAPPER, idSop);
    }
    public Optional<Contact> findById(String id) { 
        return jdbc.query(SELECT + "WHERE id_contact=?", MAPPER, id).stream().findFirst(); 
    }
    public int create(Contact e) { 
        return jdbc.update("INSERT INTO SOP_CONTACT (id_contact,id_sop,matricule,email,ordre) VALUES (?,?,?,?,?)", 
                                e.getIdContact(), e.getIdSop(), e.getMatricule(), e.getEmail(), e.getOrdre()); 
    }
    public int update(Contact e) { 
        return jdbc.update("UPDATE SOP_CONTACT SET id_sop=?,matricule=?,email=?,ordre=? WHERE id_contact=?", 
                                e.getIdSop(), e.getMatricule(), e.getEmail(), e.getOrdre(), e.getIdContact()); }
    public int deleteById(String id) { 
        return jdbc.update("DELETE FROM SOP_CONTACT WHERE id_contact=?", id); 
    }
    public int deleteBySop(String idSop) {
        return jdbc.update("DELETE FROM SOP_CONTACT WHERE id_sop=?", idSop);
    }
}
