package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.MetodoRequestDTO;
import br.com.mod.gestaomusical.dto.MetodoResponseDTO;
import br.com.mod.gestaomusical.service.MetodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/metodos")
public class MetodoController {

    private final MetodoService service;

    public MetodoController(MetodoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MetodoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetodoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PROGRESSO_MUSICAL_REGISTRAR')")
    public ResponseEntity<MetodoResponseDTO> salvar(
            @RequestBody MetodoRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.salvar(dto));
    }
}