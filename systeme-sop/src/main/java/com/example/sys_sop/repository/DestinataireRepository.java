package com.example.sys_sop.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.sys_sop.modele.Destinataire;

@Repository
public class DestinataireRepository {
    private static final String SELECT = "SELECT id_destinataire,id_sop,matricule,nom,organisation,fonction,action,date_action,ordre FROM SOP_DESTINATAIRE ";
    
    private static final RowMapper<Destinataire> MAPPER = (rs, n) -> new Destinataire(
            rs.getString("ID_DESTINATAIRE"), rs.getString("ID_SOP"), rs.getString("MATRICULE"),
            rs.getString("NOM"), rs.getString("ORGANISATION"), rs.getString("FONCTION"), rs.getString("ACTION"),
            rs.getObject("DATE_ACTION", java.time.LocalDateTime.class), rs.getObject("ORDRE", Integer.class));
    
    private final JdbcTemplate jdbc;
    public DestinataireRepository(JdbcTemplate jdbc) { 
        this.jdbc = jdbc; 
    }
    public List<Destinataire> findAll() { 
        return jdbc.query(SELECT + "ORDER BY ordre", MAPPER); 
    }
    public List<Destinataire> findBySop(String idSop) {
        return jdbc.query(SELECT + "WHERE id_sop=? ORDER BY ordre", MAPPER, idSop);
    }
    public Optional<Destinataire> findById(String id) { 
        return jdbc.query(SELECT + "WHERE id_destinataire=?", MAPPER, id).stream().findFirst(); 
    }
    public int create(Destinataire e) {
        String sql = "INSERT INTO SOP_DESTINATAIRE (id_destinataire,id_sop,matricule,nom,organisation,fonction,action,date_action,ordre) VALUES (?,?,?,?,?,?,?,?,?)";
        return jdbc.update(sql, e.getIdDestinataire(), e.getIdSop(), e.getMatricule(), e.getNom(),
                e.getOrganisation(), e.getFonction(), e.getAction(), e.getDateAction(), e.getOrdre());
    }
    public void batchCreate(List<Destinataire> entities) {
        if (entities.isEmpty()) return;
        jdbc.batchUpdate("INSERT INTO SOP_DESTINATAIRE (id_destinataire,id_sop,matricule,nom,organisation,fonction,action,date_action,ordre) VALUES (?,?,?,?,?,?,?,?,?)",
                entities, entities.size(), (ps, e) -> {
                    ps.setString(1, e.getIdDestinataire());
                    ps.setString(2, e.getIdSop());
                    ps.setString(3, e.getMatricule());
                    ps.setString(4, e.getNom());
                    ps.setString(5, e.getOrganisation());
                    ps.setString(6, e.getFonction());
                    ps.setString(7, e.getAction());
                    ps.setObject(8, e.getDateAction());
                    ps.setObject(9, e.getOrdre());
                });
    }
    public int update(Destinataire e) {
        String sql = "UPDATE SOP_DESTINATAIRE SET id_sop=?,matricule=?,nom=?,organisation=?,fonction=?,action=?,date_action=?,ordre=? WHERE id_destinataire=?";
        return jdbc.update(sql, e.getIdSop(), e.getMatricule(), e.getNom(), e.getOrganisation(), e.getFonction(), e.getAction(),
                e.getDateAction(), e.getOrdre(), e.getIdDestinataire());
    }
    public int deleteById(String id) { 
        return jdbc.update("DELETE FROM SOP_DESTINATAIRE WHERE id_destinataire=?", id); 
    }
    public int deleteBySop(String idSop) {
        return jdbc.update("DELETE FROM SOP_DESTINATAIRE WHERE id_sop=?", idSop);
    }
}
