package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.MsaRequestDTO;
import br.com.mod.gestaomusical.dto.MsaResponseDTO;
import br.com.mod.gestaomusical.service.MsaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/msa")
public class MsaController {

    private final MsaService service;

    public MsaController(MsaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MsaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MsaResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MsaResponseDTO> salvar(
            @RequestBody MsaRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.salvar(dto));
    }
}