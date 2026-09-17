package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.ExclusaoAlunoRequestDTO;
import br.com.mod.gestaomusical.dto.ExclusaoAlunoResponseDTO;
import br.com.mod.gestaomusical.service.ExclusaoAlunoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/exclusoes-alunos")
public class ExclusaoAlunoController {

    private final ExclusaoAlunoService service;

    public ExclusaoAlunoController(ExclusaoAlunoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ExclusaoAlunoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExclusaoAlunoResponseDTO> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ExclusaoAlunoResponseDTO> excluir(
            @RequestBody ExclusaoAlunoRequestDTO dto) {

        return ResponseEntity.ok(service.excluir(dto));
    }
}