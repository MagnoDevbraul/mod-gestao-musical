package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.NivelRequestDTO;
import br.com.mod.gestaomusical.entity.Nivel;
import br.com.mod.gestaomusical.service.NivelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/niveis")
public class NivelController {

    private final NivelService service;

    public NivelController(NivelService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Nivel>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nivel> buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Nivel> salvar(
            @RequestBody NivelRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Nivel> atualizar(
            @PathVariable Long id,
            @RequestBody NivelRequestDTO dto) {

        return ResponseEntity.ok(
                service.atualizar(id, dto)
        );
    }
}