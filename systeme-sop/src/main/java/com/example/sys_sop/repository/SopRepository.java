package com.example.sys_sop.repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.sys_sop.modele.Sop;

@Repository
public class SopRepository {
    private static final String COLUMNS = "s.id_sop, s.numero_sop, s.titre, s.version_courante, s.code_service, s.auteur_matricule, s.section_validee, s.code_statut, s.supprime, s.date_suppression, s.date_creation, s.date_modification, se.libelle_service, d.libelle_direction";
    private static final String SELECT = "SELECT " + COLUMNS
            + " FROM SOP s JOIN DRH.DRH_SERVICE se ON s.code_service=se.code_service"
            + " JOIN DRH.DRH_DIRECTION d ON se.code_direction=d.code_direction ";
    private static final RowMapper<Sop> MAPPER = (rs, n) -> new Sop(
            rs.getString("ID_SOP"), rs.getString("NUMERO_SOP"), rs.getString("TITRE"),
            rs.getString("VERSION_COURANTE"), rs.getString("CODE_SERVICE"), rs.getString("AUTEUR_MATRICULE"),
            rs.getObject("SECTION_VALIDEE", Integer.class),rs.getString("CODE_STATUT"), rs.getString("SUPPRIME"),
            rs.getObject("DATE_SUPPRESSION", java.time.LocalDateTime.class),
            rs.getObject("DATE_CREATION", java.time.LocalDateTime.class),
            rs.getObject("DATE_MODIFICATION", java.time.LocalDateTime.class),
            rs.getString("LIBELLE_SERVICE"),rs.getString("LIBELLE_DIRECTION")    
        );

    private final JdbcTemplate jdbc;
    
    public SopRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public List<Sop> findAll() { 
        return jdbc.query( 
            "SELECT s.id_sop,s.numero_sop,s.titre,s.version_courante,s.code_service, "
            + "s.auteur_matricule,s.section_validee,s.code_statut,s.supprime,s.date_suppression,s.date_creation,s.date_modification,se.libelle_service,d.libelle_direction "
            + "FROM SOP s "
            + "JOIN DRH.DRH_SERVICE se ON s.code_service=se.code_service "
            + "JOIN DRH.DRH_DIRECTION d ON se.code_direction=d.code_direction "
            + "WHERE (s.code_statut IS NULL OR s.code_statut <> 'STAT002') "
            + "ORDER BY s.date_creation DESC", MAPPER
        ); 
    }

