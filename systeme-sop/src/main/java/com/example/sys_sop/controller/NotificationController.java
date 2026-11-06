package com.example.sys_sop.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.sys_sop.dto.NotificationDto;
import com.example.sys_sop.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService service;
    public NotificationController(NotificationService service) { this.service = service; }
    @GetMapping public List<NotificationDto> find(@RequestParam String matricule) {
        return service.findByMatricule(matricule);
    }
    @GetMapping("/count") public int count(@RequestParam String matricule) {
        return service.countUnread(matricule);
    }
    @PatchMapping("/{id}/lue") public ResponseEntity<Void> read(@PathVariable String id, @RequestParam String matricule) {
        service.markRead(id, matricule); return ResponseEntity.noContent().build();
    }
    @PatchMapping("/lues") public ResponseEntity<Void> readAll(@RequestParam String matricule) {
        service.markAllRead(matricule); return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable String id, @RequestParam String matricule) {
        service.delete(id, matricule); return ResponseEntity.noContent().build();
    }
}
