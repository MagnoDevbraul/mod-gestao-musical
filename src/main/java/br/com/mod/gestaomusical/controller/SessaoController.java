package br.com.mod.gestaomusical.controller;

import br.com.mod.gestaomusical.dto.SessaoAtualResponseDTO;
import br.com.mod.gestaomusical.entity.Usuario;
import br.com.mod.gestaomusical.security.UsuarioAutenticadoService;
import br.com.mod.gestaomusical.security.UsuarioSessaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SessaoController {

    private final UsuarioAutenticadoService usuarioAutenticadoService;
    private final UsuarioSessaoService usuarioSessaoService;

    public SessaoController(
            UsuarioAutenticadoService usuarioAutenticadoService,
            UsuarioSessaoService usuarioSessaoService) {

        this.usuarioAutenticadoService = usuarioAutenticadoService;
        this.usuarioSessaoService = usuarioSessaoService;
    }

    /**
     * Retorna os dados do usuário vinculado à sessão atual.
     *
     * O status "online" é obtido diretamente do registro de sessões
     * do Spring Security, sem depender de um campo persistido no banco.
     */
    @GetMapping("/sessao")
    public ResponseEntity<SessaoAtualResponseDTO> consultarSessaoAtual() {

        Usuario usuario =
                usuarioAutenticadoService.obterUsuarioAutenticado();

        boolean online =
                usuarioSessaoService.estaOnline(
                        usuario.getEmail()
                );

        SessaoAtualResponseDTO resposta =
                new SessaoAtualResponseDTO(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getPerfilUsuario().getNome(),
                        online
                );

        return ResponseEntity.ok(resposta);
    }
}