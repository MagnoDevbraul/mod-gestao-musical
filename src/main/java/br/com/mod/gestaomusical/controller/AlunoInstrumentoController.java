package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.AlunoInstrumentoRequestDTO;
import br.com.mod.gestaomusical.dto.AlunoInstrumentoResponseDTO;
import br.com.mod.gestaomusical.service.AlunoInstrumentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos-instrumentos")
public class AlunoInstrumentoController {

    private final AlunoInstrumentoService service;

    public AlunoInstrumentoController(AlunoInstrumentoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AlunoInstrumentoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoInstrumentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlunoInstrumentoResponseDTO> salvar(
            @RequestBody AlunoInstrumentoRequestDTO dto) {

        return ResponseEntity.ok(service.salvar(dto));
    }
}