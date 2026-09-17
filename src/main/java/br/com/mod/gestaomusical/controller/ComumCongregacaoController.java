package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.ComumCongregacaoRequestDTO;
import br.com.mod.gestaomusical.entity.ComumCongregacao;
import br.com.mod.gestaomusical.service.ComumCongregacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comuns")
public class ComumCongregacaoController {

    private final ComumCongregacaoService service;

    public ComumCongregacaoController(
            ComumCongregacaoService service) {

        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ComumCongregacao>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComumCongregacao> buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ComumCongregacao> salvar(
            @RequestBody ComumCongregacaoRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComumCongregacao> atualizar(
            @PathVariable Long id,
            @RequestBody ComumCongregacaoRequestDTO dto) {

        return ResponseEntity.ok(
                service.atualizar(id, dto)
        );
    }
}