package cl.duoc.vidasalud.ms_vidasalud_rabbitmq_admin.controller;

import cl.duoc.vidasalud.ms_vidasalud_rabbitmq_admin.dto.BindingRequest;
import cl.duoc.vidasalud.ms_vidasalud_rabbitmq_admin.dto.ExchangeRequest;
import cl.duoc.vidasalud.ms_vidasalud_rabbitmq_admin.dto.QueueRequest;
import cl.duoc.vidasalud.ms_vidasalud_rabbitmq_admin.service.RabbitAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class RabbitAdminController {

    private final RabbitAdminService service;

    public RabbitAdminController(RabbitAdminService service) {
        this.service = service;
    }

    @PostMapping("/queues")
    public ResponseEntity<Map<String, Object>> createQueue(@Valid @RequestBody QueueRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createQueue(request));
    }

    @DeleteMapping("/queues/{name}")
    public ResponseEntity<Void> deleteQueue(@PathVariable String name) {
        service.deleteQueue(name);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/exchanges")
    public ResponseEntity<Map<String, Object>> createExchange(@Valid @RequestBody ExchangeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createExchange(request));
    }

    @DeleteMapping("/exchanges/{name}")
    public ResponseEntity<Void> deleteExchange(@PathVariable String name) {
        service.deleteExchange(name);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bindings")
    public ResponseEntity<Map<String, Object>> createBinding(@Valid @RequestBody BindingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createBinding(request));
    }

    @DeleteMapping("/bindings")
    public ResponseEntity<Void> deleteBinding(@Valid @RequestBody BindingRequest request) {
        service.deleteBinding(request);
        return ResponseEntity.noContent().build();
    }
}