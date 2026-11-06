package com.example.sys_sop.repository;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.sys_sop.modele.Notification;

@Repository
public class NotificationRepository {
    private static final String SELECT = "SELECT id_notification,id_sop,matricule,type_destinataire,titre,message,"
            + "date_creation,vue,date_vue FROM SOP_NOTIFICATION ";
    private static final RowMapper<Notification> MAPPER = (rs, rowNum) -> new Notification(
            rs.getString("ID_NOTIFICATION"), rs.getString("ID_SOP"), rs.getString("MATRICULE"),
            rs.getString("TYPE_DESTINATAIRE"), rs.getString("TITRE"), rs.getString("MESSAGE"),
            rs.getObject("DATE_CREATION", java.time.LocalDateTime.class), rs.getString("VUE"),
            rs.getObject("DATE_VUE", java.time.LocalDateTime.class));
    private final JdbcTemplate jdbc;

    public NotificationRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public List<Notification> findByMatricule(String matricule) {
        return jdbc.query(SELECT + "WHERE matricule=? ORDER BY date_creation DESC", MAPPER, matricule);
    }
    public int countUnread(String matricule) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM SOP_NOTIFICATION WHERE matricule=? AND vue='N'",
                Integer.class, matricule);
    }
    public int markRead(String id, String matricule) {
        return jdbc.update("UPDATE SOP_NOTIFICATION SET vue='O',date_vue=SYSDATE "
                + "WHERE id_notification=? AND matricule=?", id, matricule);
    }
    public int markAllRead(String matricule) {
        return jdbc.update("UPDATE SOP_NOTIFICATION SET vue='O',date_vue=SYSDATE "
                + "WHERE matricule=? AND vue='N'", matricule);
    }
    public int delete(String id, String matricule) {
        return jdbc.update("DELETE FROM SOP_NOTIFICATION WHERE id_notification=? AND matricule=?", id, matricule);
    }
    public void create(List<Notification> notifications) {
        if (notifications.isEmpty()) return;
        jdbc.batchUpdate("INSERT INTO SOP_NOTIFICATION (id_notification,id_sop,matricule,type_destinataire,"
                + "titre,message,date_creation,vue,date_vue) VALUES (?,?,?,?,?,?,SYSDATE,'N',NULL)",
                notifications, notifications.size(), (ps, n) -> {
                    ps.setString(1, n.getIdNotification()); ps.setString(2, n.getIdSop());
                    ps.setString(3, n.getMatricule()); ps.setString(4, n.getTypeDestinataire());
                    ps.setString(5, n.getTitre()); ps.setString(6, n.getMessage());
                });
    }
}
