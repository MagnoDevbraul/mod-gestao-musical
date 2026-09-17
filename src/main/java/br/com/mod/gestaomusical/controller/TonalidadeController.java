package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.entity.Tonalidade;
import br.com.mod.gestaomusical.service.TonalidadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tonalidades")
public class TonalidadeController {

    private final TonalidadeService service;

    public TonalidadeController(TonalidadeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Tonalidade>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tonalidade> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}