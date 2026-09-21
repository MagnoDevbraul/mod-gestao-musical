package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.HistoricoResponseDTO;
import br.com.mod.gestaomusical.service.HistoricoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historicos")
public class HistoricoController {

    private final HistoricoService service;

    public HistoricoController(
            HistoricoService service) {

        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<HistoricoResponseDTO>> listarTodos() {

        return ResponseEntity.ok(
                service.listarTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoricoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}