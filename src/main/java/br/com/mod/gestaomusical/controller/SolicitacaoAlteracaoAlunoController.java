package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.DecisaoAlteracaoAlunoRequestDTO;
import br.com.mod.gestaomusical.dto.SolicitacaoAlteracaoAlunoRequestDTO;
import br.com.mod.gestaomusical.dto.SolicitacaoAlteracaoAlunoResponseDTO;
import br.com.mod.gestaomusical.service.SolicitacaoAlteracaoAlunoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alteracoes-restritas-alunos")
public class SolicitacaoAlteracaoAlunoController {

    private final SolicitacaoAlteracaoAlunoService service;

    public SolicitacaoAlteracaoAlunoController(
            SolicitacaoAlteracaoAlunoService service) {

        this.service = service;
    }

    /*
     * Consulta todas as solicitações.
     *
     * A fila de aprovação é restrita aos usuários
     * que possuem autorização para decidir alterações.
     */
    @GetMapping
    @PreAuthorize(
            "hasAuthority('ALTERACAO_RESTRITA_APROVAR')"
    )
    public ResponseEntity<List<SolicitacaoAlteracaoAlunoResponseDTO>>
    listarTodos() {

        return ResponseEntity.ok(
                service.listarTodos()
        );
    }

    @GetMapping("/pendentes")
    @PreAuthorize(
            "hasAuthority('ALTERACAO_RESTRITA_APROVAR')"
    )
    public ResponseEntity<List<SolicitacaoAlteracaoAlunoResponseDTO>>
    listarPendentes() {

        return ResponseEntity.ok(
                service.listarPendentes()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize(
            "hasAuthority('ALTERACAO_RESTRITA_APROVAR')"
    )
    public ResponseEntity<SolicitacaoAlteracaoAlunoResponseDTO>
    buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /*
     * Cria uma solicitação.
     *
     * O aluno ainda não é alterado.
     */
    @PostMapping("/aluno/{alunoId}")
    @PreAuthorize(
            "hasAuthority('ALUNO_EDITAR_RESTRITO')"
    )
    public ResponseEntity<SolicitacaoAlteracaoAlunoResponseDTO>
    solicitar(
            @PathVariable Long alunoId,
            @RequestBody SolicitacaoAlteracaoAlunoRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        service.solicitar(
                                alunoId,
                                dto
                        )
                );
    }

    /*
     * Aprovação deve ser realizada por usuário
     * diferente do solicitante.
     */
    @PatchMapping("/{id}/aprovar")
    @PreAuthorize(
            "hasAuthority('ALTERACAO_RESTRITA_APROVAR')"
    )
    public ResponseEntity<SolicitacaoAlteracaoAlunoResponseDTO>
    aprovar(
            @PathVariable Long id,
            @RequestBody(required = false)
            DecisaoAlteracaoAlunoRequestDTO dto) {

        return ResponseEntity.ok(
                service.aprovar(
                        id,
                        dto
                )
        );
    }

    /*
     * Rejeição também deve ser realizada por
     * usuário diferente do solicitante.
     */
    @PatchMapping("/{id}/rejeitar")
    @PreAuthorize(
            "hasAuthority('ALTERACAO_RESTRITA_APROVAR')"
    )
    public ResponseEntity<SolicitacaoAlteracaoAlunoResponseDTO>
    rejeitar(
            @PathVariable Long id,
            @RequestBody
            DecisaoAlteracaoAlunoRequestDTO dto) {

        return ResponseEntity.ok(
                service.rejeitar(
                        id,
                        dto
                )
        );
    }
}