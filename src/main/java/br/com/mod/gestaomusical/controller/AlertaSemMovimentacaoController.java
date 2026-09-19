package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.service.AlertaSemMovimentacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/alertas")
public class AlertaSemMovimentacaoController {

    private final AlertaSemMovimentacaoService service;

    public AlertaSemMovimentacaoController(
            AlertaSemMovimentacaoService service) {

        this.service = service;
    }

    @PostMapping("/sem-movimentacao")
    public ResponseEntity<Map<String, Object>> gerarAlertasSemMovimentacao() {

        int totalAlertasGerados = service.gerarAlertas();

        return ResponseEntity.ok(
                Map.of(
                        "mensagem",
                        "Verificação de alunos sem movimentação concluída.",
                        "alertasGerados",
                        totalAlertasGerados
                )
        );
    }
}