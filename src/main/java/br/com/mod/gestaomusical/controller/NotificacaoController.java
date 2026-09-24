package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.NotificacaoRequestDTO;
import br.com.mod.gestaomusical.dto.NotificacaoResponseDTO;
import br.com.mod.gestaomusical.service.NotificacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
@PreAuthorize("hasAuthority('NOTIFICACAO_GERENCIAR')")
public class NotificacaoController {

    private final NotificacaoService service;

    public NotificacaoController(
            NotificacaoService service) {

        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<NotificacaoResponseDTO>>
    listarTodos() {

        return ResponseEntity.ok(
                service.listarTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacaoResponseDTO>
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

    @PostMapping
    public ResponseEntity<NotificacaoResponseDTO>
    salvar(
            @RequestBody NotificacaoRequestDTO dto) {

        return ResponseEntity.ok(
                service.salvar(dto)
        );
    }

    @PatchMapping("/{id}/marcar-lida")
    public ResponseEntity<NotificacaoResponseDTO>
    marcarComoLida(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.marcarComoLida(id)
        );
    }
}