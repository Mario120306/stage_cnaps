package com.example.sys_sop.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import com.example.sys_sop.modele.Direction;

@Repository 
public class DirectionRepository {

    private  final JdbcTemplate jdbcTemplate;

    public DirectionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Direction> ROW_MAPPER = (rs, rowNum) -> new Direction(
            rs.getString("CODE_DIRECTION"),
            rs.getString("LIBELLE_DIRECTION"),
            rs.getString("CODE_ABREV")
    );
    /** get Direction*/
    public List<Direction>findAll(){
        String sql="SELECT code_direction,libelle_direction,code_abrev FROM DRH.DRH_DIRECTION";
        return jdbcTemplate.query(sql,ROW_MAPPER);
    }
    public Optional<Direction> findById(String codeDirection) {
        String sql = "SELECT code_direction, libelle_direction, code_abrev "
                    + "FROM DRH.DRH_DIRECTION "
                    + "WHERE code_direction = ?";
        return jdbcTemplate.query(sql, ROW_MAPPER, codeDirection).stream().findFirst();
    }

}