    public List<Sop> findPage(int page, int size, String titre, String codeDirection, String codeService, String auteur, LocalDate dateFrom, LocalDate dateTo) {
        int offset = page * size;
        StringBuilder where = new StringBuilder(" WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        where.append(" AND (s.code_statut IS NULL OR s.code_statut <> 'STAT002') ");
        if (titre != null && !titre.isBlank()) {
            where.append(" AND LOWER(s.titre) LIKE ? ");
            params.add("%" + titre.toLowerCase() + "%");
        }
        if (codeDirection != null && !codeDirection.isBlank() && !"#".equals(codeDirection)) {
            where.append(" AND d.code_direction = ? ");
            params.add(codeDirection);
        }
        if (codeService != null && !codeService.isBlank() && !"#".equals(codeService)) {
            where.append(" AND s.code_service = ? ");
            params.add(codeService);
        }
        if (auteur != null && !auteur.isBlank()) {
            where.append(" AND UPPER(s.auteur_matricule) = ? ");
            params.add(auteur.toUpperCase());
        }
        if (dateFrom != null) {
            where.append(" AND s.date_creation >= ? ");
            params.add(Timestamp.valueOf(dateFrom.atStartOfDay()));
        }
        if (dateTo != null) {
            where.append(" AND s.date_creation <= ? ");
            params.add(Timestamp.valueOf(dateTo.atTime(LocalTime.MAX)));
        }

        String sql = "SELECT * FROM ("
                + " SELECT s.id_sop, s.numero_sop, s.titre, s.version_courante, s.code_service, s.auteur_matricule, s.section_validee, s.code_statut, s.supprime, s.date_suppression, s.date_creation, s.date_modification, se.libelle_service, d.libelle_direction,"
                + " ROW_NUMBER() OVER (ORDER BY s.date_creation DESC, s.id_sop) AS rn"
                + " FROM SOP s JOIN DRH.DRH_SERVICE se ON s.code_service=se.code_service JOIN DRH.DRH_DIRECTION d ON se.code_direction=d.code_direction "
                + where.toString()
                + " ) WHERE rn > ? AND rn <= ? ORDER BY rn";

        params.add(offset);
        params.add(offset + size);

        return jdbc.query(sql, MAPPER, params.toArray());
    }

    public int countFiltered(String titre, String codeDirection, String codeService, String auteur, LocalDate dateFrom, LocalDate dateTo) {
        StringBuilder where = new StringBuilder(" WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        
        where.append(" AND (s.code_statut IS NULL OR s.code_statut <> 'STAT002') ");
        if (titre != null && !titre.isBlank()) {
            where.append(" AND LOWER(s.titre) LIKE ? ");
            params.add("%" + titre.toLowerCase() + "%");
        }
        if (codeDirection != null && !codeDirection.isBlank() && !"#".equals(codeDirection)) {
            where.append(" AND d.code_direction = ? ");
            params.add(codeDirection);
        }
        if (codeService != null && !codeService.isBlank() && !"#".equals(codeService)) {
            where.append(" AND s.code_service = ? ");
            params.add(codeService);
        }
        if (auteur != null && !auteur.isBlank()) {
            where.append(" AND UPPER(s.auteur_matricule) = ? ");
            params.add(auteur.toUpperCase());
        }
        if (dateFrom != null) {
            where.append(" AND s.date_creation >= ? ");
            params.add(Timestamp.valueOf(dateFrom.atStartOfDay()));
        }
        if (dateTo != null) {
            where.append(" AND s.date_creation <= ? ");
            params.add(Timestamp.valueOf(dateTo.atTime(LocalTime.MAX)));
        }

        String sql = "SELECT COUNT(*) FROM SOP s JOIN DRH.DRH_SERVICE se ON s.code_service=se.code_service JOIN DRH.DRH_DIRECTION d ON se.code_direction=d.code_direction " + where.toString();
        Integer count = jdbc.queryForObject(sql, Integer.class, params.toArray());
        return count == null ? 0 : count;
    }

    public Optional<Sop> findById(String id) { 
        return jdbc.query(SELECT + "WHERE id_sop = ?", MAPPER, id).stream().findFirst(); 
    }

    public long countSops() {
        return jdbc.queryForObject("SELECT COUNT(*) FROM SOP", Long.class);
    }

    public long nextSopNumber() {
        return jdbc.queryForObject("SELECT seq_sop.NEXTVAL FROM DUAL", Long.class);
    }

    public int create(Sop e) {
        String sql = "INSERT INTO SOP (id_sop, numero_sop, titre, version_courante, code_service, "
                + "auteur_matricule, section_validee, code_statut, supprime, date_suppression, "
                + "date_creation, date_modification) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String versionCourante = e.getVersionCourante() != null ? e.getVersionCourante() : "1";
        Integer sectionValidee = e.getSectionValidee() != null ? e.getSectionValidee() : 0;
        String supprime = e.getSupprime() != null ? e.getSupprime() : "N";
        Timestamp dateSuppression = e.getDateSuppression() != null
                ? Timestamp.valueOf(e.getDateSuppression()) : null;
        Timestamp dateCreation = e.getDateCreation() != null
                ? Timestamp.valueOf(e.getDateCreation())
                : new Timestamp(System.currentTimeMillis());

        // À la création, la dernière modification = la date de création
        Timestamp dateModification = dateCreation;

        return jdbc.update(sql,
                e.getIdSop(), e.getNumeroSop(), e.getTitre(), versionCourante,
                e.getCodeService(), e.getAuteurMatricule(), sectionValidee,
                e.getCodeStatut(), supprime, dateSuppression, dateCreation, dateModification);
    }

    public int update(Sop e) {
        String sql = "UPDATE SOP SET numero_sop=?, titre=?, version_courante=?, code_service=?, auteur_matricule=?, section_validee=?, code_statut=?, supprime=?, date_suppression=?, date_creation=?, date_modification=SYSDATE WHERE id_sop=?";
        return jdbc.update(sql, e.getNumeroSop(), e.getTitre(), e.getVersionCourante(), e.getCodeService(), e.getAuteurMatricule(), e.getSectionValidee(), e.getCodeStatut(), e.getSupprime(), e.getDateSuppression(), e.getDateCreation(), e.getIdSop());
    }

    public int deleteById(String id) { 
        return jdbc.update("DELETE FROM SOP WHERE id_sop=?", id); 
    }
}
