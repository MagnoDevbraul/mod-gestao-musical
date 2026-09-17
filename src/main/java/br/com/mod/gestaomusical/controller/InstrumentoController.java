package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.entity.Instrumento;
import br.com.mod.gestaomusical.service.InstrumentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instrumentos")
public class InstrumentoController {

    private final InstrumentoService service;

    public InstrumentoController(InstrumentoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Instrumento>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Instrumento> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}