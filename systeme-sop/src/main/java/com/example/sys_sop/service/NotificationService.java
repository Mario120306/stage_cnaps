package com.example.sys_sop.service;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.sys_sop.dto.NotificationDto;
import com.example.sys_sop.modele.Contact;
import com.example.sys_sop.modele.Destinataire;
import com.example.sys_sop.modele.EquipeProjet;
import com.example.sys_sop.modele.Notification;
import com.example.sys_sop.repository.NotificationRepository;

@Service
public class NotificationService {

    private static final String COMPLEMENT_NOUVEAU_SOP =
            " Merci d'en prendre connaissance et de me signaler toute difficulté éventuelle avant sa mise en application.";
    private static final String COMPLEMENT_AJOUT =
            " Merci de vérifier que cette procédure est compatible avec les pratiques actuelles de votre équipe et de signaler toute difficulté d'interprétation.";
    private static final String COMPLEMENT_RETRAIT =
            " Merci de ne plus vous référer à cette procédure à compter de la date de retrait et de signaler toute question éventuelle.";

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) { this.repository = repository; }

    public List<NotificationDto> findByMatricule(String matricule) {
        validate(matricule);
        return repository.findByMatricule(matricule.trim()).stream().map(this::toDto).collect(Collectors.toList());
    }
    public int countUnread(String matricule) { validate(matricule); return repository.countUnread(matricule.trim()); }
    public void markRead(String id, String matricule) {
        validate(matricule);
        if (repository.markRead(id, matricule.trim()) == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    public void markAllRead(String matricule) {
        validate(matricule);
        repository.markAllRead(matricule.trim());
    }
    public void delete(String id, String matricule) {
        validate(matricule);
        if (repository.delete(id, matricule.trim()) == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public void notifyNewSop(String idSop, String numero, String titre, List<Destinataire> destinataires,
            List<Contact> contacts, List<EquipeProjet> equipe) {
        LinkedHashMap<String, Set<String>> recipients = recipients(destinataires, contacts, equipe);
        repository.create(recipients.entrySet().stream().map(x -> new Notification(UUID.randomUUID().toString(),
                idSop, x.getKey(), String.join(",", x.getValue()), "Nouveau SOP disponible",
                "La procédure " + numero + " - " + titre + " vous concerne." + COMPLEMENT_NOUVEAU_SOP,
                null, "N", null))
                .collect(Collectors.toList()));
    }

    public void notifySopUpdate(String idSop, String numero, String titre,
            List<Destinataire> oldDestinataires, List<Contact> oldContacts, List<EquipeProjet> oldEquipe,
            List<Destinataire> newDestinataires, List<Contact> newContacts, List<EquipeProjet> newEquipe) {
        LinkedHashMap<String, Set<String>> oldRecipients = recipients(oldDestinataires, oldContacts, oldEquipe);
        LinkedHashMap<String, Set<String>> newRecipients = recipients(newDestinataires, newContacts, newEquipe);
        List<Notification> notifications = newRecipients.entrySet().stream()
                .flatMap(entry -> difference(idSop, entry, oldRecipients.get(entry.getKey()), "Ajout dans un SOP",
                        "Vous avez été ajouté au SOP " + numero + " - " + titre + " comme %s." + COMPLEMENT_AJOUT).stream())
                .collect(Collectors.toList());
        notifications.addAll(oldRecipients.entrySet().stream()
                .flatMap(entry -> difference(idSop, entry, newRecipients.get(entry.getKey()), "Retrait d'un SOP",
                        "Vous avez été retiré du SOP " + numero + " - " + titre + " comme %s." + COMPLEMENT_RETRAIT).stream())
                .collect(Collectors.toList()));
        repository.create(notifications);
    }

    private List<Notification> difference(String idSop, Map.Entry<String, Set<String>> entry, Set<String> otherRoles,
            String title, String messageTemplate) {
        Set<String> changedRoles = new LinkedHashSet<>(entry.getValue());
        if (otherRoles != null) {
            changedRoles.removeAll(otherRoles);
        }
        return changedRoles.stream()
                .map(role -> new Notification(UUID.randomUUID().toString(), idSop, entry.getKey(), role, title,
                        String.format(messageTemplate, roleLabel(role)), null, "N", null))
                .collect(Collectors.toList());
    }

    private LinkedHashMap<String, Set<String>> recipients(List<Destinataire> destinataires,
            List<Contact> contacts, List<EquipeProjet> equipe) {
        LinkedHashMap<String, Set<String>> recipients = new LinkedHashMap<>();
        destinataires.stream().filter(x -> valid(x.getMatricule()))
                .forEach(x -> addRole(recipients, x.getMatricule(), "DESTINATAIRE"));
        contacts.stream().filter(x -> valid(x.getMatricule()))
                .forEach(x -> addRole(recipients, x.getMatricule(), "CONTACT_COMMENTAIRE"));
        equipe.stream().filter(x -> valid(x.getMatricule()))
                .forEach(x -> addRole(recipients, x.getMatricule(), "EQUIPE_PROJET"));
        return recipients;
    }

    private String roleLabel(String role) {
        if ("CONTACT_COMMENTAIRE".equals(role)) return "commentataire";
        if ("EQUIPE_PROJET".equals(role)) return "équipe de projet";
        return "destinataire";
    }
    private void addRole(Map<String, Set<String>> recipients, String matricule, String role) {
        recipients.computeIfAbsent(matricule.trim(), key -> new LinkedHashSet<>()).add(role);
    }
    private boolean valid(String value) { return value != null && !value.isBlank(); }
    private void validate(String value) {
        if (!valid(value)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le matricule est obligatoire.");
    }
    private NotificationDto toDto(Notification n) {
        return new NotificationDto(n.getIdNotification(), n.getIdSop(), n.getMatricule(),
                n.getTypeDestinataire(), n.getTitre(), n.getMessage(), n.getDateCreation(), n.getVue(), n.getDateVue());
    }
}