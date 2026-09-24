package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.HinarioRequestDTO;
import br.com.mod.gestaomusical.dto.HinarioResponseDTO;
import br.com.mod.gestaomusical.service.HinarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/hinarios")
public class HinarioController {

    private final HinarioService service;

    public HinarioController(HinarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<HinarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HinarioResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PROGRESSO_MUSICAL_REGISTRAR')")
    public ResponseEntity<HinarioResponseDTO> salvar(
            @RequestBody HinarioRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.salvar(dto));
    }
}