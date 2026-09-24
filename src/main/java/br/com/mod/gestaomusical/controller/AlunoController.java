package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.AlunoRequestDTO;
import br.com.mod.gestaomusical.dto.AlunoResponseDTO;
import br.com.mod.gestaomusical.dto.AtualizarAlunoRequestDTO;
import br.com.mod.gestaomusical.service.AlunoService;
import br.com.mod.gestaomusical.service.AtualizacaoAlunoService;
import br.com.mod.gestaomusical.service.RestauracaoAlunoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;
    private final RestauracaoAlunoService restauracaoAlunoService;
    private final AtualizacaoAlunoService atualizacaoAlunoService;

    public AlunoController(
            AlunoService alunoService,
            RestauracaoAlunoService restauracaoAlunoService,
            AtualizacaoAlunoService atualizacaoAlunoService) {

        this.alunoService = alunoService;
        this.restauracaoAlunoService = restauracaoAlunoService;
        this.atualizacaoAlunoService = atualizacaoAlunoService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ALUNO_CONSULTAR')")
    public ResponseEntity<List<AlunoResponseDTO>> listarTodos() {

        return ResponseEntity.ok(
                alunoService.listarTodos()
        );
    }

    @GetMapping("/arquivados")
    @PreAuthorize("hasAuthority('ALUNO_CONSULTAR')")
    public ResponseEntity<List<AlunoResponseDTO>> listarArquivados() {

        return ResponseEntity.ok(
                alunoService.listarArquivados()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ALUNO_CONSULTAR')")
    public ResponseEntity<AlunoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return alunoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ALUNO_CADASTRAR')")
    public ResponseEntity<AlunoResponseDTO> salvar(
            @RequestBody AlunoRequestDTO dto) {

        var alunoSalvo =
                alunoService.salvar(dto);

        return alunoService.buscarPorId(alunoSalvo.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ALUNO_EDITAR')")
    public ResponseEntity<AlunoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody AtualizarAlunoRequestDTO dto) {

        return ResponseEntity.ok(
                atualizacaoAlunoService.atualizar(id, dto)
        );
    }

    @PatchMapping("/{id}/restaurar")
    @PreAuthorize("hasAuthority('ALUNO_RESTAURAR_MOD')")
    public ResponseEntity<AlunoResponseDTO> restaurar(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                restauracaoAlunoService.restaurar(id)
        );
    }
}