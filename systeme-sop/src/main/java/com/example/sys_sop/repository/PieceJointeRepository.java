package com.example.sys_sop.repository;

import java.sql.Types;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.sys_sop.modele.PieceJointe;
import java.sql.Timestamp;

@Repository
public class PieceJointeRepository {

    private static final String SELECT = "SELECT id_piece_jointe,id_sop,usage,nom_fichier,type_mime,taille,contenu,auteur_matricule,date_insert FROM SOP_PIECE_JOINTE ";
    
    private static final RowMapper<PieceJointe> MAPPER = (rs, n) -> new PieceJointe(
            rs.getString("ID_PIECE_JOINTE"), rs.getString("ID_SOP"), rs.getString("USAGE"),
            rs.getString("NOM_FICHIER"), rs.getString("TYPE_MIME"), rs.getObject("TAILLE", Long.class),
            rs.getBytes("CONTENU"), rs.getString("AUTEUR_MATRICULE"),
            rs.getObject("DATE_INSERT", java.time.LocalDateTime.class));
    
    private final JdbcTemplate jdbc;

    public PieceJointeRepository(JdbcTemplate jdbc) { 
        this.jdbc = jdbc; 
    }
    public List<PieceJointe> findAll() { 
        return jdbc.query(SELECT + "ORDER BY date_insert DESC", MAPPER); 
    }
    public Optional<PieceJointe> findById(String id) { 
        return jdbc.query(SELECT + "WHERE id_piece_jointe=?", MAPPER, id).stream().findFirst(); 
    }
    public int create(PieceJointe e) {
        String sql = "INSERT INTO SOP_PIECE_JOINTE (id_piece_jointe,id_sop,usage,nom_fichier,type_mime,taille,contenu,auteur_matricule,date_insert) VALUES (?,?,?,?,?,?,?,?,?)";
        return jdbc.update(sql, ps -> {
            ps.setString(1, e.getIdPieceJointe());
            ps.setString(2, e.getIdSop());
            ps.setString(3, e.getUsage());
            ps.setString(4, e.getNomFichier());
            ps.setString(5, e.getTypeMime());
            ps.setObject(6, e.getTaille());
            if (e.getContenu() == null) ps.setNull(7, Types.BLOB);
            else ps.setBinaryStream(7, new ByteArrayInputStream(e.getContenu()), e.getContenu().length);
            ps.setString(8, e.getAuteurMatricule());
            ps.setTimestamp(9, e.getDateInsert() != null
                    ? Timestamp.valueOf(e.getDateInsert())
                    : new Timestamp(System.currentTimeMillis()));
        });
    }
    public int update(PieceJointe e) {
        String sql = "UPDATE SOP_PIECE_JOINTE SET id_sop=?,usage=?,nom_fichier=?,type_mime=?,taille=?,contenu=?,auteur_matricule=?,date_insert=? WHERE id_piece_jointe=?";
        return jdbc.update(sql, ps -> {
            ps.setString(1, e.getIdSop());
            ps.setString(2, e.getUsage());
            ps.setString(3, e.getNomFichier());
            ps.setString(4, e.getTypeMime());
            ps.setObject(5, e.getTaille());
            if (e.getContenu() == null) ps.setNull(6, Types.BLOB);
            else ps.setBinaryStream(6, new ByteArrayInputStream(e.getContenu()), e.getContenu().length);
            ps.setString(7, e.getAuteurMatricule());
            ps.setObject(8, e.getDateInsert());
            ps.setString(9, e.getIdPieceJointe());
        });
    }
    public int deleteById(String id) { 
        return jdbc.update("DELETE FROM SOP_PIECE_JOINTE WHERE id_piece_jointe=?", id); 
    }
}
