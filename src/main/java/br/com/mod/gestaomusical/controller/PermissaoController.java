package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.AtualizarPermissoesPerfilRequestDTO;
import br.com.mod.gestaomusical.dto.PermissaoResponseDTO;
import br.com.mod.gestaomusical.service.PermissaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissoes")
@PreAuthorize("hasAuthority('PERMISSAO_GERENCIAR')")
public class PermissaoController {

    private final PermissaoService service;

    public PermissaoController(
            PermissaoService service) {

        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PermissaoResponseDTO>>
    listarTodas() {

        return ResponseEntity.ok(
                service.listarTodas()
        );
    }

    @GetMapping("/perfil/{perfilId}")
    public ResponseEntity<List<PermissaoResponseDTO>>
    listarPorPerfil(
            @PathVariable Long perfilId) {

        return ResponseEntity.ok(
                service.listarPorPerfil(
                        perfilId
                )
        );
    }

    @PutMapping("/perfil/{perfilId}")
    public ResponseEntity<List<PermissaoResponseDTO>>
    atualizarPermissoesPerfil(
            @PathVariable Long perfilId,
            @RequestBody AtualizarPermissoesPerfilRequestDTO dto) {

        return ResponseEntity.ok(
                service.atualizarPermissoesPerfil(
                        perfilId,
                        dto
                )
        );
    }
}