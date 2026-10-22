package com.example.sys_sop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.sys_sop.modele.Utilisateur;

@Repository
public class UtilisateurRepository {

    private final JdbcTemplate jdbcTemplate;

    public UtilisateurRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Utilisateur> ROW_MAPPER = (rs, rowNum) -> new Utilisateur(
            rs.getString("MATRICULE"),
            rs.getString("NOM"),
            rs.getString("PRENOMS"),
            rs.getDate("DATE_NAISSAINCE") == null ? null : rs.getDate("DATE_NAISSAINCE").toLocalDate(),
            rs.getString("LIEU"),
            rs.getString("CODE_DIRECTION"),
            rs.getString("LIBELLE_DIRECTION"),
            rs.getString("CODE_SERVICE"),
            rs.getString("LIBELLE_SERVICE"),
            rs.getString("CODE_FONCTION"),
            rs.getString("LIBELLE_FONCTION"),
            rs.getString("MAIL"),
            rs.getString("TEL_GSM")
    );

    private static final String SELECT_COLUMNS = "SELECT matricule, nom, prenoms, date_naissaince, lieu, "
            + "code_direction, libelle_direction, code_service, libelle_service, "
            + "code_fonction, libelle_fonction, mail, tel_gsm FROM INTRANET.VIEW_ANNUAIRE_ELECTRONIQUE ";

    public List<Utilisateur> findAll() {
        return jdbcTemplate.query(SELECT_COLUMNS + " ORDER BY matricule ", ROW_MAPPER);
    }

    public Optional<Utilisateur> findById(String matricule) {
        return jdbcTemplate.query(SELECT_COLUMNS + " WHERE matricule = ?", ROW_MAPPER, matricule)
                .stream()
                .findFirst();
    }

    public List<Utilisateur> searchByMail(String mail) {
        // Utilisation de ROWNUM pour compatibilité Oracle 11g et antérieur
        String sql = "SELECT * FROM ("
                + SELECT_COLUMNS
                + "WHERE LOWER(mail) LIKE LOWER(?) ORDER BY mail"
                + ") WHERE ROWNUM <= 8";
        return jdbcTemplate.query(sql, ROW_MAPPER, "%" + mail.trim() + "%");
    }

    public List<Utilisateur> searchByMatricule(String matricule) {
        // Utilisation de ROWNUM pour compatibilité Oracle 11g et antérieur
        String sql = "SELECT * FROM ("
                + SELECT_COLUMNS
                + "WHERE LOWER(matricule) LIKE LOWER(?) ORDER BY matricule"
                + ") WHERE ROWNUM <= 8";
        return jdbcTemplate.query(sql, ROW_MAPPER, "%" + matricule.trim() + "%");
    }
}