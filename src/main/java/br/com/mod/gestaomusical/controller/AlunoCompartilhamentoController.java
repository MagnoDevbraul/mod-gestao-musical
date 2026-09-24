package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.AlunoCompartilhamentoRequestDTO;
import br.com.mod.gestaomusical.dto.AlunoCompartilhamentoResponseDTO;
import br.com.mod.gestaomusical.service.AlunoCompartilhamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    /*
     * Consulta todos os compartilhamentos.
     *
     * Permitido para usuários que podem consultar alunos.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ALUNO_CONSULTAR')")
    public ResponseEntity<List<AlunoCompartilhamentoResponseDTO>> listarTodos() {

        return ResponseEntity.ok(
                service.listarTodos()
        );
    }

    /*
     * Consulta um compartilhamento pelo ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ALUNO_CONSULTAR')")
    public ResponseEntity<AlunoCompartilhamentoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /*
     * Consulta o compartilhamento vinculado a um aluno.
     */
    @GetMapping("/aluno/{alunoId}")
    @PreAuthorize("hasAuthority('ALUNO_CONSULTAR')")
    public ResponseEntity<AlunoCompartilhamentoResponseDTO> buscarPorAluno(
            @PathVariable Long alunoId) {

        return service.buscarPorAlunoId(alunoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /*
     * Compartilha um aluno com outra Comum.
     *
     * Permitido para:
     * - Secretaria
     * - Encarregado Regional
     * - Encarregado Local
     *
     * Instrutor não possui ALUNO_COMPARTILHAR.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ALUNO_COMPARTILHAR')")
    public ResponseEntity<AlunoCompartilhamentoResponseDTO> compartilhar(
            @RequestBody AlunoCompartilhamentoRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.compartilhar(dto));
    }
}