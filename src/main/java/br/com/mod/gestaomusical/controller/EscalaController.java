package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.EscalaRequestDTO;
import br.com.mod.gestaomusical.dto.EscalaResponseDTO;
import br.com.mod.gestaomusical.service.EscalaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/escalas")
public class EscalaController {

    private final EscalaService service;

    public EscalaController(EscalaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EscalaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EscalaResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EscalaResponseDTO> salvar(
            @RequestBody EscalaRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.salvar(dto));
    }
}