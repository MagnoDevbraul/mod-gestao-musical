package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.AlunoSemMovimentacaoResponseDTO;
import br.com.mod.gestaomusical.service.AlunoSemMovimentacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoSemMovimentacaoController {

    private final AlunoSemMovimentacaoService service;

    public AlunoSemMovimentacaoController(
            AlunoSemMovimentacaoService service) {

        this.service = service;
    }

    @GetMapping("/sem-movimentacao")
    public ResponseEntity<List<AlunoSemMovimentacaoResponseDTO>>
    listarAlunosSemMovimentacao() {

        return ResponseEntity.ok(
                service.listarAlunosSemMovimentacao()
        );
    }
}