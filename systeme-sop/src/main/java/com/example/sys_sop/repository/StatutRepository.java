package com.example.sys_sop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.sys_sop.modele.Statut;

@Repository
public class StatutRepository {

    private static final String SELECT_COLUMNS =
            "SELECT code_statut, libelle, ordre FROM SOP_STATUT ";

    private static final RowMapper<Statut> ROW_MAPPER = (rs, rowNum) -> new Statut(
            rs.getString("CODE_STATUT"),
            rs.getString("LIBELLE"),
            rs.getInt("ORDRE")
    );

    private final JdbcTemplate jdbcTemplate;

    public StatutRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Statut> findAll() {
        return jdbcTemplate.query(SELECT_COLUMNS + " ORDER BY ordre ", ROW_MAPPER);
    }

    public Optional<Statut> findById(String codeStatut) {
        return jdbcTemplate.query(
                SELECT_COLUMNS + " WHERE code_statut = ?",
                ROW_MAPPER,
                codeStatut
        ).stream().findFirst();
    }

    public int create(Statut statut) {
        String sql = "INSERT INTO SOP_STATUT (code_statut, libelle, ordre) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, statut.getCodeStatut(), statut.getLibelle(), statut.getOrdre());
    }

    public int update(Statut statut) {
        String sql = "UPDATE SOP_STATUT SET libelle = ?, ordre = ? WHERE code_statut = ?";
        return jdbcTemplate.update(sql, statut.getLibelle(), statut.getOrdre(), statut.getCodeStatut());
    }

    public int deleteById(String codeStatut) {
        return jdbcTemplate.update("DELETE FROM SOP_STATUT WHERE code_statut = ?", codeStatut);
    }
}