package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.ExclusaoAlunoRequestDTO;
import br.com.mod.gestaomusical.dto.ExclusaoAlunoResponseDTO;
import br.com.mod.gestaomusical.service.ExclusaoAlunoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exclusoes-alunos")
public class ExclusaoAlunoController {

    private final ExclusaoAlunoService service;

    public ExclusaoAlunoController(
            ExclusaoAlunoService service) {

        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ExclusaoAlunoResponseDTO>> listarTodos() {

        return ResponseEntity.ok(
                service.listarTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExclusaoAlunoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /*
     * Exclusão originada no SAM.
     *
     * Permitido para:
     * - Secretaria
     * - Encarregado Regional
     * - Encarregado Local
     *
     * O aluno permanece arquivado no MOD.
     */
    @PostMapping("/sam")
    @PreAuthorize("hasAuthority('ALUNO_EXCLUIR_SAM')")
    public ResponseEntity<ExclusaoAlunoResponseDTO> excluirNoSam(
            @RequestBody ExclusaoAlunoRequestDTO dto) {

        return ResponseEntity.ok(
                service.excluirNoSam(dto)
        );
    }

    /*
     * Arquivamento direto no MOD.
     *
     * Permitido somente para a Secretaria.
     */
    @PostMapping("/mod")
    @PreAuthorize("hasAuthority('ALUNO_EXCLUIR_MOD')")
    public ResponseEntity<ExclusaoAlunoResponseDTO> arquivarNoMod(
            @RequestBody ExclusaoAlunoRequestDTO dto) {

        return ResponseEntity.ok(
                service.arquivarNoMod(dto)
        );
    }
}