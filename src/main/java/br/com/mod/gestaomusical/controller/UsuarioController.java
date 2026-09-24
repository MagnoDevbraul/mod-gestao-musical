package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.AlterarUsuarioAtivoRequestDTO;
import br.com.mod.gestaomusical.dto.AlterarUsuarioPerfilRequestDTO;
import br.com.mod.gestaomusical.dto.UsuarioResponseDTO;
import br.com.mod.gestaomusical.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@PreAuthorize("hasAuthority('USUARIO_GERENCIAR')")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(
            UsuarioService service) {

        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>>
    listarTodos() {

        return ResponseEntity.ok(
                service.listarTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO>
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

    @PatchMapping("/{id}/ativo")
    public ResponseEntity<UsuarioResponseDTO>
    alterarAtivo(
            @PathVariable Long id,
            @RequestBody AlterarUsuarioAtivoRequestDTO dto) {

        return ResponseEntity.ok(
                service.alterarAtivo(
                        id,
                        dto
                )
        );
    }

    @PatchMapping("/{id}/perfil")
    public ResponseEntity<UsuarioResponseDTO>
    alterarPerfil(
            @PathVariable Long id,
            @RequestBody AlterarUsuarioPerfilRequestDTO dto) {

        return ResponseEntity.ok(
                service.alterarPerfil(
                        id,
                        dto
                )
        );
    }
}