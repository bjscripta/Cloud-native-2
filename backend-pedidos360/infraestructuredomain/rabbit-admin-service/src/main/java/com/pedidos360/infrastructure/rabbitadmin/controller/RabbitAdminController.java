package com.pedidos360.infrastructure.rabbitadmin.controller;

import java.util.List;
import java.util.Map;

import com.pedidos360.infrastructure.rabbitadmin.service.RabbitManagementService;
import com.pedidos360.infrastructure.rabbitadmin.service.RabbitManagementService.QueueStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit-admin")
public class RabbitAdminController {

    private final RabbitManagementService rabbitManagementService;

    public RabbitAdminController(RabbitManagementService rabbitManagementService) {
        this.rabbitManagementService = rabbitManagementService;
    }

    @GetMapping("/queues")
    public List<QueueStatus> queues() {
        return rabbitManagementService.getQueues();
    }

    @GetMapping("/queues/{name}")
    public QueueStatus queue(@PathVariable String name) {
        return rabbitManagementService.getQueue(name);
    }

    @DeleteMapping("/dlq/{name}/messages")
    public Map<String, Object> purgeDlq(@PathVariable String name) {
        int purged = rabbitManagementService.purgeDlq(name);
        return Map.of("queue", name, "purgedMessages", purged);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> invalidQueue(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
    }
}
