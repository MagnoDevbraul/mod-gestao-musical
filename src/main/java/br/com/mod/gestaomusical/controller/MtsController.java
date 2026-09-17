package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.MtsRequestDTO;
import br.com.mod.gestaomusical.dto.MtsResponseDTO;
import br.com.mod.gestaomusical.service.MtsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mts")
public class MtsController {

    private final MtsService service;

    public MtsController(MtsService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MtsResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MtsResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MtsResponseDTO> salvar(
            @RequestBody MtsRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.salvar(dto));
    }
}