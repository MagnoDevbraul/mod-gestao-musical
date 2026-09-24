package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.HistoricoResponseDTO;
import br.com.mod.gestaomusical.service.HistoricoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historicos")
public class HistoricoController {

    private final HistoricoService service;

    public HistoricoController(
            HistoricoService service) {

        this.service = service;
    }

    /*
     * Consulta completa dos registros da tabela histórico.
     *
     * Restrita à Secretaria.
     */
    @GetMapping
    @PreAuthorize(
            "hasAuthority('AUDITORIA_CONSULTAR')"
    )
    public ResponseEntity<List<HistoricoResponseDTO>>
    listarTodos() {

        return ResponseEntity.ok(
                service.listarTodos()
        );
    }

    /*
     * Consulta de um registro administrativo específico.
     *
     * Restrita à Secretaria.
     */
    @GetMapping("/{id}")
    @PreAuthorize(
            "hasAuthority('AUDITORIA_CONSULTAR')"
    )
    public ResponseEntity<HistoricoResponseDTO>
    buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }

    /*
     * Histórico de desenvolvimento musical
     * de um aluno.
     *
     * Secretaria, Regional, Local e Instrutor
     * possuem HISTORICO_CONSULTAR.
     */
    @GetMapping("/aluno/{alunoId}")
    @PreAuthorize(
            "hasAuthority('HISTORICO_CONSULTAR')"
    )
    public ResponseEntity<List<HistoricoResponseDTO>>
    listarDesenvolvimentoAluno(
            @PathVariable Long alunoId) {

        return ResponseEntity.ok(
                service.listarDesenvolvimentoAluno(
                        alunoId
                )
        );
    }
}