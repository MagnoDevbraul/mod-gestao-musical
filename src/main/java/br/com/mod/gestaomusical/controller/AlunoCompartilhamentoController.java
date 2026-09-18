package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.AlunoCompartilhamentoRequestDTO;
import br.com.mod.gestaomusical.dto.AlunoCompartilhamentoResponseDTO;
import br.com.mod.gestaomusical.service.AlunoCompartilhamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos-compartilhamentos")
public class AlunoCompartilhamentoController {

    private final AlunoCompartilhamentoService service;

    public AlunoCompartilhamentoController(
            AlunoCompartilhamentoService service) {

        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AlunoCompartilhamentoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoCompartilhamentoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<AlunoCompartilhamentoResponseDTO> buscarPorAluno(
            @PathVariable Long alunoId) {

        return service.buscarPorAlunoId(alunoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlunoCompartilhamentoResponseDTO> compartilhar(
            @RequestBody AlunoCompartilhamentoRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.compartilhar(dto));
    }
}