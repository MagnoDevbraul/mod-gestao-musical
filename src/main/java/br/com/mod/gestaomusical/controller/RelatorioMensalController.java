package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.RelatorioMensalResponseDTO;
import br.com.mod.gestaomusical.service.RelatorioMensalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relatorios/mensais")
public class RelatorioMensalController {

    private final RelatorioMensalService service;

    public RelatorioMensalController(RelatorioMensalService service) {
        this.service = service;
    }

    @PostMapping("/{ano}/{mes}")
    public ResponseEntity<RelatorioMensalResponseDTO> gerarRelatorio(
            @PathVariable int ano,
            @PathVariable int mes) {

        return ResponseEntity.ok(
                service.gerarRelatorio(ano, mes)
        );
    }

    @GetMapping("/{ano}/{mes}")
    public ResponseEntity<RelatorioMensalResponseDTO> buscarRelatorio(
            @PathVariable int ano,
            @PathVariable int mes) {

        return ResponseEntity.ok(
                service.buscarPorPeriodo(ano, mes)
        );
    }
}