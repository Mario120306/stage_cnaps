package com.example.sys_sop.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.sys_sop.modele.Version;

@Repository
public class VersionRepository {
    private static final String SELECT = "SELECT id_version, id_sop, numero_version, date_entree, libelle_modification, auteur_modification, code_statut FROM SOP_VERSION ";
    
    private static final RowMapper<Version> MAPPER = (rs, n) -> new Version(
            rs.getString("ID_VERSION"), rs.getString("ID_SOP"), rs.getString("NUMERO_VERSION"),
            rs.getObject("DATE_ENTREE", java.time.LocalDateTime.class), rs.getString("LIBELLE_MODIFICATION"),
            rs.getString("AUTEUR_MODIFICATION"), rs.getString("CODE_STATUT"));
    
    private final JdbcTemplate jdbc;
    
    public VersionRepository(JdbcTemplate jdbc) { 
        this.jdbc = jdbc; 
    }
    public List<Version> findAll() { 
        return jdbc.query(SELECT + "ORDER BY date_entree DESC", MAPPER); 
    }
    public Optional<Version> findById(String id) { 
        return jdbc.query(SELECT + "WHERE id_version=?", MAPPER, id).stream().findFirst(); 
    }
    public int create(Version e) {
        String sql = "INSERT INTO SOP_VERSION (id_version, id_sop, numero_version, date_entree, "
                + "libelle_modification, auteur_modification, code_statut) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Timestamp dateEntree = e.getDateEntree() != null
                ? Timestamp.valueOf(e.getDateEntree())
                : new Timestamp(System.currentTimeMillis());

        return jdbc.update(sql,
                e.getIdVersion(), e.getIdSop(), e.getNumeroVersion(), dateEntree,
                e.getLibelleModification(), e.getAuteurModification(), e.getCodeStatut());
    }
    public int update(Version e) {
        String sql = "UPDATE SOP_VERSION SET id_sop=?, numero_version=?, date_entree=?, libelle_modification=?, auteur_modification=?, code_statut=? WHERE id_version=?";
        return jdbc.update(sql, e.getIdSop(), e.getNumeroVersion(), e.getDateEntree(), e.getLibelleModification(), e.getAuteurModification(), e.getCodeStatut(), e.getIdVersion());
    }
    public int countVersionByIdSop(String idSop){
        String sql = "SELECT COUNT(ID_VERSION) FROM SOP_VERSION WHERE id_sop=?";
        return jdbc.queryForObject(sql, Integer.class, idSop);
    }

    public long countAll() {
        return jdbc.queryForObject("SELECT COUNT(*) FROM SOP_VERSION", Long.class);
    }
}
