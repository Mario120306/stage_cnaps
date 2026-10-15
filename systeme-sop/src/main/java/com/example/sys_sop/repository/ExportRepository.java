package com.example.sys_sop.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.sys_sop.modele.Export;

@Repository
public class ExportRepository {

    private static final String SELECT = "SELECT id_export,id_sop,id_version,libelle_export,nb_pages,date_export,nom_fichier_pdf,genere_par FROM SOP_EXPORT ";
    
    private static final RowMapper<Export> MAPPER = (rs, n) -> new Export(
            rs.getString("ID_EXPORT"), rs.getString("ID_SOP"), rs.getString("ID_VERSION"), rs.getString("LIBELLE_EXPORT"),
            rs.getObject("NB_PAGES", Integer.class), rs.getObject("DATE_EXPORT", java.time.LocalDateTime.class),
            rs.getString("NOM_FICHIER_PDF"), rs.getString("GENERE_PAR"));
    
    private final JdbcTemplate jdbc;

    public ExportRepository(JdbcTemplate jdbc) { 
        this.jdbc = jdbc; 
    }
    public List<Export> findAll() { 
        return jdbc.query(SELECT + "ORDER BY date_export DESC", MAPPER); 
    }
    public Optional<Export> findById(String id) { 
        return jdbc.query(SELECT + "WHERE id_export=?", MAPPER, id).stream().findFirst(); 
    }
    public int create(Export e) {
        String sql = "INSERT INTO SOP_EXPORT (id_export,id_sop,id_version,libelle_export,nb_pages,date_export,nom_fichier_pdf,genere_par) VALUES (?,?,?,COALESCE(?,'Draft version'),?,COALESCE(?,SYSDATE),?,?)";
        return jdbc.update(sql, e.getIdExport(), e.getIdSop(), e.getIdVersion(), e.getLibelleExport(), e.getNbPages(), e.getDateExport(), e.getNomFichierPdf(), e.getGenerePar());
    }
    public int create(Export e, byte[] contenuPdf) {
        String sql = "INSERT INTO SOP_EXPORT (id_export,id_sop,id_version,libelle_export,nb_pages,date_export,nom_fichier_pdf,genere_par,contenu_pdf) "
                + "VALUES (?,?,?,COALESCE(?,'Draft version'),?,COALESCE(CAST(? AS DATE),SYSDATE),?,?,?)";
        return jdbc.update(sql, e.getIdExport(), e.getIdSop(), e.getIdVersion(), e.getLibelleExport(),
                e.getNbPages(), e.getDateExport(), e.getNomFichierPdf(), e.getGenerePar(), contenuPdf);
    }
    public int update(Export e) {
        String sql = "UPDATE SOP_EXPORT SET id_sop=?,id_version=?,libelle_export=?,nb_pages=?,date_export=?,nom_fichier_pdf=?,genere_par=? WHERE id_export=?";
        return jdbc.update(sql, e.getIdSop(), e.getIdVersion(), e.getLibelleExport(), e.getNbPages(), e.getDateExport(), e.getNomFichierPdf(), e.getGenerePar(), e.getIdExport());
    }
    public Optional<Export> findLatestBySop(String idSop) {
        return jdbc.query(SELECT + "WHERE id_sop=? ORDER BY date_export DESC", MAPPER, idSop).stream().findFirst();
    }
    public Optional<byte[]> findPdfById(String idExport) {
        return jdbc.query("SELECT contenu_pdf FROM SOP_EXPORT WHERE id_export=?",
                (rs, n) -> rs.getBytes("CONTENU_PDF"), idExport).stream().findFirst();
    }

    public int deleteById(String id) { 
        return jdbc.update("DELETE FROM SOP_EXPORT WHERE id_export=?", id);
    }
}
